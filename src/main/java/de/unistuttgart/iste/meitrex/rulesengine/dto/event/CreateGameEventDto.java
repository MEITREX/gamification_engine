package de.unistuttgart.iste.meitrex.rulesengine.dto.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.EventVisibility;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Optional;
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
    @NotBlank
    String eventType;

    @Schema(description = "The visibility of the event. The visibility determines who can see the event." +
                          "If not set, the default visibility of the event type is used.",
            example = "GAME",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true)
    @Nullable
    EventVisibility visibility;

    @Schema(description = "The unique identifier of the player the event belongs to",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED, nullable = true)
    @Nullable
    UUID playerId;

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

    @JsonIgnore
    public Optional<UUID> getOptionalPlayerId() {
        return Optional.ofNullable(playerId);
    }

    @JsonIgnore
    public Optional<UUID> getOptionalParentEventId() {
        return Optional.ofNullable(parentEventId);
    }

    @JsonIgnore
    public Optional<JsonObject> getOptionalData() {
        return Optional.ofNullable(data);
    }

    @JsonIgnore
    public Optional<EventVisibility> getOptionalVisibility() {
        return Optional.ofNullable(visibility);
    }

}