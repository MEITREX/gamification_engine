package de.unistuttgart.iste.meitrex.rulesengine.util.expression;

import io.vertx.core.json.JsonObject;
import lombok.RequiredArgsConstructor;
import org.apache.commons.text.StringSubstitutor;

/**
 * A template string such as "Hello ${name}" that can be evaluated with a context.
 * The context is used to replace the variables in the template string.
 * The context is a map of variable names to their values.
 * The template string can contain variables in the form of ${variableName}.
 * If the variableName is not found in the context, it is left as is in the result.
 */
@RequiredArgsConstructor
public class TemplateString implements EvaluableExpression<String> {

    private final String template;

    @Override
    public EvaluationResult<String> evaluateWithDetails(JsonObject context) {
        StringSubstitutor substitutor = new StringSubstitutor(context::getString)
                .setEscapeChar('\\')
                .setEnableSubstitutionInVariables(true)
                .setValueDelimiter(':')
                .setEnableUndefinedVariableException(false);
        return new EvaluationResult<>(substitutor.replace(template));
    }

}
