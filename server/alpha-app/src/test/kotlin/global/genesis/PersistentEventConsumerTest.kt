package global.genesis

import global.genesis.PersistentEventConsumer.Companion.METRIC_RECORD_PROCESS_FAIL
import global.genesis.PersistentEventConsumer.Companion.METRIC_RECORD_PROCESS_SUCCESS
import global.genesis.clustersupport.availability.NamedPublisherStateReceiver
import global.genesis.clustersupport.availability.ProcessState
import global.genesis.clustersupport.availability.ProcessStateReceiver
import global.genesis.commons.config.DbConfig
import global.genesis.db.DbRecord
import global.genesis.db.entity.EntityIndexReference
import global.genesis.db.entity.NonUniqueEntityIndex
import global.genesis.db.entity.UniqueEntityIndex
import global.genesis.db.entity.UniqueEntityIndexReference
import global.genesis.db.rx.entity.multi.AsyncEntityDb
import global.genesis.db.rx.entity.multi.AsyncMultiEntityReadWriteGenericSupport
import global.genesis.db.updatequeue.RecordUpdate
import global.genesis.db.updatequeue.UpdateType
import global.genesis.gen.config.tables.EXTERNAL_TRADE
import global.genesis.gen.dao.ExternalTrade
import global.genesis.gen.dao.TxEvent
import global.genesis.gen.dao.enums.alpha.tx_event.EventStatus
import global.genesis.jackson.core.GenesisJacksonMapper
import global.genesis.metrics.MetricService
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.anyVararg
import org.mockito.kotlin.argThat
import org.mockito.kotlin.atLeastOnce
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.eq
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import kotlin.time.Duration.Companion.seconds

class PersistentEventConsumerTest {

    private val genRecordId = System.currentTimeMillis()

    private val asyncEntityDb = mock<AsyncEntityDb> {
        on { getRange(
            index = any<NonUniqueEntityIndex<TxEvent, EntityIndexReference<TxEvent>>>(),
            fields = any()
        ) } doReturn flowOf(
            TxEvent {
                id = 1
                eventType = EXTERNAL_TRADE.name
            }
        )

        onBlocking {
            get(
                index = any<UniqueEntityIndex<TxEvent, UniqueEntityIndexReference<TxEvent>>>(),
                fields = any()
            )
        } doReturn TxEvent {
            id = 1
            eventType = EXTERNAL_TRADE.name
            eventBlob = packRecordUpdate(RecordUpdate().apply {
                updateType = UpdateType.INSERT
                tableName = EXTERNAL_TRADE.name
                record = DbRecord(EXTERNAL_TRADE.name).apply {
                    setLong(DbConfig.RECORD_ID_FIELD, genRecordId)
                    setLong(DbConfig.TIMESTAMP_FIELD, genRecordId)
                    setLong(EXTERNAL_TRADE.RECORD_ID.name, 1L)
                    setString(EXTERNAL_TRADE.BOOK.name, "BOOK_1")
                }
                modifiedFields = listOf(EXTERNAL_TRADE.BOOK.name)
                emitter = PROCESS_NAME
            })
        }

        onBlocking {
            writeTransaction<Any>(any())
        } doAnswer {
            val closure = it.getArgument<suspend AsyncMultiEntityReadWriteGenericSupport.() -> Any>(0)
            runBlocking { closure(tx) }
            Pair(Object(), emptyList())
        }
    }

    private val metricsService = mock<MetricService>()

    private val stateReceiver = mock<ProcessStateReceiver>()

    private val tx = mock< AsyncMultiEntityReadWriteGenericSupport>()

    @Test
    fun `can consume messages and mark then as CONSUMED`() = runTest {

        // Given one event is waiting to be consumed
        val persistentEventConsumer = createPersistentEventConsumer()

        // When any client work happens successfully
        persistentEventConsumer.subscribe<ExternalTrade> { _, _ -> }

        advanceTimeBy(1.seconds)

        // Then the event is marked as consumed
        verify(tx, times(1))
            .modify(argThat<TxEvent> { eventStatus == EventStatus.CONSUMED })

        // And the state receiver signals the process to be up (since all records completed)
        verify(stateReceiver, times(1))
            .set(eq(PROCESS_NAME), eq(ProcessState.UP))

        // And successful metrics are incremented
        verify(metricsService, times(1))
            .counter(eq(METRIC_RECORD_PROCESS_SUCCESS), anyVararg<Pair<String, String>>())

    }

    @Test
    fun `can retry consuming messages and mark then as FAILED`() = runTest {
        // Given one event is waiting to be consumed
        val persistentEventConsumer = createPersistentEventConsumer()

        // When any client fails due a problem on its internal implementation
        persistentEventConsumer.subscribe<ExternalTrade> { _, _ -> throw Exception("Code can't consume") }

        advanceTimeBy(3.seconds)

        // Then the event is marked as failed as three times have been attempted
        verify(asyncEntityDb, atLeastOnce())
            .modify(argThat<TxEvent> { (dequeueCount ?: 0) >= 1 })

        // And the state receiver signals the process to set itself as WARNING as it has breached the threshold
        verify(stateReceiver, times(1))
            .set(eq(PROCESS_NAME), eq(Pair(ProcessState.WARNING, "3 consecutive errors while processing events")))

        // And failed metrics are incremented
        verify(metricsService, times(3))
            .counter(eq(METRIC_RECORD_PROCESS_FAIL), anyVararg<Pair<String, String>>())
    }

    private fun packRecordUpdate(recordUpdate: RecordUpdate): ByteArray =
        GenesisJacksonMapper.defaultMsgPackMapper.writeValueAsBytes(recordUpdate)

    private fun TestScope.createPersistentEventConsumer(): PersistentEventConsumer {
        val persistentEventConsumer = PersistentEventConsumer(
            asyncEntityDb = asyncEntityDb,
            metricsService = metricsService,
            pollingBatchSize = 10,
            poolingFrequencyInSeconds = 1,
            stateReceiver = NamedPublisherStateReceiver(PROCESS_NAME, stateReceiver),
            scope = backgroundScope
        )
        return persistentEventConsumer
    }

    companion object {
        private const val PROCESS_NAME = "UnitTest"
    }

}