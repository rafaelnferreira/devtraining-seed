package mycompany.events

import com.google.inject.Inject
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
import kotlinx.coroutines.rx3.awaitSingleOrNull
import org.slf4j.LoggerFactory

@Module
class EventCounterPartyUploadAsync @Inject constructor(
    private val db: AsyncEntityDb,
    private val entityDb: RxEntityDb,
    private val clientProvider: ClientProvider,
) :
    AsyncEventHandler<CounterPartyUpload, EventReply> {

    override fun schemaValidation() = false

    override suspend fun process(message: Event<CounterPartyUpload>): EventReply {

        val request = genesisSet {
            "MESSAGE_TYPE" with "EVENT_COUNTERPARTY_UPLOAD_RECORD"
            "SOURCE_REF" with message.sourceRef
            "DETAILS" with genesisSet {
                Counterparty.COUNTERPARTY_NAME.name with message.details.name
                Counterparty.COUNTERPARTY_ID.name with message.details.id
            }
        }

        val response = clientProvider.client.suspendRequest(request)

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

    companion object {
        private val LOG = LoggerFactory.getLogger(EventCounterPartyUploadAsync::class.java)
    }

}