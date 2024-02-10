package de.unistuttgart.iste.meitrex.rulesengine.service;

import de.unistuttgart.iste.meitrex.rulesengine.dto.game.CreatePlayerDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.PlayerDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.*;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.PlayerRepository;
import de.unistuttgart.iste.meitrex.rulesengine.service.game.PlayerService;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static de.unistuttgart.iste.meitrex.rulesengine.matcher.PlayerMatcher.samePlayerAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @InjectMocks
    private PlayerService playerService;

    @Test
    void testCreatePlayer() {
        UUID gameId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CreatePlayerDto inputDto = new CreatePlayerDto("Test User", userId);

        PlayerId playerId = new PlayerId(gameId, userId);
        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(playerId)
                .name(inputDto.getName())
                .game(GameEntity.builder().id(gameId).build())
                .build();
        when(playerRepository.save(any())).thenReturn(playerEntity);

        PlayerDto result = playerService.createPlayer(gameId, inputDto);

        assertThat(result, is(samePlayerAs(playerEntity)));

        verify(playerRepository, times(1)).save(any());
    }

    @Test
    void testGetAllPlayersOfGame() {
        UUID gameId = UUID.randomUUID();
        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(new PlayerId(gameId, UUID.randomUUID()))
                .name("Test Player 1")
                .game(GameEntity.builder().id(gameId).name("Test").build())
                .build();
        PlayerEntity playerEntity2 = PlayerEntity.builder()
                .id(new PlayerId(gameId, UUID.randomUUID()))
                .name("Test Player 2")
                .game(GameEntity.builder().id(gameId).name("Test").build())
                .build();

        when(playerRepository.findAllByIdGameId(gameId))
                .thenReturn(List.of(playerEntity, playerEntity2));

        List<PlayerDto> actual = playerService.getAllPlayersOfGame(gameId);

        assertThat(actual, containsInAnyOrder(samePlayerAs(playerEntity), samePlayerAs(playerEntity2)));

        verify(playerRepository, times(1)).findAllByIdGameId(gameId);
    }

    @Test
    void testGetPlayer() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(new PlayerId(gameId, playerId))
                .name("Test Player")
                .game(GameEntity.builder().id(gameId).name("Test").build())
                .flags(Set.of("flag1", "flag2"))
                .additionalData(JsonObject.of("key", "value"))
                .scores(Map.of("xp", 100, "health", 200))
                .build();
        when(playerRepository.findById(new PlayerId(gameId, playerId)))
                .thenReturn(Optional.of(playerEntity));

        PlayerDto result = playerService.getPlayer(gameId, playerId);

        assertThat(result, is(samePlayerAs(playerEntity)));

        verify(playerRepository, times(1))
                .findById(new PlayerId(gameId, playerId));
    }

    @Test
    void testGetPlayerNotFound() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        when(playerRepository.findById(new PlayerId(gameId, playerId)))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> playerService.getPlayer(gameId, playerId));

        verify(playerRepository, times(1))
                .findById(new PlayerId(gameId, playerId));
    }

    @Test
    void testDeletePlayer() {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        doNothing().when(playerRepository).deleteById(new PlayerId(gameId, playerId));

        playerService.deletePlayer(gameId, playerId);

        verify(playerRepository, times(1))
                .deleteById(new PlayerId(gameId, playerId));
    }
}
