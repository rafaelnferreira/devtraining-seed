package global.genesis

import global.genesis.clustersupport.availability.NamedPublisherStateReceiver
import global.genesis.clustersupport.availability.ProcessState
import global.genesis.clustersupport.availability.ProcessStateManager
import global.genesis.commons.annotation.Module
import global.genesis.config.system.SystemDefinitionService
import global.genesis.db.entity.EntityDescription
import global.genesis.db.entity.TableEntity
import global.genesis.db.entity.repo.AbstractEntityOperation
import global.genesis.db.rx.entity.multi.AsyncEntityDb
import global.genesis.db.rx.entity.multi.AsyncMultiEntityReadWriteGenericSupport
import global.genesis.db.updatequeue.GenericRecordUpdate
import global.genesis.db.updatequeue.RecordUpdate
import global.genesis.gen.config.tables.TX_EVENT
import global.genesis.gen.dao.TxEvent
import global.genesis.gen.dao.enums.alpha.tx_event.EventStatus
import global.genesis.jackson.core.GenesisJacksonMapper
import global.genesis.metrics.MetricService
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import org.msgpack.core.MessagePack
import org.msgpack.core.buffer.InputStreamBufferInput
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject
import kotlin.reflect.KClass
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

private const val THREAD_POOL_SIZE = 10

private val jobDispatcher = Executors
    .newFixedThreadPool(THREAD_POOL_SIZE, object : ThreadFactory {
        private val threadCounter = AtomicInteger()

        override fun newThread(r: Runnable): Thread = Thread(r).also {
            it.name = "${javaClass.simpleName}-${threadCounter.incrementAndGet()}"
        }
    })
    .asCoroutineDispatcher()

interface TxEventPersistentListener {
    fun <E : TableEntity> subscribe(
        consumer: suspend (GenericRecordUpdate<E>, AsyncMultiEntityReadWriteGenericSupport) -> Unit,
    ): Job
}


@Module
class PersistentEventConsumer internal constructor(
    private val asyncEntityDb: AsyncEntityDb,
    private val metricsService: MetricService,
    private val pollingBatchSize: Int,
    private val poolingFrequencyInSeconds: Int,
    private val stateReceiver: NamedPublisherStateReceiver = ProcessStateManager.forPublisher(PersistentEventConsumer::class.java.simpleName),
    private val scope: CoroutineScope = CoroutineScope(SupervisorJob() + jobDispatcher + CoroutineExceptionHandler { _, exception ->
        LOG.error("Unhandled error in consumer scope", exception)
        stateReceiver(ProcessState.DOWN to "One or more persistent listeners are offline")
    }),
) {

    private val failureCounter = AtomicInteger()

    private val descriptorProvider = object : AbstractEntityOperation() {
        fun <E : TableEntity> entityDescription(entity: KClass<E>): EntityDescription<E> = this.description(entity)
    }

    @Inject
    constructor(
        asyncEntityDb: AsyncEntityDb,
        metricsService: MetricService,
        systemDefinitionService: SystemDefinitionService
    ) : this(
        asyncEntityDb = asyncEntityDb,
        metricsService = metricsService,
        poolingFrequencyInSeconds = systemDefinitionService.getOrDefault(
            "PERSISTENT_EVENT_POLLING_FREQUENCY_SECONDS",
            "3"
        ).toInt(),
        pollingBatchSize = systemDefinitionService.getOrDefault("PERSISTENT_EVENT_CONSUMER_BATCH_SIZE", "100").toInt()
    ) {

    }

    inline fun <reified E : TableEntity> subscribe(
        noinline consumer: suspend (GenericRecordUpdate<E>, AsyncMultiEntityReadWriteGenericSupport) -> Unit,
    ): Job = subscribe(E::class, consumer)

    fun <E : TableEntity> subscribe(
        type: KClass<E>,
        consumer: suspend (GenericRecordUpdate<E>, AsyncMultiEntityReadWriteGenericSupport) -> Unit
    ): Job {

        val description = descriptorProvider.entityDescription(type)
        LOG.info("Starting persistent subscription for events from table {}", description.tableName)

        return ticker()
            .onEach {

                try {

                    processEvents(description, consumer)

                } catch (ex: Throwable) {
                    when (ex) {
                        is FailedToProcessRecordException -> {
                            metricsService.counter(
                                METRIC_RECORD_PROCESS_FAIL,
                                TABLE_NAME to description.tableName
                            )
                            handleException(ex)
                        }

                        else -> handleException(ex)
                    }
                }

            }
            .onCompletion { LOG.debug("Subscription for {} completed", description.tableName) }
            .launchIn(scope)
    }

    private suspend fun <E : TableEntity> PersistentEventConsumer.processEvents(
        description: EntityDescription<E>,
        consumer: suspend (GenericRecordUpdate<E>, AsyncMultiEntityReadWriteGenericSupport) -> Unit
    ) {
        val events = queryEvents(description)

        LOG.info("Found {} pending events for table {}", events.size, description.tableName)

        events.forEach { txEvent ->

            processRecord(txEvent.id, description, consumer)

            LOG.debug("Event {}, for table {}, successfully consumed", txEvent.id, description.tableName)

            metricsService.counter(
                METRIC_RECORD_PROCESS_SUCCESS,
                TABLE_NAME to description.tableName
            )

        }

        // full cycle complete, set process to up
        stateReceiver(ProcessState.UP)
    }

    private suspend fun <E : TableEntity> processRecord(
        eventId: Long,
        description: EntityDescription<E>,
        consumer: suspend (GenericRecordUpdate<E>, AsyncMultiEntityReadWriteGenericSupport) -> Unit
    ) {

        val txEvent = asyncEntityDb.get(TxEvent.byId(eventId))
        requireNotNull(txEvent) { "Event with id $eventId not found" }

        try {

            val recordUpdate = unpackFrom(txEvent.eventBlob)
                .asGenericUpdate()
                .map { description.fromDbRecord(it) }

            asyncEntityDb.writeTransaction {
                // call consumer
                consumer.invoke(recordUpdate, this)

                txEvent.eventStatus = EventStatus.CONSUMED
                modify(txEvent)
            }
        } catch (e: Exception) {
            throw FailedToProcessRecordException(description.tableName, txEvent, e)
        }
    }

    private suspend fun handleException(e: Throwable) {

        LOG.error(e.message, e)

        val errorCount = failureCounter.incrementAndGet()
        if (errorCount >= ERROR_COUNT_THRESHOLD) {
            stateReceiver(ProcessState.WARNING to "$errorCount consecutive errors while processing events")
        }

        if (e is FailedToProcessRecordException) {
            val txEvent = e.txEvent.copy {
                val localCount = this.dequeueCount?.inc() ?: 1
                dequeueCount = localCount
                eventStatus = if (localCount >= DEQUEUE_COUNT_THRESHOLD) EventStatus.FAILED else eventStatus
            }

            try {
                asyncEntityDb.modify(txEvent)
            } catch (e: Exception) {
                LOG.error("Failed to update event, execution will be retried in the next cycle", e)
            }

        }
    }

    private suspend fun <E : TableEntity> queryEvents(description: EntityDescription<E>) =
        try {
            // TODO confirm if it is ordered by timestamp
            asyncEntityDb.getRange(
                index = TxEvent.byEventTypeEventStatus(description.tableName, EventStatus.PENDING),
                fields = setOf(TX_EVENT.ID.name)
            )
                .take(pollingBatchSize)
                .toList()

        } catch (e: Exception) {
            throw UnableToQueryDbException(description.tableName, e)
        }

    private fun unpackFrom(msg: ByteArray?): RecordUpdate {
        ByteArrayInputStream(msg).use { inputStream ->
            MessagePack.newDefaultUnpacker(InputStreamBufferInput(ByteArrayInputStream(byteArrayOf()))).use { _ ->
                return GenesisJacksonMapper.defaultMsgPackMapper.readValue(
                    inputStream.readAllBytes(),
                    RecordUpdate::class.java
                )
            }
        }
    }

    private fun ticker(duration: Duration = poolingFrequencyInSeconds.seconds) = flow {
        while (true) {
            emit(Unit)
            delay(duration)
        }
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(PersistentEventConsumer::class.java)
        private const val TABLE_NAME = "tableName"

        private const val ERROR_COUNT_THRESHOLD = 3
        private const val DEQUEUE_COUNT_THRESHOLD = 3

        const val METRIC_RECORD_PROCESS_SUCCESS = "persistent.consumer.records.success"
        const val METRIC_RECORD_PROCESS_FAIL = "persistent.consumer.records.fail"

    }

}

abstract class PersistentConsumerException(message: String, val tableName: String, cause: Exception) :
    RuntimeException(message, cause)

class UnableToQueryDbException(tableName: String, cause: Exception) :
    PersistentConsumerException("Unable to retrieve records from the database for table $tableName", tableName, cause)

class FailedToProcessRecordException(tableName: String, val txEvent: TxEvent, cause: Exception) :
    PersistentConsumerException("Failed while processing event ${txEvent.id} for table $tableName", tableName, cause)

