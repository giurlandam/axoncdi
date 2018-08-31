package org.trinakria.axon;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.messaging.MetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ProcessingGroup("AxonDBEventListener")
public class AxonDBEventListener
{
   private static final Logger LOGGER = LoggerFactory.getLogger(AxonDBEventListener.class);

    public void AxonDBEventListener()
    {
        LOGGER.error("initalize AxonDBEventListener\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    @EventHandler
    public void on(Object event, MetaData eventMetaData)
    {
        LOGGER.info("Logging event {} with metadata {}", event.getClass().toString(), eventMetaData);
    }
}