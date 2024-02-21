package de.unistuttgart.iste.meitrex.rulesengine.dto.event;


import de.unistuttgart.iste.meitrex.rulesengine.model.event.EventVisibility;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEvent;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Schema(name = "GameEventDto", description = "The data transfer object for a game event")
@Value
@Jacksonized
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class GameEventDto implements GameEvent {

    @Schema(description = "The unique identifier of the event",
            example = "123e4567-e89b-12d3-a456-426614174000",
            accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull
    UUID id;

    @Schema(description = "The timestamp of the event",
            example = "2021-08-01T12:00:00Z",
            accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull
    OffsetDateTime timestamp;

    @Schema(description = "The timestamp of the most recent child event. Useful for ordering events.",
            example = "2021-08-01T12:00:00Z",
            accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull
    OffsetDateTime mostRecentChildTimestamp;

    @Schema(description = "The type of the event",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull
    EventTypeDto eventType;

    @Schema(description = "The visibility of the event",
            requiredMode = Schema.RequiredMode.REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            defaultValue = "GAME")
    @NotNull
    EventVisibility visibility;

    @Schema(description = "The player associated with the event",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Nullable
    UUID playerId;

    @Schema(description = "The game associated with the event",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = false)
    @NotNull
    UUID gameId;

    @Schema(description = "The data of the event. The structure of the data depends on the event type.",
            example = "{\"key\": \"value\"}",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = false,
            defaultValue = "{}")
    @NotNull
    @Builder.Default
    JsonObject data = new JsonObject();

    @Schema(description = "The id of the parent event of this event. " +
                          "If set, this event is a sub-event of the parent event. " +
                          "If not set, this event is a root event.",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @Nullable
    UUID parentEventId;

    @Schema(description = "The child events of this event",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = false,
            defaultValue = "[]")
    @NotNull
    @Builder.Default
    List<GameEventDto> childEvents = List.of();

    public static GameEventDto from(GameEvent event) {
        if (event instanceof GameEventDto dto) {
            return dto;
        }
        return GameEventDto.builder()
                .id(event.getId())
                .timestamp(event.getTimestamp())
                .mostRecentChildTimestamp(event.getMostRecentChildTimestamp())
                .eventType(EventTypeDto.from(event.getEventType()))
                .visibility(event.getVisibility())
                .playerId(event.getPlayerId())
                .gameId(event.getGameId())
                .data(event.getData())
                .parentEventId(event.getParentEventId())
                .childEvents(event.getChildEvents().stream().map(GameEventDto::from).toList())
                .build();
    }
}
