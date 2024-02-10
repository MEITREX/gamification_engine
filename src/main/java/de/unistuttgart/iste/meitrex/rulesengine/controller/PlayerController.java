package de.unistuttgart.iste.meitrex.rulesengine.controller;

import de.unistuttgart.iste.meitrex.rulesengine.dto.game.CreatePlayerDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.PlayerDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.SpringErrorPayload;
import de.unistuttgart.iste.meitrex.rulesengine.service.game.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@Tag(name = "Player", description = "Manage the players of a game.")
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    @Operation(
            summary = "Add a new player to a game",
            description = "This operation is used to let a player join a game. " +
                          "The player is identified by its user ID and can have a name. " +
                          "Players can be added to multiple games at any time."
    )
    @ApiResponse(
            responseCode = "201",
            description = "Player added successfully"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid input, e.g. the identifier is already in use.",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class))
    )
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/api/{gameId}/player", consumes = "application/json", produces = "application/json")
    public PlayerDto addPlayerToGame(
            @PathVariable("gameId") @Parameter(description = "id of the game") UUID gameId,
            @RequestBody CreatePlayerDto playerDto) {
        return playerService.createPlayer(gameId, playerDto);
    }

    @Operation(summary = "Get all players of a game")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @GetMapping(value = "/api/{gameId}/player", produces = "application/json")
    public List<PlayerDto> getAllPlayers(
            @PathVariable("gameId") @Parameter(description = "id of the game") UUID gameId) {
        return playerService.getAllPlayersOfGame(gameId);
    }

    @Operation(summary = "Get a player by its ID")
    @ApiResponse(responseCode = "200", description = "Successful operation")
    @ApiResponse(responseCode = "404", description = "Player not found",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @GetMapping(value = "/api/{gameId}/player/{playerId}", produces = "application/json")
    public PlayerDto getPlayer(
            @PathVariable("gameId") @Parameter(description = "id of the game") UUID gameId,
            @PathVariable("playerId") @Parameter(description = "id of the player") UUID playerId) {
        return playerService.getPlayer(gameId, playerId);
    }

    @Operation(
            summary = "Delete a player from a game",
            description = "This operation is used to remove a player from a game. " +
                          "The player is identified by its user ID."
    )
    @ApiResponse(responseCode = "204", description = "Player deleted successfully")
    @ApiResponse(responseCode = "500", description = "Server error",
            content = @Content(schema = @Schema(implementation = SpringErrorPayload.class)))
    @DeleteMapping("/api/{gameId}/player/{playerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePlayerFromGame(
            @PathVariable("gameId") @Parameter(description = "id of the game") UUID gameId,
            @PathVariable("playerId") @Parameter(description = "id of the player") UUID playerId) {
        playerService.deletePlayer(gameId, playerId);
    }

}
