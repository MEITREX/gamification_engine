package de.unistuttgart.iste.meitrex.rulesengine.persistence;

import de.unistuttgart.iste.meitrex.common.testutil.MeitrexPostgresSqlContainer;
import de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.*;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.GameRepository;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.UUID;

import static de.unistuttgart.iste.meitrex.rulesengine.matcher.PlayerMatcher.samePlayerAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Testcontainers
@ExtendWith(MeitrexPostgresSqlContainer.class)
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GameRepository gameRepository;

    @Test
    @Commit
    @Transactional
    void testSaveAndDelete() {
        GameEntity gameEntity = createGameEntity();
        UUID gameId = gameEntity.getId();
        PlayerId playerId = new PlayerId(gameId, UUID.randomUUID());
        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(playerId)
                .name("Test Player")
                .game(gameEntity)
                .build();

        playerRepository.save(playerEntity);

        PlayerEntity createdPlayerEntity = playerRepository.findById(playerEntity.getId()).orElseThrow();

        assertThat(createdPlayerEntity, is(samePlayerAs(playerEntity)));

        playerRepository.deleteById(playerEntity.getId());

        assertThat(playerRepository.existsById(playerEntity.getId()), is(false));
    }

    @Test
    @Commit
    void testFindAllByGameId() {
        GameEntity game1 = createGameEntity();
        GameEntity game2 = createGameEntity();
        UUID gameId1 = game1.getId();
        UUID gameId2 = game2.getId();
        PlayerId playerId1 = new PlayerId(gameId1, UUID.randomUUID());
        PlayerId playerId2 = new PlayerId(gameId1, UUID.randomUUID());
        PlayerId playerIdOtherGame = new PlayerId(gameId2, UUID.randomUUID());
        PlayerEntity playerEntity1 = PlayerEntity.builder()
                .id(playerId1)
                .name("Test Player 1")
                .game(game1)
                .build();
        PlayerEntity playerEntity2 = PlayerEntity.builder()
                .id(playerId2)
                .name("Test Player 2")
                .game(game1)
                .build();
        PlayerEntity playerEntityOtherGame = PlayerEntity.builder()
                .id(playerIdOtherGame)
                .name("Test Player 3")
                .game(game2)
                .build();

        playerRepository.saveAll(List.of(playerEntity1, playerEntity2, playerEntityOtherGame));

        var players = playerRepository.findAllByIdGameId(gameId1);

        assertThat(players, containsInAnyOrder(playerEntity1, playerEntity2));
    }

    @Test
    void testGetPlayerNotFound() {
        PlayerId playerId = new PlayerId(UUID.randomUUID(), UUID.randomUUID());

        assertThrows(ResourceNotFoundException.class, () -> playerRepository.findByIdOrThrow(playerId));
    }

    private GameEntity createGameEntity() {
        UUID gameId = UUID.randomUUID();
        GameEntity gameEntity = GameEntity.builder()
                .id(gameId)
                .name("Test Game")
                .build();
        gameRepository.save(gameEntity);
        return gameEntity;
    }
}