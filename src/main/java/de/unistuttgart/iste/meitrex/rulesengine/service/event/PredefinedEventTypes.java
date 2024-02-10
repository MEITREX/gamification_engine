package de.unistuttgart.iste.meitrex.rulesengine.service.event;

import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEventScope;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.EventTypeEntity;
import io.vertx.core.json.JsonObject;
import lombok.NoArgsConstructor;

import java.util.*;

import static io.vertx.json.schema.common.dsl.Schemas.*;

/**
 * Contains predefined event types.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PredefinedEventTypes {

    public static final EventTypeEntity SYSTEM_MESSAGE = EventTypeEntity.builder()
            .identifier("SYSTEM_MESSAGE")
            .description("A message from the system.")
            .defaultScope(GameEventScope.GAME)
            .eventSchema(objectSchema().requiredProperty("message", stringSchema()).toJson())
            .build();

    public static final EventTypeEntity PLAYER_MESSAGE = EventTypeEntity.builder()
            .identifier("PLAYER_MESSAGE")
            .description("A message from a player.")
            .defaultScope(GameEventScope.GAME)
            .eventSchema(objectSchema().requiredProperty("message", stringSchema()).toJson())
            .build();

    public static final EventTypeEntity PLAYER_JOINED = EventTypeEntity.builder()
            .identifier("PLAYER_JOINED")
            .description("A player joined the game.")
            .defaultScope(GameEventScope.GAME)
            .eventSchema(schema().toJson())
            .build();

    public static final EventTypeEntity PLAYER_LEFT = EventTypeEntity.builder()
            .identifier("PLAYER_LEFT")
            .description("A player left the game.")
            .defaultScope(GameEventScope.GAME)
            .eventSchema(schema().toJson())
            .build();

    private static final JsonObject SCORE_CHANGE_SCHEMA = objectSchema()
            .requiredProperty("scoreName", stringSchema())
            .requiredProperty("scoreBefore", intSchema())
            .requiredProperty("scoreNew", intSchema())
            .optionalProperty("reason", stringSchema())
            .toJson();

    public static final EventTypeEntity PLAYER_SCORE_CHANGED = EventTypeEntity.builder()
            .identifier("PLAYER_SCORE_CHANGED")
            .description("A player's score changed.")
            .defaultScope(GameEventScope.PLAYER)
            .eventSchema(SCORE_CHANGE_SCHEMA)
            .build();

    private static final JsonObject FLAG_CHANGE_SCHEMA = objectSchema()
            .requiredProperty("flagName", stringSchema())
            .requiredProperty("flagBefore", booleanSchema())
            .requiredProperty("flagNew", booleanSchema())
            .optionalProperty("reason", stringSchema())
            .toJson();

    public static final EventTypeEntity PLAYER_FLAG_CHANGED = EventTypeEntity.builder()
            .identifier("PLAYER_FLAG_CHANGED")
            .description("A player's flag changed.")
            .defaultScope(GameEventScope.PLAYER)
            .eventSchema(FLAG_CHANGE_SCHEMA)
            .build();

    public static final EventTypeEntity GAME_FLAG_CHANGED = EventTypeEntity.builder()
            .identifier("GAME_FLAG_CHANGED")
            .description("A game's flag changed.")
            .defaultScope(GameEventScope.GAME)
            .eventSchema(SCORE_CHANGE_SCHEMA)
            .build();

    public static final EventTypeEntity GAME_SCORE_CHANGED = EventTypeEntity.builder()
            .identifier("GAME_SCORE_CHANGED")
            .description("A game's score changed.")
            .defaultScope(GameEventScope.GAME)
            .eventSchema(FLAG_CHANGE_SCHEMA)
            .build();

    private static final Map<String, EventTypeEntity> PREDEFINED_EVENT_TYPES = Map.of(
            SYSTEM_MESSAGE.getIdentifier(), SYSTEM_MESSAGE,
            PLAYER_MESSAGE.getIdentifier(), PLAYER_MESSAGE,
            PLAYER_JOINED.getIdentifier(), PLAYER_JOINED,
            PLAYER_LEFT.getIdentifier(), PLAYER_LEFT,
            PLAYER_SCORE_CHANGED.getIdentifier(), PLAYER_SCORE_CHANGED,
            PLAYER_FLAG_CHANGED.getIdentifier(), PLAYER_FLAG_CHANGED,
            GAME_FLAG_CHANGED.getIdentifier(), GAME_FLAG_CHANGED,
            GAME_SCORE_CHANGED.getIdentifier(), GAME_SCORE_CHANGED
    );

    public static Collection<EventTypeEntity> getAll() {
        return PREDEFINED_EVENT_TYPES.values();
    }

    public static Optional<EventTypeEntity> findById(String identifier) {
        return Optional.ofNullable(PREDEFINED_EVENT_TYPES.get(identifier));
    }
}
