package de.unistuttgart.iste.meitrex.rulesengine.service.event;

import de.unistuttgart.iste.meitrex.rulesengine.dto.event.EventTypeDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.*;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.EventType;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.EventTypeEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.EventTypeRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class EventTypeService {

    private static final String CLASS_NAME = "GameEventType";
    private final EventTypeRepository gameEventTypeRepository;

    /**
     * Creates a new game event type.
     *
     * @param gameEventTypeDto the game event type to create
     * @return the created game event type
     * @throws ResourceAlreadyExistsException if the game event type already exists
     */
    public EventTypeDto createEventType(EventTypeDto gameEventTypeDto) {
        findEventTypeById(gameEventTypeDto.getIdentifier())
                .ifPresent(entity -> {
                    throw new ResourceAlreadyExistsException(CLASS_NAME, gameEventTypeDto.getIdentifier());
                });

        EventTypeEntity entity = EventTypeEntity.from(gameEventTypeDto);

        entity = gameEventTypeRepository.save(entity);

        return EventTypeDto.from(entity);
    }

    /**
     * Returns all game event types.
     *
     * @return all game event types, including predefined ones
     */
    public List<EventTypeDto> getAllGameEventTypes() {
        return Stream.concat(gameEventTypeRepository.findAll().stream(),
                        PredefinedEventTypes.getAll().stream())
                .map(EventTypeDto::from)
                .toList();
    }

    /**
     * Returns a game event type by its ID.
     *
     * @param id the identifier of the game event type
     * @return the game event type
     * @throws ResourceNotFoundException if the game event type does not exist
     */
    public EventTypeDto getGameEventTypeById(String id) {
        return findEventTypeById(id)
                .map(EventTypeDto::from)
                .orElseThrow(() -> eventTypeNotFound(id));
    }


    /**
     * Updates a game event type or creates it if it does not exist.
     *
     * @param id               the identifier of the game event type
     * @param gameEventTypeDto the game event type to update
     * @return the updated game event type
     * @throws PreDefinedModificationForbiddenException if the game event type is predefined
     */
    public EventTypeDto updateGameEventType(String id, EventTypeDto gameEventTypeDto) {
        requireNotPredefined(id);

        EventTypeEntity entity = EventTypeEntity.from(gameEventTypeDto)
                .setIdentifier(id);

        entity = gameEventTypeRepository.save(entity);

        return EventTypeDto.from(entity);
    }

    /**
     * Deletes a game event type.
     *
     * @param id the identifier of the game event type
     * @throws PreDefinedModificationForbiddenException if the game event type is predefined
     */
    public void deleteGameEventType(String id) {
        requireNotPredefined(id);

        gameEventTypeRepository.deleteById(id);
    }

    public boolean existsGameEventType(String id) {
        return findEventTypeById(id).isPresent();
    }

    private Optional<EventType> findEventTypeById(String id) {
        return PredefinedEventTypes.findById(id)
                .or(() -> gameEventTypeRepository.findById(id));
    }

    private void requireNotPredefined(String id) {
        PredefinedEventTypes.findById(id)
                .ifPresent(entity -> {
                    throw new PreDefinedModificationForbiddenException(CLASS_NAME, id);
                });
    }

    @NotNull
    public static ResourceNotFoundException eventTypeNotFound(String id) {
        return new ResourceNotFoundException(CLASS_NAME, id);
    }
}
