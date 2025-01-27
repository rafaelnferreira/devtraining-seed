package mycompany;

import global.genesis.PersistentEventPublisher;
import global.genesis.commons.annotation.Module;
import global.genesis.db.ModifyDetails;
import global.genesis.db.rx.RxDb;
import global.genesis.db.rx.entity.multi.RxEntityDb;
import global.genesis.eventhandler.typed.rx3.Rx3EventHandler;
import global.genesis.gen.config.tables.TRADE;
import global.genesis.gen.dao.LegalEntity;
import global.genesis.gen.dao.Trade;
import global.genesis.gen.dao.TxEvent;
import global.genesis.gen.dao.enums.alpha.tx_event.EventStatus;
import global.genesis.jackson.core.GenesisJacksonMapper;
import global.genesis.message.core.event.Event;
import global.genesis.message.core.event.EventReply;
import io.reactivex.rxjava3.core.Single;
import mycompany.events.ExtIssuance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Random;
import java.util.UUID;

@Module
public class EventExtIssuanceRxHandlerAsync implements Rx3EventHandler<ExtIssuance, EventReply>  {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final RxEntityDb entityDb;
    private final RxDb rxDb;
    private final PersistentEventPublisher persistentEventPublisher;

    @Inject
    public EventExtIssuanceRxHandlerAsync(
            RxEntityDb entityDb,
                                          RxDb rxDb,
                                          PersistentEventPublisher persistentEventPublisher
                                          ) {
        this.entityDb = entityDb;
        this.persistentEventPublisher = persistentEventPublisher;
        this.rxDb = rxDb;
    }

    @Override
    public Single<EventReply> process(Event<ExtIssuance> event) {

        // obtained outside the tx boundary
        var trade = entityDb.get(Trade.byId(event.getDetails().getTradeId())).blockingGet();

        if (trade == null) throw new IllegalArgumentException("Trade does not exist");

        return rxDb.writeTransaction(tx -> {
            // A legal entity insert
            var legalEntity = LegalEntity.builder()
                    .setCountryCode("US")
                    .setName(UUID.randomUUID().toString())
                    .build()
                    .toDbRecord();

            var legalEntityWriteResult = tx.insert(legalEntity).blockingGet();

            trade.setPrice(1 * new Random().nextDouble(0.01, 0.99));

            // A trade modify
            var modifyWriteResult = tx.modify(new ModifyDetails(
                    trade.toDbRecord(), TRADE.BY_ID.INSTANCE.getName()
            )).blockingGet();

            var updates = persistentEventPublisher.createRecordUpdates (legalEntityWriteResult, modifyWriteResult);

            updates.forEach( update -> {

                var eventBlob = GenesisJacksonMapper.defaultMsgPackMapper.writeValueAsBytes(update);

                tx.insert(TxEvent.builder()
                        .setEventStatus(EventStatus.PENDING)
                        .setEventType(update.getTopic())
                        .setEventBlob(eventBlob).build().toDbRecord()).blockingGet();

            });

            return Single.just(true);
        }).doOnSuccess((res) -> logger.info("Successfully saved"))
                .map( res -> new EventReply.EventAck());

    }

    @Override
    public boolean schemaValidation() {
        return false;
    }
}
