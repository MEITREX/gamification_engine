package de.unistuttgart.iste.meitrex.rulesengine.controller;

import de.unistuttgart.iste.meitrex.rulesengine.dto.game.CreateOrUpdateGameDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.GameDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.SpringErrorPayload;
import de.unistuttgart.iste.meitrex.rulesengine.service.game.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name = "Game",
        description = "Manage games in the system. A game is a collection of players and game rules.")
@RequestMapping("/api/game")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;

    @PostMapping(consumes = "application/json", produces = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Create a new game",
            description = "Register a new game in the system."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Game created successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input, e.g. the identifier is already in use.",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public GameDto createGame(
            @RequestBody @Valid
            CreateOrUpdateGameDto gameDto
    ) {
        return gameService.createGame(gameDto);
    }

    @GetMapping(produces = "application/json")
    @Operation(summary = "Get all games")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public List<GameDto> getAllGames(
            @Parameter(description = "Optional filter for games of a specific user")
            @RequestParam(name = "userId", required = false)
            @Nullable
            UUID userId
    ) {
        return gameService.getAllGames(userId);
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    @Operation(summary = "Get a game by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Game not found",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public GameDto getGameById(
            @PathVariable("id")
            @Parameter(description = "The unique identifier of the game", example = "123e4567-e89b-12d3-a456-426614174000")
            UUID id
    ) {
        return gameService.getGameById(id);
    }

    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    @Operation(summary = "Update a game by its ID")
    @ApiResponse(responseCode = "200", description = "Game updated successfully")
    @ApiResponse(responseCode = "201", description = "Game created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid input")
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public ResponseEntity<GameDto> updateGame(

            @PathVariable("id") @Parameter(description = "Identifier of the game")
            UUID id,

            @RequestBody @Valid
            CreateOrUpdateGameDto gameDto
    ) {
        HttpStatus responseStatus = gameService.existsGame(id) ? HttpStatus.OK : HttpStatus.CREATED;

        return ResponseEntity.status(responseStatus).body(gameService.updateOrCreateGame(id, gameDto));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a game by its ID.")
    @ApiResponse(
            responseCode = "204",
            description = "Game deleted successfully or game did not exist"
    )
    @ApiResponse(
            responseCode = "500",
            description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    public void deleteGame(@PathVariable("id") UUID id) {
        gameService.deleteGame(id);
    }
}