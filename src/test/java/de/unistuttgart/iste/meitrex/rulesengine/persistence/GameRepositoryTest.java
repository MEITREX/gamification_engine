package de.unistuttgart.iste.meitrex.rulesengine.persistence;

import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.*;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.GameRepository;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.PlayerRepository;
import de.unistuttgart.iste.meitrex.util.MeitrexPostgresSqlContainer;
import io.vertx.core.json.JsonObject;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

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

    @Test
    @Transactional
    @Commit
    void testSave() {
        GameEntity gameEntity = GameEntity.builder()
                .name("Test Game")
                .flags(Set.of("flag1", "flag2"))
                .scores(Map.of("score1", 1, "score2", 2))
                .additionalData(JsonObject.of("key", "value", "key2", "value2"))
                .build();

        gameRepository.save(gameEntity);

        GameEntity createdGameEntity = gameRepository.findById(gameEntity.getId()).orElseThrow();

        assertThat(createdGameEntity.getId(), is(gameEntity.getId()));
        assertThat(createdGameEntity.getName(), is("Test Game"));
        assertThat(createdGameEntity.getFlags(), containsInAnyOrder("flag1", "flag2"));
        assertThat(createdGameEntity.getScores(), allOf(
                hasEntry("score1", 1),
                hasEntry("score2", 2)
        ));
        assertThat(createdGameEntity.getAdditionalData().encode(), is("{\"key\":\"value\",\"key2\":\"value2\"}"));
    }

    @Test
    @Commit
    void testDelete() {
        GameEntity gameEntity = GameEntity.builder()
                .name("Test Game")
                .flags(Set.of("flag1", "flag2"))
                .scores(Map.of("score1", 1, "score2", 2))
                .additionalData(JsonObject.of("key", "value", "key2", "value2"))
                .build();

        gameRepository.save(gameEntity);
        gameRepository.deleteById(gameEntity.getId());

        assertFalse(gameRepository.existsById(gameEntity.getId()));
    }

    @Test
    @Commit
    void testDeleteWithPlayers() {
        GameEntity gameEntity = GameEntity.builder()
                .name("Test Game")
                .flags(Set.of("flag1", "flag2"))
                .scores(Map.of("score1", 1, "score2", 2))
                .additionalData(JsonObject.of("key", "value", "key2", "value2"))
                .build();

        gameRepository.save(gameEntity);

        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(new PlayerId(UUID.randomUUID(), gameEntity.getId()))
                .game(gameEntity)
                .name("Test Player")
                .flags(Set.of("flag1", "flag2"))
                .scores(Map.of("score1", 1, "score2", 2))
                .additionalData(JsonObject.of("key", "value", "key2", "value2"))
                .build();

        playerRepository.save(playerEntity);

        gameRepository.deleteById(gameEntity.getId());

        assertThat(playerRepository.findAll(), empty());
        assertThat(gameRepository.findAll(), empty());
    }
}