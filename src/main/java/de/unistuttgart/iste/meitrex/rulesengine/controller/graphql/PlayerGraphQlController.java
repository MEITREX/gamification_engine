package de.unistuttgart.iste.meitrex.rulesengine.controller.graphql;

import de.unistuttgart.iste.meitrex.rulesengine.controller.PlayerController;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class PlayerGraphQlController {

    private final PlayerController playerController;

    @MutationMapping("createPlayerInGame")
    public PlayerDto createPlayer(
            @Argument(name = "gameId")
            UUID gameId,

            @Valid @Argument(name = "player")
            CreatePlayerDto playerDto
    ) {
        return playerController.addPlayerToGame(gameId, playerDto);
    }

    @MutationMapping("deletePlayerFromGame")
    public boolean deletePlayer(
            @Argument(name = "gameId")
            UUID gameId,
            @Argument(name = "playerId")
            UUID playerId
    ) {
        playerController.deletePlayerFromGame(gameId, playerId);
        return true;
    }

    @QueryMapping("player")
    public PlayerDto getPlayer(
            @Argument(name = "gameId")
            UUID gameId,
            @Argument(name = "playerId")
            UUID playerId
    ) {
        return playerController.getPlayer(gameId, playerId);
    }

    @QueryMapping("players")
    public List<PlayerDto> getAllPlayers(
            @Argument(name = "gameId")
            UUID gameId
    ) {
        return playerController.getAllPlayers(gameId);
    }

    @SchemaMapping(typeName = "GameDto", field = "players")
    public List<PlayerDto> getPlayers(GameDto gameDto) {
        return playerController.getAllPlayers(gameDto.getId());
    }
}
