package mycompany

import global.genesis.PersistentEventConsumer
import global.genesis.commons.annotation.Module
import global.genesis.db.updatequeue.GenericRecordUpdate
import global.genesis.gen.dao.ExternalTrade
import global.genesis.gen.dao.Trade
import kotlinx.coroutines.Job
import org.slf4j.LoggerFactory
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy
import javax.inject.Inject

@Module
class TradeListener @Inject constructor(
    private val persistentEventConsumer: PersistentEventConsumer
) {

    private lateinit var subscription: Job

    @PostConstruct
    fun initialize() {
        subscription = persistentEventConsumer.subscribe<Trade> { update, tx ->
            LOG.info("Processing trade from event {} into ext trade", update)

            when (update) {
                is GenericRecordUpdate.Insert<Trade> -> tx.upsert(update.record.toExternalTrade())
                is GenericRecordUpdate.Modify<Trade> -> tx.upsert(update.newRecord.toExternalTrade())
                is GenericRecordUpdate.Delete<*> -> LOG.info("DELETE does not cause side effect")
            }

        }
        LOG.info("subscription: {}", subscription)
    }

    private fun Trade.toExternalTrade() = ExternalTrade {
        price = this@toExternalTrade.price
    }

    @PreDestroy
    fun tearDown(){
        LOG.debug("Terminating subscription")
        subscription.cancel()
    }

    companion object {
        private val LOG = LoggerFactory.getLogger(TradeListener::class.java)
    }

}