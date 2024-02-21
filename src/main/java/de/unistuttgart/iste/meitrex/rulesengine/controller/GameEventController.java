package de.unistuttgart.iste.meitrex.rulesengine.controller;

import de.unistuttgart.iste.meitrex.rulesengine.dto.event.CreateGameEventDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.event.GameEventDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.SpringErrorPayload;
import de.unistuttgart.iste.meitrex.rulesengine.service.event.GameEventService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Event", description = "Get and log game events")
public class GameEventController {

    private final GameEventService gameEventService;

    @PostMapping(value = "/game/{gameId}/event", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new game event",
            description = "Register a new game event in the system."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Game event created successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Game not found",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public GameEventDto createEvent(

            @PathVariable("gameId") @Parameter(description = "The id of the game") @NotNull
            UUID gameId,

            @RequestBody @Valid
            CreateGameEventDto gameEventDto
    ) {
        return gameEventService.createEvent(gameId, gameEventDto);
    }

    @PostMapping(value = "/game/{gameId}/player/{playerId}/event", consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new game event",
            description = "Register a new game event in the system."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Game event created successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(
            responseCode = "404",
            description = "Game or player not found",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public GameEventDto createEventForPlayer(

            @PathVariable("gameId") @Parameter(description = "The id of the game")
            UUID gameId,

            @PathVariable("playerId") @Parameter(description = "The id of the player")
            UUID playerId,

            @RequestBody @Valid
            CreateGameEventDto gameEventDto
    ) {
        return gameEventService.createEvent(gameId, gameEventDto.withPlayerId(playerId));
    }

    @GetMapping(value = "/game/{gameId}/event", produces = "application/json")
    @Operation(summary = "Get all events of the game. This is mainly for administrative purposes.")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Game not found",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public List<GameEventDto> getEventsForGame(

            @PathVariable("gameId") @Parameter(description = "The id of the game")
            UUID gameId,

            @ParameterObject
            Pageable pageable
    ) {
        return gameEventService.getEventsForGame(gameId, pageable);
    }

    @GetMapping(value = "/game/{gameId}/player/{playerId}/event", produces = "application/json")
    @Operation(summary = "Get all events of the game for a specific player")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Game or player not found",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public List<GameEventDto> getEventsForPlayer(

            @PathVariable("gameId") @Parameter(description = "The id of the game")
            UUID gameId,

            @PathVariable("playerId") @Parameter(description = "The id of the player")
            UUID playerId,

            @ParameterObject
            Pageable pageable
    ) {
        return gameEventService.getEventsForPlayer(gameId, playerId, pageable);
    }

}
