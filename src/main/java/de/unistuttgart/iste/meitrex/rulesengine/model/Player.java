package de.unistuttgart.iste.meitrex.rulesengine.model;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * Represents a player in a game.
 */
public interface Player extends IWithCustomData {

    /**
     * A name that is used to identify the player.
     */
    @NotNull
    String getName();

    /**
     * Each player is associated with a game.
     *
     * @return the game the player is associated with
     */
    @NotNull
    Game getGame();

    /**
     * Each player is associated with a user.
     *
     * @return the user id of the player
     */
    @NotNull
    UUID getUserId();

}
