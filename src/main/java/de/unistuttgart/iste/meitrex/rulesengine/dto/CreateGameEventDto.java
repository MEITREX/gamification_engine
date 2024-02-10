package de.unistuttgart.iste.meitrex.rulesengine.dto;

import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEventScope;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Schema(name = "CreateGameEventDto",
        description = "The data transfer object to create a game event",
        accessMode = Schema.AccessMode.WRITE_ONLY)
@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
@With
public class CreateGameEventDto {

    @Schema(description = "The type of the event. Must be a registered event type.",
            example = "player_joined",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @NotBlank String eventType;

    @Schema(description = "The unique identifier of the game the event belongs to",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true)
    @Nullable
    UUID gameId;

    @Schema(description = "The unique identifier of the player the event belongs to",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true)
    @Nullable
    UUID playerId;

    @Schema(description = "The unique identifier of the group the event belongs to",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true)
    @Nullable
    UUID groupId;

    @Schema(description = "The scope of the event. If not set, this will automatically determined by the event type and the presence of the player or group id. If set, the player or group id will be ignored.",
            example = "GAME",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = false)
    @NotNull GameEventScope scope;

    @Schema(description = "The unique identifier of the parent event. If set, this event is a sub-event of the parent event. If not set, this event is a root event.",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true)
    @Nullable
    UUID parentEventId;

    @Schema(description = "The data of the event. The structure of the data depends on the event type.",
            example = "{\"key\": \"value\"}",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true,
            implementation = Object.class)
    @Nullable
    JsonObject data;

}