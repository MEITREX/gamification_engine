package de.unistuttgart.iste.meitrex.rulesengine.controller;

import de.unistuttgart.iste.meitrex.rulesengine.config.JsonConfiguration;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.GameDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.PlayerDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException;
import de.unistuttgart.iste.meitrex.rulesengine.service.game.PlayerService;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PlayerControllerTest {

    @Mock
    private PlayerService playerService;

    @InjectMocks
    private PlayerController playerController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(playerController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(JsonConfiguration.createObjectMapper()))
                .build();
    }

    @Test
    void testAddPlayerToGame() throws Exception {
        UUID gameId = UUID.randomUUID();
        PlayerDto playerDto = PlayerDto.builder()
                .name("Test Player")
                .userId(UUID.randomUUID())
                .game(GameDto.builder().id(gameId).name("Game").build())
                .flags(Set.of())
                .additionalData(new JsonObject())
                .scores(Map.of())
                .build();

        String playerJson = """
                {
                    "name": "Test Player",
                    "userId": "123e4567-e89b-12d3-a456-426614174000"
                }
                """;

        when(playerService.createPlayer(eq(gameId), any())).thenReturn(playerDto);

        mockMvc.perform(post("/api/" + gameId + "/player")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(playerJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Test Player")))
                .andExpect(jsonPath("$.userId", is(playerDto.getUserId().toString())))
                .andExpect(jsonPath("$.game.id", is(gameId.toString())))
                .andExpect(jsonPath("$.game.name", is("Game")))
                .andExpect(jsonPath("$.flags", hasToString("[]")))
                .andExpect(jsonPath("$.additionalData", hasToString("{}")))
                .andExpect(jsonPath("$.scores", aMapWithSize(0)));

        verify(playerService, times(1)).createPlayer(eq(gameId), any());
    }

    @Test
    void testGetAllPlayers() throws Exception {
        UUID gameId = UUID.randomUUID();
        List<PlayerDto> playerDtoList = List.of(
                PlayerDto.builder()
                        .name("Test Player 1")
                        .userId(UUID.randomUUID())
                        .build(),
                PlayerDto.builder()
                        .name("Test Player 2")
                        .userId(UUID.randomUUID())
                        .build()
        );

        when(playerService.getAllPlayersOfGame(gameId)).thenReturn(playerDtoList);

        mockMvc.perform(get("/api/" + gameId + "/player")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Test Player 1")))
                .andExpect(jsonPath("$[1].name", is("Test Player 2")));

        verify(playerService, times(1)).getAllPlayersOfGame(gameId);
    }

    @Test
    void testGetPlayer() throws Exception {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();
        PlayerDto playerDto = PlayerDto.builder()
                .name("Test Player")
                .userId(UUID.randomUUID())
                .game(GameDto.builder().id(gameId).name("Game").build())
                .flags(Set.of("flag1", "flag2"))
                .additionalData(new JsonObject().put("key", "value"))
                .scores(Map.of("xp", 100, "health", 200))
                .build();

        when(playerService.getPlayer(gameId, playerId)).thenReturn(playerDto);

        mockMvc.perform(get("/api/" + gameId + "/player/" + playerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Test Player")))
                .andExpect(jsonPath("$.userId", is(playerDto.getUserId().toString())))
                .andExpect(jsonPath("$.game.id", is(gameId.toString())))
                .andExpect(jsonPath("$.game.name", is("Game")))
                .andExpect(jsonPath("$.flags", containsInAnyOrder("flag1", "flag2")))
                .andExpect(jsonPath("$.additionalData.key", is("value")))
                .andExpect(jsonPath("$.scores.xp", is(100)))
                .andExpect(jsonPath("$.scores.health", is(200)));

        verify(playerService, times(1)).getPlayer(gameId, playerId);
    }

    @Test
    void testGetPlayerNotFound() throws Exception {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        when(playerService.getPlayer(gameId, playerId))
                .thenThrow(new ResourceNotFoundException("Player", playerId));

        mockMvc.perform(get("/api/" + gameId + "/player/" + playerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(playerService, times(1)).getPlayer(gameId, playerId);
    }

    @Test
    void testDeletePlayerFromGame() throws Exception {
        UUID gameId = UUID.randomUUID();
        UUID playerId = UUID.randomUUID();

        doNothing().when(playerService).deletePlayer(gameId, playerId);

        mockMvc.perform(delete("/api/" + gameId + "/player/" + playerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(playerService, times(1)).deletePlayer(gameId, playerId);
    }
}