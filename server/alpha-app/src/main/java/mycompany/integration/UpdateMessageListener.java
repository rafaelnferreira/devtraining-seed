package mycompany.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import global.genesis.PersistentEventPublisher;
import global.genesis.commons.annotation.ConditionalOnProperty;
import global.genesis.db.DbRecord;
import global.genesis.db.engine.WriteResult;
import global.genesis.db.updatequeue.UpdateQueue;
import jakarta.jms.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Collections;
import java.util.Map;

@Singleton
@ConditionalOnProperty(property = "UpdateQueueIntegration", conditionalValue = "true")
public class UpdateMessageListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final Logger logger = LoggerFactory.getLogger(UpdateMessageListener.class);
    private final ConnectionFactory connectionFactory;
    private final UpdateQueue updateQueue;
    private final PersistentEventPublisher eventPublisher;

    private JMSContext jmsContext;

    @Inject
    public UpdateMessageListener(ObjectMapper objectMapper, ConnectionFactory connectionFactory, UpdateQueue updateQueue, PersistentEventPublisher eventPublisher) {
        this.objectMapper = objectMapper;
        this.connectionFactory = connectionFactory;
        this.updateQueue = updateQueue;
        this.eventPublisher = eventPublisher;
    }

    enum UpdateType {
        INSERT,
        UPDATE,
        DELETE
    }

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS)
    public record UpdateMessage(
        @JsonProperty("entityType")
        String entityType,
        @JsonProperty("currentVersion")
        Map<String, Object> currentVersion,
        @JsonProperty("previousVersion")
        Map<String, Object> previousVersion,
        @JsonProperty("type")
        UpdateType type
    ) {}

    @PostConstruct
    public void start() throws JMSException {
        // TODO handle recovery
        jmsContext = connectionFactory.createContext( Session.CLIENT_ACKNOWLEDGE);
        jmsContext.start();
        var topic = jmsContext.createTopic("entity-updates");
        var consumer = jmsContext.createConsumer(topic);
        consumer.setMessageListener(this);
    }

    @PreDestroy
    public void tearDown() {
        if (jmsContext != null ) {
            jmsContext.close();
        }
    }

    @Override
    public void onMessage(Message message) {
        try {
            String messageBody = ((TextMessage) message).getText();
            UpdateMessage updateMessage = objectMapper.readValue(messageBody, UpdateMessage.class);
            
            logger.info("Received message for entity: {}, update type: {}", 
                updateMessage.entityType(), updateMessage.type());
            
            processMessage(updateMessage);

            jmsContext.acknowledge();
            
        } catch (Exception e) {
            logger.error("Error processing message: {}", e.getMessage(), e);
            jmsContext.rollback();
        }
    }

    private void processMessage(UpdateMessage message) {
        logger.debug("Processing message - Current version: {}, Previous version: {}",
            message.currentVersion(), message.previousVersion());

        var currentUpdate = new DbRecord( message.entityType);
        message.currentVersion.forEach(currentUpdate::setObject);

        WriteResult result = message.type == UpdateType.DELETE
                ? new WriteResult(Collections.emptyList(), Collections.singletonList(currentUpdate))
                : new WriteResult(Collections.singletonList(currentUpdate));

        var updates =eventPublisher.createRecordUpdates(result);
        updates.forEach( u -> updateQueue.publishRecordUpdate(u, result));
    }
} 