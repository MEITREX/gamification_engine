package de.unistuttgart.iste.meitrex.rulesengine.service.event;

import de.unistuttgart.iste.meitrex.rulesengine.dto.event.EventTypeDto;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.*;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;

import java.util.*;

import static io.vertx.json.schema.common.dsl.Schemas.objectSchema;
import static io.vertx.json.schema.common.dsl.Schemas.stringSchema;

/**
 * Contains predefined event types.
 */
@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class PredefinedEventTypes {

//    public static final EventTypeEntity SYSTEM_MESSAGE = EventTypeEntity.builder()
//            .identifier("SYSTEM_MESSAGE")
//            .description("A message from the system.")
//            .eventSchema(objectSchema().requiredProperty("message", stringSchema()).toJson())
//            .build();

    public static final EventType PLAYER_MESSAGE = EventTypeDto.builder()
            .identifier("PLAYER_MESSAGE")
            .description("A message from a player.")
            .eventSchema(objectSchema()
                    .requiredProperty("message", stringSchema())
                    .toJson())
            .defaultVisibility(EventVisibility.GAME)
            .action(ActionOnEvent.NONE)
            .build();

    //    public static final PlayerBehaviourEntity PLAYER_JOINED = PlayerBehaviourEntity.builder()
//            .identifier("PLAYER_JOINED")
//            .description("A player joined the game.")
//            .eventSchema(objectSchema().toJson())
//            .build();
//
//    public static final PlayerBehaviourEntity PLAYER_LEFT = PlayerBehaviourEntity.builder()
//            .identifier("PLAYER_LEFT")
//            .description("A player left the game.")
//            .eventSchema(objectSchema().toJson())
//            .build();
    public static final EventType UNKNOWN = EventTypeDto.builder()
            .identifier("UNKNOWN")
            .description("An unknown event.")
            .eventSchema(objectSchema().toJson())
            .defaultVisibility(EventVisibility.ADMIN)
            .action(ActionOnEvent.NONE)
            .build();

//    private static final JsonObject SCORE_CHANGE_SCHEMA = objectSchema()
//            .requiredProperty("scoreName", stringSchema())
//            .requiredProperty("scoreBefore", intSchema())
//            .requiredProperty("scoreNew", intSchema())
//            .optionalProperty("reason", stringSchema())
//            .toJson();
//
//    public static final EventTypeEntity PLAYER_SCORE_CHANGED = EventTypeEntity.builder()
//            .identifier("PLAYER_SCORE_CHANGED")
//            .description("A player's score changed.")
//            .eventSchema(SCORE_CHANGE_SCHEMA)
//            .build();
//
//    private static final JsonObject FLAG_CHANGE_SCHEMA = objectSchema()
//            .requiredProperty("flagName", stringSchema())
//            .requiredProperty("flagBefore", booleanSchema())
//            .requiredProperty("flagNew", booleanSchema())
//            .optionalProperty("reason", stringSchema())
//            .toJson();
//
//    public static final EventTypeEntity PLAYER_FLAG_CHANGED = EventTypeEntity.builder()
//            .identifier("PLAYER_FLAG_CHANGED")
//            .description("A player's flag changed.")
//            .eventSchema(FLAG_CHANGE_SCHEMA)
//            .build();
//
//    public static final EventTypeEntity GAME_FLAG_CHANGED = EventTypeEntity.builder()
//            .identifier("GAME_FLAG_CHANGED")
//            .description("A game's flag changed.")
//            .eventSchema(SCORE_CHANGE_SCHEMA)
//            .build();
//
//    public static final EventTypeEntity GAME_SCORE_CHANGED = EventTypeEntity.builder()
//            .identifier("GAME_SCORE_CHANGED")
//            .description("A game's score changed.")
//            .eventSchema(FLAG_CHANGE_SCHEMA)
//            .build();

    private static final Map<String, EventType> PREDEFINED_EVENT_TYPES = Map.of(
            PLAYER_MESSAGE.getIdentifier(), PLAYER_MESSAGE,
            UNKNOWN.getIdentifier(), UNKNOWN
    );

    public static Collection<EventType> getAll() {
        return PREDEFINED_EVENT_TYPES.values();
    }

    public static Optional<EventType> findById(@NotNull String identifier) {
        return Optional.ofNullable(PREDEFINED_EVENT_TYPES.get(identifier));
    }
}
