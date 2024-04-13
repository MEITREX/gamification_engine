package de.unistuttgart.iste.meitrex.rulesengine.service.event;

import de.unistuttgart.iste.meitrex.rulesengine.model.event.EventType;
import de.unistuttgart.iste.meitrex.rulesengine.model.rule.JsonSchemaRule;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidatorService {

    public static final String BASE_URI = "https://github.com/MEITREX/gamification_engine/";
    public static final Draft DRAFT = Draft.DRAFT202012;

    private static final JsonSchemaOptions OPTIONS = new JsonSchemaOptions()
            .setBaseUri(BASE_URI)
            .setDraft(DRAFT);

    public Validator getValidatorForRule(JsonSchemaRule rule) {
        // use copy, otherwise apparently the schema is modified,
        // and spring boot tries to persist these changes
        var schemaObject = rule.getConditionSchema().copy();

        return getValidatorForSchema(schemaObject);
    }

    public Validator getValidatorForEventType(EventType eventType) {
        var schemaObject = eventType.getSchemaAsJsonObject().copy();

        return getValidatorForSchema(schemaObject);
    }

    private Validator getValidatorForSchema(JsonObject schemaJsonObject) {
        return Validator.create(JsonSchema.of(schemaJsonObject), OPTIONS);
    }
}
