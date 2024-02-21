package de.unistuttgart.iste.meitrex.rulesengine.model.event;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * The scope of a game event, which determines the visibility of the event for the users.
 */
@Schema(name = "EventVisibility", description = "The visibility of a game event", enumAsRef = true)
public enum EventVisibility {

    /**
     * The event is visible to all players of the game.
     */
    GAME,

    /**
     * The event is visible to the player that caused the event.
     */
    PLAYER,

    /**
     * The event is only visible to the game engine and administrators.
     */
    ADMIN
}
