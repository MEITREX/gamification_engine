package de.unistuttgart.iste.meitrex.rulesengine.persistence;

import de.unistuttgart.iste.meitrex.common.testutil.MeitrexPostgresSqlContainer;
import de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.*;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.*;
import io.vertx.core.json.JsonObject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.*;

import static de.unistuttgart.iste.meitrex.rulesengine.matcher.GameMatcher.sameGameAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests that game entities can be saved and deleted.
 */
@SpringBootTest
@Testcontainers
@ExtendWith(MeitrexPostgresSqlContainer.class)
class GameRepositoryTest {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameEventRepository gameEventRepository;

    @Test
    @Transactional
    @Commit
    void testSaveAndDelete() {
        GameEntity gameEntity = GameEntity.builder()
                .name("Test Game")
                .flags(Set.of("flag1", "flag2"))
                .scores(Map.of("score1", 1, "score2", 2))
                .additionalData(JsonObject.of("key", "value", "key2", "value2"))
                .build();

        gameRepository.save(gameEntity);
        GameEntity createdGameEntity = gameRepository.findByIdOrThrow(gameEntity.getId());

        assertThat(createdGameEntity, is(sameGameAs(gameEntity)));

        gameRepository.deleteById(gameEntity.getId());

        assertFalse(gameRepository.existsById(gameEntity.getId()));
    }

    @Test
    @Commit
    void testDeleteWithData() {
        GameEntity gameEntity = GameEntity.builder()
                .name("Test Game")
                .flags(Set.of("flag1", "flag2"))
                .scores(Map.of("score1", 1, "score2", 2))
                .additionalData(JsonObject.of("key", "value", "key2", "value2"))
                .build();

        gameRepository.save(gameEntity);

        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(new PlayerId(gameEntity.getId(), UUID.randomUUID()))
                .game(gameEntity)
                .name("Test Player")
                .flags(Set.of("flag1", "flag2"))
                .scores(Map.of("score1", 1, "score2", 2))
                .additionalData(JsonObject.of("key", "value", "key2", "value2"))
                .build();

        playerRepository.save(playerEntity);

        GameEventEntity gameEventEntity = GameEventEntity.builder()
                .gameId(gameEntity.getId())
                .data(JsonObject.of("key", "value", "key2", "value2"))
                .timestamp(OffsetDateTime.now())
                .mostRecentChildTimestamp(OffsetDateTime.now())
                .eventTypeIdentifier("UNKNOWN")
                .build();

        gameEventRepository.save(gameEventEntity);

        gameRepository.deleteById(gameEntity.getId());

        assertThat(playerRepository.findAll(), empty());
        assertThat(gameRepository.existsById(gameEntity.getId()), is(false));
        assertThat(gameEventRepository.findAll(), empty());
    }

    @Test
    void testGetGameByIdNotFound() {
        UUID id = UUID.randomUUID();

        assertThrows(ResourceNotFoundException.class, () -> gameRepository.findByIdOrThrow(id));
    }
}