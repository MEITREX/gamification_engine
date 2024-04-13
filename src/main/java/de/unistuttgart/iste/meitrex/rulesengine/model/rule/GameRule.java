package de.unistuttgart.iste.meitrex.rulesengine.model.rule;

import de.unistuttgart.iste.meitrex.rulesengine.util.expression.EvaluableExpression;

import java.util.List;
import java.util.UUID;

public interface GameRule {

    UUID getId();

    List<String> getTriggerEventTypes();

    EvaluableExpression<Boolean> getCondition();

    @SuppressWarnings("java:S1452")
        // wildcard type is needed here
    List<? extends EventTypeInRule> getCreateEventTypes();
}
