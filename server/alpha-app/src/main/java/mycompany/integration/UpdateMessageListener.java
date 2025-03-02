package mycompany.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import global.genesis.PersistentEventPublisher;
import global.genesis.commons.annotation.ConditionalOnProperty;
import global.genesis.db.DbRecord;
import global.genesis.db.engine.WriteResult;
import global.genesis.db.rx.RxDb;
import global.genesis.db.updatequeue.UpdateQueue;
import global.genesis.infra.listener.JmsUpdateQueuePublisher;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSContext;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;

import global.genesis.aliasstore.stores.SimpleAliasStore;
@Singleton
@ConditionalOnProperty(property = "UpdateQueueIntegration", conditionalValue = "true")
public class UpdateMessageListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(UpdateMessageListener.class);
    private final ConnectionFactory connectionFactory;
    private final UpdateQueue updateQueue;
    private final PersistentEventPublisher eventPublisher;
    private final RxDb rxDb;

    private JMSContext jmsContext;

    @Inject
    public UpdateMessageListener(ObjectMapper objectMapper, ConnectionFactory connectionFactory, UpdateQueue updateQueue, PersistentEventPublisher eventPublisher, RxDb rxDb) {
        this.objectMapper = objectMapper;
        this.connectionFactory = connectionFactory;
        this.updateQueue = updateQueue;
        this.eventPublisher = eventPublisher;
        this.rxDb = rxDb;
    }


    @PostConstruct
    public void start() throws JMSException {
        // TODO handle recovery
        jmsContext = connectionFactory.createContext(Session.CLIENT_ACKNOWLEDGE);
        jmsContext.start();
        var queue = jmsContext.createQueue("entity-updates");
        var consumer = jmsContext.createConsumer(queue);
        consumer.setMessageListener(this);
        logger.info("JMSContext started");
    }

    @PreDestroy
    public void tearDown() {
        if (jmsContext != null) {
            jmsContext.close();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = ((TextMessage) message).getText();
            JmsUpdateQueuePublisher.UpdateMessage updateMessage = objectMapper.readValue(messageBody, JmsUpdateQueuePublisher.UpdateMessage.class);

            logger.info("Received message for entity: {}, update type: {}",
                    updateMessage.entityType(), updateMessage.type());

            processMessage(updateMessage);

            jmsContext.acknowledge();

        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage(), e);
        }
    }

    private void processMessage(JmsUpdateQueuePublisher.UpdateMessage message) {
        logger.debug("Processing message - Current version: {}, Previous version: {}",
                message.currentVersion(), message.previousVersion());

        var currentUpdate = new DbRecord(message.entityType());
        message.currentVersion().forEach(currentUpdate::setObject);

        WriteResult result = message.type() == JmsUpdateQueuePublisher.UpdateType.UPDATE
                ? new WriteResult(Collections.emptyList(), Collections.singletonList(currentUpdate))
                : new WriteResult(Collections.singletonList(currentUpdate));


        var updates = eventPublisher.createRecordUpdates(result);
        updates.forEach(u -> {

            // ALIAS STORE MUST MATCH ON BOTH ENDS SO IT CAN BE DESERIALIZED
            // u.setAliasStore(new SimpleAliasStore());

            updateQueue.publishRecordUpdate(u, null);
        });

        logger.info("Updates published {}", updates);
    }
} 