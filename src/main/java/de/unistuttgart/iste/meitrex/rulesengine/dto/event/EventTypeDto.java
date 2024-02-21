package de.unistuttgart.iste.meitrex.rulesengine.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.*;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.JsonSchema;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.NonFinal;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.NOT_REQUIRED;
import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Schema(name = "GameEventTypeDto",
        description = "The data transfer object for a game event type")
@NonFinal
@Value
@With
@Jacksonized
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
public class EventTypeDto implements EventType {

    @Schema(description = "The name of the event type. Must be unique and only contain uppercase letters and underscores.",
            example = "PLAYER_JOINED",
            requiredMode = REQUIRED)
    @Pattern(regexp = "^[A-Z_]+$") // only uppercase letters and underscores
    @NotNull
    @NotBlank
    String identifier;

    @Schema(description = "The description of the event type",
            example = "This event is triggered when a player joins the game",
            requiredMode = NOT_REQUIRED, nullable = false,
            defaultValue = "")
    @NotNull
    @Builder.Default
    String description = "";

    @Schema(description = "The default scope of the event type",
            example = "GAME",
            defaultValue = "GAME",
            requiredMode = NOT_REQUIRED)
    @NotNull
    @Builder.Default
    EventVisibility defaultVisibility = EventVisibility.GAME;

    @Schema(description = "The schema of the event data. Must be a valid JSON schema of draft 2020. If not set, the event data will be unvalidated." +
                          "If set, the event data will be validated against this schema. If the validation fails, the event will not trigger rules processing.",
            example = "{\"type\": \"object\", \"properties\": {\"playerId\": {\"type\": \"string\"}, \"score\": {\"type\": \"integer\"}}}",
            defaultValue = "true",
            requiredMode = NOT_REQUIRED,
            implementation = Object.class)
    @NotNull
    @Builder.Default
    JsonObject eventSchema = new JsonObject();

    @Schema(description = "The action to be performed when the event is triggered",
            example = "NONE",
            defaultValue = "NONE",
            requiredMode = NOT_REQUIRED)
    @NotNull
    @Builder.Default
    ActionOnEvent action = ActionOnEvent.NONE;

    @Override
    @JsonIgnore
    public JsonObject getSchemaAsJsonObject() {
        return eventSchema;
    }

    @Override
    @JsonIgnore
    public JsonSchema getJsonSchema() {
        return EventType.super.getJsonSchema();
    }

    @NotNull
    public static EventTypeDto from(@NotNull EventType gameEventType) {
        if (gameEventType instanceof EventTypeDto dto) {
            return dto;
        }

        return EventTypeDto.builder()
                .identifier(gameEventType.getIdentifier())
                .description(gameEventType.getDescription())
                .eventSchema(gameEventType.getSchemaAsJsonObject())
                .defaultVisibility(gameEventType.getDefaultVisibility())
                .action(gameEventType.getAction())
                .build();
    }
}
