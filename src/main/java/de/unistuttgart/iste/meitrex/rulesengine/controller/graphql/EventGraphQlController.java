package de.unistuttgart.iste.meitrex.rulesengine.controller.graphql;

import de.unistuttgart.iste.meitrex.rulesengine.controller.GameEventController;
import de.unistuttgart.iste.meitrex.rulesengine.dto.event.CreateGameEventDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.event.GameEventDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class EventGraphQlController {

    private final GameEventController gameEventController;

    @MutationMapping("createEvent")
    public GameEventDto createEvent(
            @Argument(name = "gameId")
            UUID gameId,

            @Valid @Argument(name = "event")
            CreateGameEventDto eventDto
    ) {
        return gameEventController.createEvent(gameId, eventDto);
    }

    @QueryMapping("eventsForGame")
    public List<GameEventDto> getEventsForGame(
            @Argument(name = "gameId")
            UUID gameId,
            @Argument(name = "pagination")
            Pageable pageable
    ) {
        return gameEventController.getEventsForGame(gameId, pageable);
    }

    @QueryMapping("eventsForPlayer")
    public List<GameEventDto> getEventsForPlayer(
            @Argument(name = "gameId")
            UUID gameId,
            @Argument(name = "playerId")
            UUID playerId,
            @Argument(name = "pagination")
            Pageable pageable
    ) {
        return gameEventController.getEventsForPlayer(gameId, playerId, pageable);
    }

}
