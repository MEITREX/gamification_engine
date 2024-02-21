package de.unistuttgart.iste.meitrex.rulesengine.model.rule;

import de.unistuttgart.iste.meitrex.rulesengine.util.expression.EvaluableExpression;

import java.util.*;

public interface GameRule {

    UUID getId();

    Set<String> getTriggerEventTypes();

    EvaluableExpression<Boolean> getCondition();

    @SuppressWarnings("java:S1452")
        // wildcard type is needed here
    List<? extends EventTypeInRule> getCreateEventTypes();
}
