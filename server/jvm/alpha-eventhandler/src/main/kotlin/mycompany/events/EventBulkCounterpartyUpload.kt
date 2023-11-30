package mycompany.events

import com.google.inject.Inject
import global.genesis.alpha.message.event.BulkCounterPartyUpload
import global.genesis.commons.annotation.Module
import global.genesis.commons.model.GenesisSet.Companion.genesisSet
import global.genesis.eventhandler.typed.async.AsyncEventHandler
import global.genesis.message.core.event.Event
import global.genesis.message.core.event.EventReply
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

@Module
class EventBulkCounterpartyUpload @Inject constructor(private val clientProvider: ClientProvider)
    : AsyncEventHandler<BulkCounterPartyUpload, EventReply> {
    override suspend fun process(message: Event<BulkCounterPartyUpload>): EventReply {

        LOG.info("Received bulk event, will dispatch {} sub events", message.details.quantity)

        CoroutineScope(Dispatchers.IO).launch {

            IntRange(1, message.details.quantity).forEach {

                launch {

                    val request = genesisSet {
                        "MESSAGE_TYPE" with "EVENT_COUNTER_PARTY_UPLOAD"
                        "SOURCE_REF" with message.sourceRef
                        "DETAILS" with genesisSet {
                            "ID" with "CP_${it}"
                            "NAME" with "CP_NAME_${it}"
                        }
                    }

                    val response = clientProvider.client.suspendRequest(request)

                    if (response == null || response.messageType.contains("NACK", true)) {
                        LOG.error("Got a nack for CP_${it}: {}", response)
                    }
                }
            }
        }

        return ack()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(EventBulkCounterpartyUpload::class.java)
    }

}