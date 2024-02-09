package de.unistuttgart.iste.meitrex.rulesengine.controller;

import de.unistuttgart.iste.meitrex.rulesengine.config.JsonConfiguration;
import de.unistuttgart.iste.meitrex.rulesengine.dto.CreateOrUpdateGameDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.GameDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException;
import de.unistuttgart.iste.meitrex.rulesengine.service.GameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

    @Mock
    private GameService gameService;

    @InjectMocks
    private GameController gameController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameController)
                // Add the custom JSON serializer and deserializer to the message converters
                .setMessageConverters(new MappingJackson2HttpMessageConverter(JsonConfiguration.createObjectMapper()))
                .build();
    }

    @Test
    void testCreateGame() throws Exception {
        GameDto gameDto = GameDto.builder()
                .id(UUID.randomUUID())
                .name("Test Game")
                .build();
        String gameJson = """
                {
                    "name": "Test Game"
                }
                """;

        when(gameService.createGame(Mockito.any(CreateOrUpdateGameDto.class)))
                .thenReturn(gameDto);

        mockMvc.perform(post("/api/game")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Test Game")));

        verify(gameService, times(1))
                .createGame(any());
    }

    @Test
    void testGetAllGames() throws Exception {
        List<GameDto> gameDtoList = List.of(
                GameDto.builder()
                        .id(UUID.randomUUID())
                        .name("Test Game 1")
                        .build(),
                GameDto.builder()
                        .id(UUID.randomUUID())
                        .name("Test Game 2")
                        .build()
        );

        when(gameService.getAllGames()).thenReturn(gameDtoList);

        mockMvc.perform(get("/api/game")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Test Game 1")))
                .andExpect(jsonPath("$[0].id", is(gameDtoList.getFirst().getId().toString())))
                .andExpect(jsonPath("$[1].name", is("Test Game 2")))
                .andExpect(jsonPath("$[1].id", is(gameDtoList.getLast().getId().toString())));

        verify(gameService, times(1)).getAllGames();
    }

    @Test
    void testGetGameById() throws Exception {
        UUID id = UUID.randomUUID();
        GameDto gameDto = GameDto.builder()
                .id(id)
                .name("Test Game")
                .build();

        when(gameService.getGameById(id)).thenReturn(gameDto);

        mockMvc.perform(get("/api/game/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Test Game")));

        verify(gameService, times(1)).getGameById(id);
    }

    @Test
    void testGetGameByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        when(gameService.getGameById(id)).thenThrow(new ResourceNotFoundException("Game", id));

        mockMvc.perform(get("/api/game/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(gameService, times(1)).getGameById(id);
    }

    @Test
    void testUpdateGame() throws Exception {
        UUID id = UUID.randomUUID();
        GameDto gameDto = GameDto.builder()
                .id(id)
                .name("Updated Test Game")
                .build();
        String gameJson = """
                {
                    "name": "Updated Test Game"
                }
                """;

        when(gameService.existsGame(id)).thenReturn(true);
        when(gameService.updateGame(eq(id), any())).thenReturn(gameDto);

        mockMvc.perform(put("/api/game/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Updated Test Game")));

        verify(gameService, times(1))
                .existsGame(id);
        verify(gameService, times(1))
                .updateGame(eq(id), any());
    }


    @Test
    void testUpdateGameNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        String gameJson = """
                {
                    "name": "Updated Test Game"
                }
                """;

        when(gameService.existsGame(id)).thenReturn(false);
        when(gameService.createGame(any())).thenReturn(GameDto.builder()
                .id(id)
                .name("Updated Test Game")
                .build());

        mockMvc.perform(put("/api/game/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gameJson))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is("Updated Test Game")));

        verify(gameService, times(1))
                .existsGame(id);
        verify(gameService, times(1))
                .createGame(any());
    }

    @Test
    void testDeleteGame() throws Exception {
        UUID id = UUID.randomUUID();

        doNothing().when(gameService).deleteGame(id);

        mockMvc.perform(delete("/api/game/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(gameService, times(1)).deleteGame(id);
    }
}