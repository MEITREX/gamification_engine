package de.unistuttgart.iste.meitrex.rulesengine.model;

import de.unistuttgart.iste.meitrex.rulesengine.dto.game.Score;
import io.vertx.core.json.JsonObject;
import jakarta.validation.constraints.NotNull;

import java.util.*;


/**
 * Interface for objects that have scores, flags and additional data.
 * Scores are integer values that can be used to store any kind of numeric value.
 * Flags are strings that can be used to store boolean values, i.e., a flag is either set or not set.
 * Additional data is a JSON object that can be used to store any other kind of data.
 */
public interface IWithCustomData {

    /**
     * Returns the additional data of the object.
     *
     * @return the additional data as a JSON object
     */
    @NotNull
    JsonObject getAdditionalData();

    /**
     * Returns the scores of the object.
     *
     * @return the scores as a map
     */
    Map<String, Integer> getScores();

    /**
     * Returns the scores of the object as a list of score objects.
     * Useful for graphql.
     *
     * @return the scores as a list
     */
    default List<Score> getScoresAsList() {
        return getScores().entrySet().stream()
                .map(entry -> new Score(entry.getKey(), entry.getValue()))
                .toList();
    }

    /**
     * Returns the flags of the object.
     *
     * @return the flags as a set
     */
    Set<String> getFlags();

    /**
     * Returns the score for the given key.
     *
     * @param key the key of the score
     * @return the score as an optional, empty if the score does not exist
     */
    default OptionalInt getScore(String key) {
        if (getScores().containsKey(key)) {
            return OptionalInt.of(getScores().get(key));
        }
        return OptionalInt.empty();
    }

    /**
     * Returns whether the object has the given flag.
     *
     * @param flag the flag to check
     * @return true if the object has the flag, false otherwise
     */
    default boolean hasFlag(String flag) {
        return getFlags().contains(flag);
    }

    /**
     * Sets the score for the given key.
     *
     * @param key   the key of the score
     * @param value the value of the score
     */
    default void setScore(String key, int value) {
        getScores().put(key, value);
    }

    /**
     * Sets the flag for the object.
     *
     * @param flag the flag to set
     */
    default void setFlag(String flag) {
        getFlags().add(flag);
    }

    /**
     * Removes the flag from the object.
     *
     * @param flag the flag to remove
     */
    default void removeFlag(String flag) {
        getFlags().remove(flag);
    }

    /**
     * Adds the given value to the score for the given key.
     * If the score does not exist, it is created with the given value.
     *
     * @param key   the key of the score
     * @param value the value to add to the score
     */
    default void addScore(String key, int value) {
        getScores().put(key, getScores().getOrDefault(key, 0) + value);
    }


}
