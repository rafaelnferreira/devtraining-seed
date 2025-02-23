package mycompany.integration;

import com.google.inject.Provider;
import com.google.inject.Singleton;
import global.genesis.commons.annotation.ConditionalOnProperty;
import global.genesis.commons.annotation.ProviderOf;
import jakarta.jms.ConnectionFactory;
import org.apache.activemq.artemis.api.core.TransportConfiguration;
import org.apache.activemq.artemis.api.jms.JMSFactoryType;
import org.apache.activemq.artemis.core.remoting.impl.netty.NettyConnectorFactory;
import org.apache.activemq.artemis.core.remoting.impl.netty.TransportConstants;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;

import java.util.HashMap;

import static org.apache.activemq.artemis.api.jms.ActiveMQJMSClient.createConnectionFactoryWithoutHA;

@Singleton
@ProviderOf(type = ConnectionFactory.class)
@ConditionalOnProperty(property = "UpdateQueueIntegration", conditionalValue = "true")
public class JMSIntegrationConnecitionFactovyProvider implements Provider<ConnectionFactory> {

    private final ActiveMQConnectionFactory connectionFactory;

    public JMSIntegrationConnecitionFactovyProvider(){
        var params = new HashMap<String, Object>();
        params.put(TransportConstants.HOST_PROP_NAME, "localhost");
        params.put(TransportConstants.PORT_PROP_NAME, 61616);
        var connectionFactory = createConnectionFactoryWithoutHA(JMSFactoryType.CF, new TransportConfiguration(NettyConnectorFactory.class.getName(), params));
        connectionFactory.setUser("admin");
        connectionFactory.setPassword("admin");

        this.connectionFactory = connectionFactory;
    }

    @Override
    public ConnectionFactory get() {
        return connectionFactory;
    }
}