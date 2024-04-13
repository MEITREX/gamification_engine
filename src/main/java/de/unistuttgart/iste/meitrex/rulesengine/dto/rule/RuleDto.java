package de.unistuttgart.iste.meitrex.rulesengine.dto.rule;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.unistuttgart.iste.meitrex.rulesengine.model.rule.JsonSchemaRule;
import de.unistuttgart.iste.meitrex.rulesengine.util.expression.EvaluableExpression;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.NonFinal;
import lombok.extern.jackson.Jacksonized;

import java.util.*;

@Value
@NonFinal
@With
@Jacksonized
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class RuleDto implements JsonSchemaRule {

    @Schema(description = "The id of the rule",
            example = "123e4567-e89b-12d3-a456-426614174000",
            accessMode = Schema.AccessMode.READ_ONLY)
    @Nullable
    UUID id;

    @Schema(description = "The event types that trigger the rule",
            example = "[\"SCORE_CHANGE\", \"COMMIT\"]")
    @NotNull
    @NotEmpty
    List<String> triggerEventTypes;

    @Schema(description = "The schema that the event data must match to trigger the rule",
            example = "{\"type\": \"object\", \"properties\": {  \"data\": {\"properties\": { \"message\": {\"type\": \"string\", \"minLength\": 5}}}}}",
            implementation = Object.class)
    @NotNull
    JsonObject conditionSchema;

    @Schema(description = "The actions that are executed when the rule is triggered")
    @NotNull
    @NotEmpty
    List<EventTypeInRuleDto> createEventTypes;

    @JsonIgnore
    public Optional<UUID> getOptionalId() {
        return Optional.ofNullable(id);
    }

    @Override
    @JsonIgnore
    public EvaluableExpression<Boolean> getCondition() {
        return JsonSchemaRule.super.getCondition();
    }

    public static RuleDto from(JsonSchemaRule gameRule) {
        return RuleDto.builder()
                .id(gameRule.getId())
                .triggerEventTypes(gameRule.getTriggerEventTypes())
                .conditionSchema(gameRule.getConditionSchema())
                .createEventTypes(gameRule.getCreateEventTypes().stream().map(EventTypeInRuleDto::from).toList())
                .build();
    }
}
