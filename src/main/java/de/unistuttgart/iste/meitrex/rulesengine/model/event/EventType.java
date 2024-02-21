package de.unistuttgart.iste.meitrex.rulesengine.model.event;

import de.unistuttgart.iste.meitrex.rulesengine.model.IWithJsonSchema;
import jakarta.validation.constraints.NotNull;

/**
 * Represents a type of game event.
 */
public interface EventType extends IWithJsonSchema {

    /**
     * Returns the unique name of the event type.
     */
    @NotNull
    String getIdentifier();

    /**
     * Returns a human-readable description of the event type.
     */
    @NotNull
    String getDescription();

    /**
     * Returns the default visibility of events of this type.
     * This visibility is used when the visibility is not explicitly set for an event.
     */
    @NotNull
    EventVisibility getDefaultVisibility();


    @NotNull
    ActionOnEvent getAction();
}
