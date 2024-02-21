package de.unistuttgart.iste.meitrex.rulesengine.model.rule;

import io.vertx.core.json.JsonObject;

public interface EventTypeInRule {

    /**
     * The identifier of the event type that this rule is for
     */
    String getEventTypeIdentifier();

    /**
     * The data template that will be filled with the data of the event
     * and used to execute the associated action
     */
    JsonObject getDataTemplate();
}
