package org.trinakria.axon;

import com.thoughtworks.xstream.XStream;
import io.axoniq.axondb.client.AxonDBConfiguration;
import io.axoniq.axondb.client.axon.AxonDBEventStore;
import org.axonframework.cdi.transaction.JtaTransactionManager;
import org.axonframework.common.jpa.EntityManagerProvider;
import org.axonframework.common.jpa.SimpleEntityManagerProvider;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.config.EventProcessingConfiguration;
import org.axonframework.config.ModuleConfiguration;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.jpa.JpaTokenStore;
import org.axonframework.serialization.Serializer;
import org.axonframework.serialization.xml.CompactDriver;
import org.axonframework.serialization.xml.XStreamSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@ApplicationScoped
public class AxonConfiguration
{
    private static final Logger LOGGER = LoggerFactory.getLogger(AxonConfiguration.class);

    @Produces
    @PersistenceContext
    private EntityManager entityManager;

    @Produces
    @ApplicationScoped
    public EntityManagerProvider entityManagerProvider(
            EntityManager entityManager)
    {
        return new SimpleEntityManagerProvider(entityManager);
    }

    @ApplicationScoped
    @Produces
    public TransactionManager containerTxManager()
    {
        return new JtaTransactionManager();
    }

    @Produces
    @ApplicationScoped
    public EventBus eventStore(AxonDBConfiguration axonDBConfiguration, Serializer serializer)
    {
        LOGGER.info("AxonDBEventStore initializing...");

        AxonDBEventStore axonDBEventStore = new AxonDBEventStore(axonDBConfiguration, serializer);
        return axonDBEventStore;
    }

    @Produces
    public AxonDBConfiguration axonDBConfiguration()
    {
        LOGGER.info("AxonDBConfiguration initializing...");
        AxonDBConfiguration config = new AxonDBConfiguration();
        config.setServers("localhost:8123");
        return config;
    }

    @Produces
    @ApplicationScoped
    public TokenStore tokenStore(EntityManagerProvider entityManagerProvider,
            Serializer serializer)
    {
        return new JpaTokenStore(entityManagerProvider, serializer);
    }

    @Produces
    @ApplicationScoped
    public Serializer serializer()
    {
        XStream xStream = new XStream(new CompactDriver());
        XStream.setupDefaultSecurity(xStream);
        xStream.allowTypesByWildcard(new String[] { "org.trinakria.**", "org.axonframework.**" });
        return new XStreamSerializer(xStream);
    }

    @Produces
    @ApplicationScoped
    public ModuleConfiguration eventHandlerConfiguration()
    {
        EventProcessingConfiguration eventProcessingConfiguration = new EventProcessingConfiguration();
        eventProcessingConfiguration.usingTrackingProcessors();
        return eventProcessingConfiguration;
    }
}
