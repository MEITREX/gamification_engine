package de.unistuttgart.iste.meitrex.rulesengine.model.game;

import de.unistuttgart.iste.meitrex.rulesengine.model.IWithCustomData;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

/**
 * The interface for a game
 */
public interface Game extends IWithCustomData {

    /**
     * The name of the game
     */
    @NotNull
    String getName();

    /**
     * The unique identifier of the game
     */
    @NotNull
    UUID getId();
}
