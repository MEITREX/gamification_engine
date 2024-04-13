package de.unistuttgart.iste.meitrex.rulesengine.service.event;

import de.unistuttgart.iste.meitrex.rulesengine.dto.event.CreateGameEventDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.event.GameEventDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.EventType;
import de.unistuttgart.iste.meitrex.rulesengine.model.game.Game;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.*;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.*;
import de.unistuttgart.iste.meitrex.rulesengine.service.rule.RuleEngine;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.*;

import static de.unistuttgart.iste.meitrex.rulesengine.service.event.EventTypeService.eventTypeNotFound;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameEventService {

    private final GameEventRepository gameEventRepository;
    private final EventTypeRepository eventTypeRepository;
    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;
    private final RuleEngine ruleEngine;


    public GameEventDto createEvent(UUID gameId, CreateGameEventDto gameEventDto) {
        String eventTypeId = gameEventDto.getEventType();
        Game game = gameRepository.findByIdOrThrow(gameId);
        Optional<PlayerEntity> player = Optional.ofNullable(gameEventDto.getPlayerId())
                .flatMap(playerId -> playerRepository.findById(new PlayerId(gameId, playerId)));
        Optional<EventTypeEntity> dbEventType = eventTypeRepository.findById(eventTypeId);

        EventType eventType = PredefinedEventTypes.findById(eventTypeId)
                .or(() -> dbEventType)
                .orElseThrow(() -> eventTypeNotFound(eventTypeId));

        GameEventEntity entity = buildEntity(gameId, gameEventDto, eventType);

        if (dbEventType.isPresent()) {
            entity.setDbEventType(dbEventType.get());
        }

        boolean hasParent = gameEventDto.getOptionalParentEventId().isPresent();
        if (hasParent) {
            setupLinkToParent(entity, gameEventDto.getParentEventId());
        }

        entity = gameEventRepository.save(entity);

        List<CreateGameEventDto> newEvents = ruleEngine.runRulesForEvent(entity, game, player.orElse(null));
        newEvents.forEach(newEvent -> createEvent(gameId, newEvent));

        return GameEventDto.from(entity);
    }

    public List<GameEventDto> getEventsForPlayer(UUID gameId, UUID userId, Pageable pageable) {
        gameRepository.findByIdOrThrow(gameId);
        playerRepository.findByIdOrThrow(new PlayerId(gameId, userId));

        return gameEventRepository.findAllForPlayer(gameId, userId, pageable)
                .map(GameEventDto::from)
                .toList();
    }

    public List<GameEventDto> getEventsForGame(UUID gameId, Pageable pageable) {
        gameRepository.findByIdOrThrow(gameId);

        return gameEventRepository.findAllForGame(gameId, pageable)
                .map(GameEventDto::from)
                .toList();
    }

    private ResourceNotFoundException eventNotFound(UUID id) {
        return new ResourceNotFoundException("Event", id);
    }

    private GameEventEntity buildEntity(UUID gameId, CreateGameEventDto gameEventDto, EventType eventType) {
        OffsetDateTime timestamp = OffsetDateTime.now();

        return GameEventEntity.builder()
                .id(UUID.randomUUID())
                .timestamp(timestamp)
                .mostRecentChildTimestamp(timestamp)
                .eventTypeIdentifier(eventType.getIdentifier())
                .visibility(gameEventDto.getOptionalVisibility().orElse(eventType.getDefaultVisibility()))
                .playerId(gameEventDto.getPlayerId())
                .data(gameEventDto.getData())
                .gameId(gameId)
                .build();
    }

    private void setupLinkToParent(GameEventEntity entity, UUID parentId) {
        GameEventEntity parent = gameEventRepository.findById(parentId).orElseThrow(() -> eventNotFound(parentId));
        parent.getChildEvents().add(entity);
        parent.setMostRecentChildTimestamp(entity.getTimestamp());
        //parent = gameEventRepository.save(parent);

        if (parent.getParentEvent() != null) {
            setupLinkToParent(parent, parent.getParentEvent().getId());
        }

        entity.setParentEvent(parent);
    }
}
