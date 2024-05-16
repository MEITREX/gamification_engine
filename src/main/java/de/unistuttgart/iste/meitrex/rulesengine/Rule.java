package de.unistuttgart.iste.meitrex.rulesengine;

import de.unistuttgart.iste.meitrex.generated.dto.CreateEventInput;
import de.unistuttgart.iste.meitrex.generated.dto.Event;

import java.util.*;

/**
 * Defines the structure for rules within the rules engine. A rule is identified by a unique ID and is triggered by
 * specific event types. It contains logic to check conditions based on the triggering event and to execute actions
 * accordingly.
 */
public interface Rule {

    /**
     * Gets the unique identifier of the rule.
     *
     * @return the unique ID of the rule
     */
    UUID getId();

    /**
     * Retrieves the identifiers of the event types that can trigger this rule.
     *
     * @return a list of event type identifiers that trigger the rule
     */
    List<String> getTriggerEventTypeIdentifiers();

    /**
     * Checks if the rule's conditions are met by the given event.
     *
     * @param triggerEvent the event that triggered the rule
     * @return true if the conditions are met, false otherwise
     */
    boolean checkCondition(Event triggerEvent);

    /**
     * Executes the action defined by the rule if its conditions are met.
     *
     * @param triggerEvent the event that triggered the rule
     * @return an optional containing the event to be created as a result of the action, or an empty optional if no new
     * event should be created
     */
    Optional<CreateEventInput> executeAction(Event triggerEvent);

}
