package de.unistuttgart.iste.meitrex.rulesengine.util.expression;

import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.*;

import java.util.List;

public class JsonSchemaCondition implements EvaluatableExpression<Boolean> {

    private static final String BASE_URI = "https://meitrex.iste.uni-stuttgart.de/schema/";

    private final Validator validator;

    public JsonSchemaCondition(JsonSchema schema) {
        this.validator = Validator.create(schema, new JsonSchemaOptions()
                .setBaseUri(BASE_URI)
                .setDraft(Draft.DRAFT202012)
                .setOutputFormat(OutputFormat.Basic));
    }

    @Override
    public EvaluationResult<Boolean> evaluateWithDetails(JsonObject context) {
        OutputUnit output = validator.validate(context);

        boolean valid = output.getValid();
        List<String> details = output.getErrors() != null
                ? output.getErrors().stream().map(OutputUnit::getError).toList()
                : List.of();

        return new EvaluationResult<>(valid, details);
    }

}
