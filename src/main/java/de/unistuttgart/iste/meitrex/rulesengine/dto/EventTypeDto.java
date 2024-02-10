package de.unistuttgart.iste.meitrex.rulesengine.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEventScope;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEventType;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.JsonSchema;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "GameEventTypeDto", description = "The data transfer object for a game event type")
@Value
@With
@Jacksonized
@Builder(toBuilder = true)
@AllArgsConstructor
public class EventTypeDto implements GameEventType {

    @Schema(description = "The name of the event type. Must be unique and only contain uppercase letters and underscores.",
            example = "PLAYER_JOINED",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @Pattern(regexp = "^[A-Z_]+$") // only uppercase letters and underscores
    @NotNull
    @NotBlank
    String identifier;

    @Schema(description = "The description of the event type",
            example = "This event is triggered when a player joins the game",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = false,
            defaultValue = "")
    @NotNull
    @Builder.Default
    String description = "";

    @Schema(description = "The default scope of the event type",
            example = "GAME",
            defaultValue = "GAME",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NotNull
    @Builder.Default
    GameEventScope defaultScope = GameEventScope.GAME;

    @Schema(description = "The schema of the event data. Must be a valid JSON schema of draft 2020. If not set, the event data will be unvalidated." +
                          "If set, the event data will be validated against this schema. If the validation fails, the event will not trigger rules processing.",
            example = "{\"type\": \"object\", \"properties\": {\"playerId\": {\"type\": \"string\"}, \"score\": {\"type\": \"integer\"}}}",
            defaultValue = "true",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            implementation = Object.class)
    @NotNull
    @Builder.Default
    JsonObject eventSchema = new JsonObject();

    @Override
    @JsonIgnore
    public JsonObject getSchemaAsJsonObject() {
        return eventSchema;
    }

    @Override
    @JsonIgnore
    public JsonSchema getJsonSchema() {
        return GameEventType.super.getJsonSchema();
    }

    @NotNull
    public static EventTypeDto from(@NotNull GameEventType gameEventType) {
        return EventTypeDto.builder()
                .identifier(gameEventType.getIdentifier())
                .description(gameEventType.getDescription())
                .defaultScope(gameEventType.getDefaultScope())
                .eventSchema(gameEventType.getSchemaAsJsonObject())
                .build();
    }
}
