package mycompany.events

import com.google.common.util.concurrent.FutureCallback
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture
import com.google.common.util.concurrent.MoreExecutors
import com.google.inject.Inject
import com.google.inject.Provider
import global.genesis.alpha.message.event.CounterPartyUpload
import global.genesis.commons.annotation.Module
import global.genesis.commons.model.GenesisSet
import global.genesis.commons.model.GenesisSet.Companion.genesisSet
import global.genesis.db.rx.entity.multi.AsyncEntityDb
import global.genesis.db.rx.entity.multi.RxEntityDb
import global.genesis.eventhandler.typed.async.AsyncEventHandler
import global.genesis.gen.dao.Counterparty
import global.genesis.message.core.event.Event
import global.genesis.message.core.event.EventReply
import global.genesis.net.GenesisMessageClient
import kotlinx.coroutines.CancellableContinuation
import kotlinx.coroutines.future.await
import kotlinx.coroutines.rx3.awaitSingleOrNull
import kotlinx.coroutines.suspendCancellableCoroutine
import org.slf4j.LoggerFactory
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import javax.annotation.PostConstruct
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Module
class EventCounterPartyUploadAsync @Inject constructor(
    private val db: AsyncEntityDb,
    private val entityDb: RxEntityDb,
    private val clientProvider: Provider<GenesisMessageClient>,
) :
    AsyncEventHandler<CounterPartyUpload, EventReply> {

    private lateinit var client: GenesisMessageClient

    @PostConstruct
    fun init() {
        client = clientProvider.get()
        LOG.info("Client is: {}", client)
    }

    override fun schemaValidation() = false

    override suspend fun process(message: Event<CounterPartyUpload>): EventReply {

        val request = genesisSet {
            "MESSAGE_TYPE" with "EVENT_COUNTERPARTY_UPLOAD_RECORD"
            //"SOURCE_REF" with UUID.randomUUID().toString()
            "DETAILS" with genesisSet {
                Counterparty.COUNTERPARTY_NAME.name with message.details.name
                Counterparty.COUNTERPARTY_ID.name with message.details.id
            }
        }

//        val response = clientProvider.client.suspendRequest(request)
        val response = send(request).await()

        if (response == null || response.messageType.contains("NACK", true)) {
            val reason = "Totally broken, got a null/nack from the response: $response"
            LOG.error(reason)
            return nack(reason)
        }

        val id = message.details.id ?: response.getArray<GenesisSet>("GENERATED")?.first()?.getString("GENERATED_ID")
        if (id == null) {
            val reason = "Response didn't return the ID of the generated object"
            LOG.error(reason)
            return nack(reason)
        }

        val counterparty = Counterparty {
            this.counterpartyId = id
        }

        // val persistedCounterparty = db.get(counterparty)
        val persistedCounterparty = entityDb.get(counterparty).awaitSingleOrNull()

        if (persistedCounterparty == null) {
            val reason = "Counterparty with ID $id returned from the event handler but the DB told it isn't there"
            LOG.error(reason)
            return nack(reason)
        }

        return ack()
    }

    private fun send(set: GenesisSet): CompletableFuture<GenesisSet> {
        val future = CompletableFuture<GenesisSet>()
        client.sendReqRep(set) { future.complete(it) }
        return future
    }

    private suspend fun <T> ListenableFuture<T>.await(): T? {
        // fast path when CompletableFuture is already done (does not suspend)
        if (isDone) {
            try {
                @Suppress("UNCHECKED_CAST")
                return get() as T
            } catch (e: ExecutionException) {
                throw e.cause ?: e // unwrap original cause from ExecutionException
            }
        }
        // slow path -- suspend
        return suspendCancellableCoroutine { cont: CancellableContinuation<T?> ->
            Futures.addCallback(
                this,
                object : FutureCallback<T> {
                    override fun onFailure(t: Throwable) {
                        cont.resumeWithException(t)
                    }

                    override fun onSuccess(result: T?) {
                        cont.resume(result)
                    }
                },
                MoreExecutors.directExecutor()
            )
            cont.invokeOnCancellation {
                (this as? ListenableFuture<T>)?.cancel(false)
            }
        }
    }


    companion object {
        private val LOG = LoggerFactory.getLogger(EventCounterPartyUploadAsync::class.java)
    }

}