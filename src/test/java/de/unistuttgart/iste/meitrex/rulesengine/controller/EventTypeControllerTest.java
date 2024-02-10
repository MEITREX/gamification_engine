package de.unistuttgart.iste.meitrex.rulesengine.controller;

import de.unistuttgart.iste.meitrex.rulesengine.config.JsonConfiguration;
import de.unistuttgart.iste.meitrex.rulesengine.dto.EventTypeDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEventScope;
import de.unistuttgart.iste.meitrex.rulesengine.service.EventTypeService;
import io.vertx.core.json.JsonObject;
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

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EventTypeControllerTest {

    @Mock
    private EventTypeService gameEventTypeService;

    @InjectMocks
    private GameEventTypeController gameEventTypeController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(gameEventTypeController)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(JsonConfiguration.createObjectMapper()))
                .build();
    }

    /**
     * Tests the creation of a game event type with a valid request.
     */
    @Test
    void testCreateGameEventType() throws Exception {
        EventTypeDto gameEventTypeDto = EventTypeDto.builder()
                .identifier("TEST_EVENT")
                .description("Test event type")
                .eventSchema(new JsonObject("{\"type\": \"object\"}"))
                .defaultScope(GameEventScope.GAME)
                .build();
        String eventJson = """
                {
                    "identifier": "TEST_EVENT",
                    "description": "Test event type",
                    "defaultScope": "GAME",
                    "eventSchema": {
                        "type": "object"
                    }
                }
                """;

        when(gameEventTypeService.createGameEventType(Mockito.any(EventTypeDto.class)))
                .thenReturn(gameEventTypeDto);

        mockMvc.perform(post("/api/eventType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.identifier", is("TEST_EVENT")))
                .andExpect(jsonPath("$.description", is("Test event type")))
                .andExpect(jsonPath("$.defaultScope", is("GAME")))
                .andExpect(jsonPath("$.eventSchema.type", is("object")));

        verify(gameEventTypeService, times(1))
                .createGameEventType(any());
    }

    /**
     * Tests that the creation of a game event type with an invalid identifier fails.
     */
    @Test
    void testCreateGameEventTypeWithInvalidIdentifier() throws Exception {
        String eventJson = """
                {
                    "identifier": "invalid identifier",
                    "description": "Test event type",
                    "defaultScope": "GAME",
                    "eventSchema": {
                        "type": "object"
                    }
                }
                """;

        mockMvc.perform(post("/api/eventType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isBadRequest());

        verify(gameEventTypeService, never())
                .createGameEventType(any());
    }

    /**
     * Tests that the creation of a game event type with a missing identifier fails.
     */
    @Test
    void testCreateGameEventTypeWithMissingIdentifier() throws Exception {
        String eventJson = """
                {
                    "description": "Test event type",
                    "defaultScope": "GAME",
                    "eventSchema": {
                        "type": "object"
                    }
                }
                """;

        mockMvc.perform(post("/api/eventType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isBadRequest());

        verify(gameEventTypeService, never())
                .createGameEventType(any());
    }

    /**
     * Tests the creation of a game event type with minimal data.
     */
    @Test
    void testCreateGameEventTypeWithMinimalData() throws Exception {
        String eventJson = """
                {
                    "identifier": "TEST_EVENT"
                }
                """;

        EventTypeDto gameEventTypeDto = EventTypeDto.builder()
                .identifier("TEST_EVENT")
                .description("")
                .defaultScope(GameEventScope.GAME)
                .eventSchema(new JsonObject())
                .build();

        when(gameEventTypeService.createGameEventType(Mockito.any(EventTypeDto.class)))
                .thenReturn(gameEventTypeDto);

        mockMvc.perform(post("/api/eventType")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(eventJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.identifier", is("TEST_EVENT")))
                .andExpect(jsonPath("$.description", is("")))
                .andExpect(jsonPath("$.defaultScope", is("GAME")))
                .andExpect(jsonPath("$.eventSchema", hasToString("{}")));

        verify(gameEventTypeService, times(1))
                .createGameEventType(any());
    }

    /**
     * Tests the retrieval of all game event types.
     */
    @Test
    void testGetAllGameEventTypes() throws Exception {
        List<EventTypeDto> gameEventTypeDtoList = List.of(
                EventTypeDto.builder()
                        .identifier("TEST_EVENT_A")
                        .description("Test event type 1")
                        .eventSchema(new JsonObject("{\"type\": \"object\"}"))
                        .defaultScope(GameEventScope.GAME)
                        .build(),
                EventTypeDto.builder()
                        .identifier("TEST_EVENT_B")
                        .description("Test event type 2")
                        .eventSchema(new JsonObject("{\"type\": \"object\"}"))
                        .defaultScope(GameEventScope.GAME)
                        .build()
        );

        when(gameEventTypeService.getAllGameEventTypes()).thenReturn(gameEventTypeDtoList);

        mockMvc.perform(get("/api/eventType")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].identifier", is("TEST_EVENT_A")))
                .andExpect(jsonPath("$[0].description", is("Test event type 1")))
                .andExpect(jsonPath("$[0].defaultScope", is("GAME")))
                .andExpect(jsonPath("$[0].eventSchema.type", is("object")))
                .andExpect(jsonPath("$[1].identifier", is("TEST_EVENT_B")))
                .andExpect(jsonPath("$[1].description", is("Test event type 2")))
                .andExpect(jsonPath("$[1].defaultScope", is("GAME")))
                .andExpect(jsonPath("$[1].eventSchema.type", is("object")));

        verify(gameEventTypeService, times(1)).getAllGameEventTypes();
    }

    @Test
    void testGetGameEventTypeById() throws Exception {
        // Arrange
        String id = "TEST_EVENT";
        EventTypeDto gameEventTypeDto = EventTypeDto.builder()
                .identifier(id)
                .description("Test event type 1")
                .eventSchema(new JsonObject("{\"type\": \"object\"}"))
                .defaultScope(GameEventScope.GAME)
                .build();

        when(gameEventTypeService.getGameEventTypeById(id)).thenReturn(gameEventTypeDto);

        // Act and Assert
        mockMvc.perform(get("/api/eventType/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.identifier", is(id)))
                .andExpect(jsonPath("$.description", is("Test event type 1")))
                .andExpect(jsonPath("$.defaultScope", is("GAME")))
                .andExpect(jsonPath("$.eventSchema.type", is("object")));

        verify(gameEventTypeService, times(1)).getGameEventTypeById(id);
    }

    @Test
    void testGetGameEventTypeByIdNotFound() throws Exception {
        String id = "INVALID_ID";
        when(gameEventTypeService.getGameEventTypeById(id))
                .thenThrow(new ResourceNotFoundException("Event Type", id));

        mockMvc.perform(get("/api/eventType/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(gameEventTypeService, times(1)).getGameEventTypeById(id);
    }

    @Test
    void testUpdateGameEventType() throws Exception {
        // Arrange
        String id = "TEST_EVENT";
        EventTypeDto gameEventTypeDto = EventTypeDto.builder()
                .identifier(id)
                .description("Updated test event type")
                .eventSchema(new JsonObject("{\"type\": \"object\"}"))
                .defaultScope(GameEventScope.GAME)
                .build();
        String json = """
                {
                    "description": "Updated test event type",
                    "defaultScope": "GAME",
                    "eventSchema": {
                        "type": "object"
                    }
                }
                """;

        when(gameEventTypeService.existsGameEventType(id)).thenReturn(true);
        when(gameEventTypeService.updateGameEventType(eq(id), any())).thenReturn(gameEventTypeDto);

        // Act and Assert
        mockMvc.perform(put("/api/eventType/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.identifier", is(id)))
                .andExpect(jsonPath("$.description", is("Updated test event type")))
                .andExpect(jsonPath("$.defaultScope", is("GAME")))
                .andExpect(jsonPath("$.eventSchema.type", is("object")));

        verify(gameEventTypeService, times(1))
                .existsGameEventType(id);
        verify(gameEventTypeService, times(1))
                .updateGameEventType(eq(id), any());
        verify(gameEventTypeService, never())
                .createGameEventType(any());
    }

    @Test
    void testUpdateGameEventTypeNotExistingYet() throws Exception {
        // Arrange
        String id = "TEST_EVENT";
        EventTypeDto gameEventTypeDto = EventTypeDto.builder()
                .identifier(id)
                .description("Updated test event type")
                .eventSchema(new JsonObject("{\"type\": \"object\"}"))
                .defaultScope(GameEventScope.GAME)
                .build();
        String json = """
                {
                    "description": "Updated test event type",
                    "defaultScope": "GAME",
                    "eventSchema": {
                        "type": "object"
                    }
                }
                """;

        when(gameEventTypeService.existsGameEventType(id)).thenReturn(false);
        when(gameEventTypeService.updateGameEventType(eq(id), any())).thenReturn(gameEventTypeDto);

        // Act and Assert
        mockMvc.perform(put("/api/eventType/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.identifier", is(id)))
                .andExpect(jsonPath("$.description", is("Updated test event type")))
                .andExpect(jsonPath("$.defaultScope", is("GAME")))
                .andExpect(jsonPath("$.eventSchema.type", is("object")));

        verify(gameEventTypeService, times(1))
                .existsGameEventType(id);
        verify(gameEventTypeService, times(1))
                .updateGameEventType(eq(id), any());
    }

    /**
     * Tests that a potentially invalid identifier in the request body
     * is ignored when updating a game event type.
     */
    @Test
    void testUpdateGameEventTypeWithNewInvalidIdentifier() throws Exception {
        // Arrange
        String id = "TEST_EVENT";
        EventTypeDto gameEventTypeDto = EventTypeDto.builder()
                .identifier(id)
                .description("Updated test event type")
                .eventSchema(new JsonObject("{\"type\": \"object\"}"))
                .defaultScope(GameEventScope.GAME)
                .build();
        String json = """
                {
                    "identifier": "NEW_EVENT3456&&asd",
                    "description": "Updated test event type",
                    "defaultScope": "GAME",
                    "eventSchema": {
                        "type": "object"
                    }
                }
                """;

        when(gameEventTypeService.existsGameEventType(id)).thenReturn(false);
        when(gameEventTypeService.updateGameEventType(eq(id), any())).thenReturn(gameEventTypeDto);

        // Act and Assert
        mockMvc.perform(put("/api/eventType/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.identifier", is("TEST_EVENT")))
                .andExpect(jsonPath("$.description", is("Updated test event type")))
                .andExpect(jsonPath("$.defaultScope", is("GAME")))
                .andExpect(jsonPath("$.eventSchema.type", is("object")));

        verify(gameEventTypeService, times(1))
                .existsGameEventType(id);
        verify(gameEventTypeService, times(1))
                .updateGameEventType(eq(id), any());
    }

    @Test
    void testDeleteGameEventType() throws Exception {
        // Arrange
        String id = "TEST_EVENT";

        doNothing().when(gameEventTypeService).deleteGameEventType(id);

        // Act and Assert
        mockMvc.perform(delete("/api/eventType/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(gameEventTypeService, times(1)).deleteGameEventType(id);
    }
}