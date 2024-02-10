package de.unistuttgart.iste.meitrex.rulesengine.controller;

import de.unistuttgart.iste.meitrex.rulesengine.dto.EventTypeDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.SpringErrorPayload;
import de.unistuttgart.iste.meitrex.rulesengine.service.EventTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Tag(
        name = "Game Event Type",
        description = "The game event type API.")
@RequestMapping("/api/eventType")
@RequiredArgsConstructor
public class GameEventTypeController {

    private final EventTypeService gameEventTypeService;

    @Operation(
            summary = "Create a new game event type",
            description = "Register a new game event type in the system."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Game event type created successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input, e.g. the identifier is already in use.",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @PostMapping(consumes = "application/json", produces = "application/json")
    public EventTypeDto createGameEventType(
            @RequestBody @Valid EventTypeDto gameEventTypeDto) {
        return gameEventTypeService.createGameEventType(gameEventTypeDto);
    }

    @Operation(summary = "Get all event types")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @GetMapping(produces = "application/json")
    public List<EventTypeDto> getAllGameEventTypes() {
        return gameEventTypeService.getAllGameEventTypes();
    }

    @Operation(summary = "Get an event type by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Game event type not found",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @GetMapping(path = "/{id}", produces = "application/json")
    public EventTypeDto getGameEventTypeById(
            @PathVariable("id")
            @Parameter(description = "The unique identifier of the event type", example = "UNKNOWN")
            String id
    ) {
        return gameEventTypeService.getGameEventTypeById(id);
    }

    @Operation(summary = "Update an event type by its ID")
    @ApiResponse(responseCode = "200", description = "Game event type updated successfully")
    @ApiResponse(responseCode = "201", description = "Game event type created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "403", description = "Predefined event types cannot be updated",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<EventTypeDto> updateGameEventType(
            @PathVariable("id") @Parameter(description = "Identifier of the event type") String id,
            @RequestBody EventTypeDto gameEventTypeDto
    ) {
        var responseStatus = gameEventTypeService.existsGameEventType(id)
                ? HttpStatus.OK
                : HttpStatus.CREATED;

        return ResponseEntity.status(responseStatus)
                .body(gameEventTypeService.updateGameEventType(id, gameEventTypeDto));
    }

    @Operation(summary = "Delete a game event type by its ID. All game events of this type will get the UNKNOWN type.")
    @ApiResponse(
            responseCode = "204",
            description = "Game event type deleted successfully or game event type did not exist"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Predefined event types cannot be deleted",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGameEventType(
            @PathVariable("id") @Parameter(description = "Identifier of the event type") String id
    ) {
        gameEventTypeService.deleteGameEventType(id);
    }
}