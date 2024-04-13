package de.unistuttgart.iste.meitrex.rulesengine.controller.graphql;

import de.unistuttgart.iste.meitrex.rulesengine.controller.GameController;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.CreateOrUpdateGameDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.GameDto;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class GameGraphQlController {

    private final GameController gameController;

    @MutationMapping("createGame")
    public GameDto createGame(
            @Valid @Argument(name = "game")
            CreateOrUpdateGameDto gameDto
    ) {
        return gameController.createGame(gameDto);
    }

    @MutationMapping("updateGame")
    public GameDto updateGame(
            @Argument(name = "gameId")
            UUID gameId,

            @Valid @Argument(name = "game")
            CreateOrUpdateGameDto gameDto
    ) {
        return gameController.updateGame(gameId, gameDto).getBody();
    }

    @MutationMapping("deleteGame")
    public boolean deleteGame(
            @Argument(name = "gameId")
            UUID gameId
    ) {
        gameController.deleteGame(gameId);
        return true;
    }

    @QueryMapping("game")
    public GameDto getGame(
            @Argument(name = "gameId")
            UUID gameId
    ) {
        return gameController.getGameById(gameId);
    }

    @QueryMapping("games")
    public List<GameDto> getAllGames(
            @Argument(name = "userId")
            @Nullable
            UUID userId
    ) {
        return gameController.getAllGames(userId);
    }
}
