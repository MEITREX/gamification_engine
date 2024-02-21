package de.unistuttgart.iste.meitrex.rulesengine.model.event;

import io.vertx.core.json.JsonObject;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Represents any event that occurs in a game, representing the user behavior or the game state.
 */
public interface GameEvent {

    /**
     * Returns the unique identifier of the event.
     */
    @NotNull
    UUID getId();

    /**
     * Returns the identifier of the event type.
     */
    @NotNull
    EventType getEventType();

    /**
     * Returns the timestamp of the event.
     */
    @NotNull
    OffsetDateTime getTimestamp();

    /**
     * Returns the timestamp of the most recent child event.
     */
    @NotNull
    OffsetDateTime getMostRecentChildTimestamp();

    @NotNull
    EventVisibility getVisibility();

    @NotNull
    UUID getGameId();

    @Nullable
    UUID getPlayerId();

    @Nullable
    UUID getParentEventId();

    @NotNull
    @SuppressWarnings("java:S1452")
        // wildcard type is needed here
    List<? extends GameEvent> getChildEvents();

    @NotNull
    JsonObject getData();

}
