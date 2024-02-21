package de.unistuttgart.iste.meitrex.rulesengine.service;

import de.unistuttgart.iste.meitrex.rulesengine.dto.game.CreateOrUpdateGameDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.GameDto;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.GameEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.GameRepository;
import de.unistuttgart.iste.meitrex.rulesengine.service.game.GameService;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static de.unistuttgart.iste.meitrex.rulesengine.matcher.GameMatcher.sameGameAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @InjectMocks
    private GameService gameService;

    @Test
    void testCreateGame() {
        CreateOrUpdateGameDto inputDto = new CreateOrUpdateGameDto("Test Game");

        GameEntity gameEntity = new GameEntity().setName(inputDto.getName());
        when(gameRepository.save(any())).thenReturn(gameEntity);

        GameDto result = gameService.createGame(inputDto);

        assertThat(result.getName(), is(equalTo(inputDto.getName())));
        assertThat(result.getId(), is(notNullValue()));
        assertThat(result.getFlags(), is(empty()));
        assertThat(result.getScores(), is(anEmptyMap()));
        assertThat(result.getAdditionalData(), hasToString("{}"));

        verify(gameRepository, times(1)).save(any());
    }

    @Test
    void testGetAllGames() {
        var gameEntityBuilder = GameEntity.builder()
                .flags(Set.of("flag1", "flag2"))
                .scores(Map.of("score1", 1, "score2", 2))
                .additionalData(JsonObject.of("key", "value", "key2", "value2"));
        GameEntity gameEntity = gameEntityBuilder.name("Test Game").build();
        GameEntity gameEntity2 = gameEntityBuilder.name("Test Game 2").build();

        when(gameRepository.findAll()).thenReturn(List.of(gameEntity, gameEntity2));

        List<GameDto> actual = gameService.getAllGames();

        assertThat(actual, containsInAnyOrder(sameGameAs(gameEntity), sameGameAs(gameEntity2)));

        verify(gameRepository, times(1)).findAll();
    }

    @Test
    void testGetGameById() {
        UUID id = UUID.randomUUID();
        GameEntity gameEntity = GameEntity.builder().id(id).name("Test Game").build();
        when(gameRepository.findByIdOrThrow(id)).thenReturn(gameEntity);

        GameDto result = gameService.getGameById(id);

        assertThat(result, sameGameAs(gameEntity));

        verify(gameRepository, times(1)).findByIdOrThrow(id);
    }

    @Test
    void testExistsGame() {
        UUID id = UUID.randomUUID();

        when(gameRepository.existsById(id)).thenReturn(true);
        assertTrue(gameService.existsGame(id));
        verify(gameRepository, times(1)).existsById(id);

        when(gameRepository.existsById(id)).thenReturn(false);
        assertFalse(gameService.existsGame(id));
        verify(gameRepository, times(2)).existsById(id);
    }

    @Test
    void testUpdateOrCreateGame() {
        UUID id = UUID.randomUUID();
        CreateOrUpdateGameDto input = new CreateOrUpdateGameDto("Updated Game");

        GameEntity gameEntity = new GameEntity().setId(id).setName("Old Game");
        when(gameRepository.findById(id)).thenReturn(Optional.of(gameEntity));
        when(gameRepository.save(any())).thenAnswer(returnsFirstArg());

        GameDto result = gameService.updateOrCreateGame(id, input);

        assertThat(result.getName(), is(equalTo(input.getName())));

        verify(gameRepository, times(1)).findById(id);
        verify(gameRepository, times(1)).save(any());
    }

    @Test
    void testUpdateOrCreateGameCreate() {
        UUID id = UUID.randomUUID();
        CreateOrUpdateGameDto input = new CreateOrUpdateGameDto("New Game");

        when(gameRepository.findById(id)).thenReturn(Optional.empty());
        when(gameRepository.save(any())).thenAnswer(returnsFirstArg());

        GameDto result = gameService.updateOrCreateGame(id, input);

        assertThat(result.getName(), is(equalTo(input.getName())));
        assertThat(result.getId(), is(equalTo(id)));
        assertThat(result.getFlags(), is(empty()));
        assertThat(result.getScores(), is(anEmptyMap()));
        assertThat(result.getAdditionalData(), hasToString("{}"));
    }

    @Test
    void testDeleteGame() {
        UUID id = UUID.randomUUID();
        doNothing().when(gameRepository).deleteById(id);

        gameService.deleteGame(id);

        verify(gameRepository, times(1)).deleteById(id);
    }
}