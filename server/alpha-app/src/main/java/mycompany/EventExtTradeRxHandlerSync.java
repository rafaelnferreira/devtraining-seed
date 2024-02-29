package mycompany;

import com.google.inject.Inject;
import global.genesis.commons.annotation.Module;
import global.genesis.db.rx.entity.multi.RxEntityDb;
import global.genesis.eventhandler.typed.sync.SyncEventHandler;
import global.genesis.gen.dao.ExternalTrade;
import global.genesis.message.core.event.Event;
import global.genesis.message.core.event.EventReply;
import io.reactivex.rxjava3.disposables.Disposable;
import mycompany.events.ExtTradeSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

@Module
public class EventExtTradeRxHandlerSync implements SyncEventHandler<ExtTradeSync, EventReply> {

    private final RxEntityDb db;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Inject
    public EventExtTradeRxHandlerSync(RxEntityDb db) {
        this.db = db;
    }

    @Override
    public EventReply process(Event<ExtTradeSync> externalTradeEvent) {
        Disposable subscribe = db.getBulk(ExternalTrade.class)
                .delay(100, TimeUnit.MILLISECONDS)
                .subscribe(t -> {
                    logger.debug("Consuming trade: {}", t.getTradeIdInt());
                });

        int _20Seconds = 1000 * 20;
        logger.info("Subscription is on, sleeping for {} seconds before unsubscribe", _20Seconds);

        sleep(_20Seconds);

        logger.info("Disposing subscription");

        subscribe.dispose();

        logger.info("Finished without throwing exception!");

        return new EventReply.EventAck();
    }

    private static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
