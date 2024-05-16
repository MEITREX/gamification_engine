package de.unistuttgart.iste.meitrex.rulesengine.model.rule;

import de.unistuttgart.iste.meitrex.rulesengine.util.expression.EvaluableExpression;
import io.vertx.core.json.JsonObject;

public interface JsonSchemaRule extends GameRule {

    JsonObject getConditionSchema();

    @Override
    default EvaluableExpression<Boolean> getCondition() {
        return null; // TODO: Implement this
    }
}
