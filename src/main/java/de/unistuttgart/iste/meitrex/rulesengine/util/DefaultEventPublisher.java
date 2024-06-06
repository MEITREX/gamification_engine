package de.unistuttgart.iste.meitrex.rulesengine.util;

import de.unistuttgart.iste.meitrex.generated.dto.CreateEventInput;
import de.unistuttgart.iste.meitrex.generated.dto.Event;

/**
 * Default event publisher for the gamification engine, using Event and CreateEventInput.
 */
public class DefaultEventPublisher extends EventPublisher<Event, CreateEventInput> {

    public DefaultEventPublisher(EventPersistence<Event, CreateEventInput> eventPersistence) {
        super(eventPersistence);
    }
}
