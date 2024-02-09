package de.unistuttgart.iste.meitrex.rulesengine.model.event;

/**
 * The scope of a game event, which determines the visibility of the event for the users.
 */
public enum GameEventScope {

    /**
     * The event is visible to all users of all games.
     */
    GLOBAL,

    /**
     * The event is visible to the player that caused the event.
     */
    PLAYER,

    /**
     * The event is visible to all players of the game.
     */
    GAME
}
