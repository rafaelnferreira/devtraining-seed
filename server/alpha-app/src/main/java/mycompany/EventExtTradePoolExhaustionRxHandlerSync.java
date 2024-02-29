package mycompany;

import com.google.inject.Inject;
import global.genesis.commons.annotation.Module;
import global.genesis.db.rx.entity.multi.RxEntityDb;
import global.genesis.eventhandler.typed.sync.SyncEventHandler;
import global.genesis.gen.dao.ExternalTrade;
import global.genesis.message.core.event.Event;
import global.genesis.message.core.event.EventReply;
import mycompany.events.ExtTradePoolExhaustionAsync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Module
public class EventExtTradePoolExhaustionRxHandlerSync implements SyncEventHandler<ExtTradePoolExhaustionAsync, EventReply> {

    private final RxEntityDb db;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public EventExtTradePoolExhaustionRxHandlerSync(RxEntityDb db) {
        this.db = db;
    }

    @Override
    public EventReply process(Event<ExtTradePoolExhaustionAsync> externalTradeEvent) {
        logger.info("Started loading ...");

        List<ExternalTrade> trades = db.getBulk(ExternalTrade.class)
                .flatMapMaybe(db::get)
                .delay(1, TimeUnit.SECONDS)
                .doOnNext( (t) -> logger.debug("Item published: {}", t.getTradeIdInt() ))
                .take(10000)
                .toList()
                .blockingGet();

        logger.info("Finished loading {} trades.", trades.size());

        return new EventReply.EventAck();
    }
}
