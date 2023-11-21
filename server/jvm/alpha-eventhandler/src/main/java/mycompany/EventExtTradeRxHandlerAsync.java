package mycompany;

import com.google.inject.Inject;
import global.genesis.commons.annotation.Module;
import global.genesis.db.rx.entity.multi.RxEntityDb;
import global.genesis.eventhandler.typed.rx3.Rx3EventHandler;
import global.genesis.gen.dao.ExternalTrade;
import global.genesis.message.core.event.Event;
import global.genesis.message.core.event.EventReply;
import io.reactivex.rxjava3.core.Single;
import mycompany.events.ExtTradeAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Module
public class EventExtTradeRxHandlerAsync implements Rx3EventHandler<ExtTradeAsync, EventReply> {

    private final RxEntityDb db;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public EventExtTradeRxHandlerAsync(RxEntityDb db) {
        this.db = db;
    }

    @Override
    public Single<EventReply> process(Event<ExtTradeAsync> externalTradeEvent) {

        return db.getBulk(ExternalTrade.class)
                .doOnNext(t -> {
                    if (t.getTradeIdInt() % 1000 == 0) {
                        logger.info("Processed a chunk of 1000 records");
                    }
                })
                .reduce(0, (Integer acc, ExternalTrade trade) -> trade.getQuantity() + acc)
                .doOnEvent( (total, ex) -> logger.info("Accumulated a total of {}", total))
                .map( total -> new EventReply.EventAck());

    }

    @Override
    public boolean schemaValidation() {
        return false;
    }
}
