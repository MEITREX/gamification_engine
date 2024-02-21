package de.unistuttgart.iste.meitrex.rulesengine.util.expression;

import io.vertx.core.json.JsonObject;

/**
 * This interface represents any user defined expression that can be evaluated
 * using the data from the context.
 *
 * @param <T> The type of the expression result
 */
@FunctionalInterface
public interface EvaluableExpression<T> {

    /**
     * Evaluates the expression using the given context.
     *
     * @param context context data to evaluate the expression with
     * @return The result of the evaluation
     */
    default T evaluate(JsonObject context) {
        return evaluateWithDetails(context).getResult();
    }

    /**
     * Evaluates the expression using the given context and returns the result and additional details.
     *
     * @param context context data to evaluate the expression with
     * @return The result of the evaluation and additional details
     */
    EvaluationResult<T> evaluateWithDetails(JsonObject context);
}
