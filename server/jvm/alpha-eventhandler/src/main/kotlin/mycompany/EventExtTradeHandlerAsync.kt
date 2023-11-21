package mycompany

import com.google.inject.Inject
import global.genesis.gen.dao.ExternalTrade
import global.genesis.message.core.event.Event
import global.genesis.message.core.event.EventReply
import global.genesis.commons.annotation.Module
import global.genesis.db.rx.entity.multi.AsyncEntityDb
import global.genesis.eventhandler.typed.async.AsyncEventHandler
import kotlinx.coroutines.delay
import org.slf4j.LoggerFactory

@Module
class EventExtTradeHandlerAsync @Inject constructor(private val db: AsyncEntityDb) :
    AsyncEventHandler<ExternalTrade, EventReply> {

    override fun schemaValidation() = false

    override suspend fun process(message: Event<ExternalTrade>): EventReply {

        LOG.info("Slow consumer about to start")

        db.getBulk<ExternalTrade>()
            .collect { trade ->
                val roughlyEveryHundred = trade.tradeIdInt % 100 == 0
                val timeToConsume = if (roughlyEveryHundred) 5000L else 1000L
                delay(timeToConsume)

                if (roughlyEveryHundred) {
                    LOG.debug("Consumed trade with ID: {}", trade.tradeIdInt)
                }
            }

        LOG.info("Finished")

        return ack()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(EventExtTradeHandlerAsync::class.java)
    }

}