package de.unistuttgart.iste.meitrex.rulesengine.service;

import de.unistuttgart.iste.meitrex.rulesengine.dto.event.EventTypeDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.*;
import de.unistuttgart.iste.meitrex.rulesengine.matcher.EventTypeMatcher;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.EventType;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.EventVisibility;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.EventTypeEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.EventTypeRepository;
import de.unistuttgart.iste.meitrex.rulesengine.service.event.EventTypeService;
import de.unistuttgart.iste.meitrex.rulesengine.service.event.PredefinedEventTypes;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static de.unistuttgart.iste.meitrex.rulesengine.matcher.EventTypeMatcher.sameEventTypeAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GameEventTypeServiceTest {

    @Mock
    private EventTypeRepository eventTypeRepository;

    @InjectMocks
    private EventTypeService eventTypeService;

    @Test
    void testCreateGameEventType() {
        EventTypeDto inputDto = getTestEventTypeDto();

        when(eventTypeRepository.findById(inputDto.getIdentifier())).thenReturn(Optional.empty());
        when(eventTypeRepository.save(any())).thenAnswer(returnsFirstArg());

        EventTypeDto result = eventTypeService.createEventType(inputDto);

        assertThat(result, is(sameEventTypeAs(inputDto)));

        verify(eventTypeRepository, times(1)).findById(inputDto.getIdentifier());
        verify(eventTypeRepository, times(1)).save(any());
    }

    @Test
    void testCreateGameEventTypeAlreadyExists() {
        EventTypeDto inputDto = getTestEventTypeDto();

        when(eventTypeRepository.findById(inputDto.getIdentifier())).thenReturn(Optional.of(getTestEventTypeEntity()));

        assertThrows(ResourceAlreadyExistsException.class, () -> eventTypeService.createEventType(inputDto));

        verify(eventTypeRepository, times(1)).findById(inputDto.getIdentifier());
        verify(eventTypeRepository, never()).save(any());
    }

    @Test
    void testGetAllGameEventTypes() {
        EventTypeEntity eventTypeEntity = getTestEventTypeEntity().setIdentifier("TEST1");
        EventTypeEntity eventTypeEntity2 = getTestEventTypeEntity().setIdentifier("TEST2");

        when(eventTypeRepository.findAll())
                .thenReturn(List.of(eventTypeEntity, eventTypeEntity2));

        List<EventTypeDto> actual = eventTypeService.getAllGameEventTypes();

        EventTypeMatcher[] expectedPredefinedEventTypes = PredefinedEventTypes.getAll().stream()
                .map(EventTypeMatcher::new)
                .toArray(EventTypeMatcher[]::new);
        assertThat(actual, hasItems(sameEventTypeAs(eventTypeEntity), sameEventTypeAs(eventTypeEntity2)));
        assertThat(actual, hasItems(expectedPredefinedEventTypes));
        assertThat(actual, hasSize(PredefinedEventTypes.getAll().size() + 2));

        verify(eventTypeRepository, times(1)).findAll();
    }

    @Test
    void testGetGameEventTypeById() {
        String id = "TEST";
        EventTypeEntity gameEventTypeEntity = getTestEventTypeEntity().setIdentifier(id);
        when(eventTypeRepository.findById(id)).thenReturn(Optional.of(gameEventTypeEntity));

        EventTypeDto result = eventTypeService.getGameEventTypeById(id);

        assertThat(result, is(sameEventTypeAs(gameEventTypeEntity)));

        verify(eventTypeRepository, times(1)).findById(id);
    }

    @Test
    void testGetGameEventTypeByIdNotFound() {
        String id = "Test Event Type";
        when(eventTypeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> eventTypeService.getGameEventTypeById(id));

        verify(eventTypeRepository, times(1)).findById(id);
    }

    @Test
    void testUpdateGameEventType() {
        String id = "TEST";
        EventTypeDto input = getTestEventTypeDto();

        when(eventTypeRepository.save(any())).thenAnswer(returnsFirstArg());

        EventTypeDto result = eventTypeService.updateGameEventType(id, input);

        assertThat(result, is(sameEventTypeAs(input)));

        verify(eventTypeRepository, times(1)).save(any());
    }

    @Test
    void testUpdatePredefinedGameEventType() {
        String id = getPredefinedEventType().getIdentifier();
        EventTypeDto input = getTestEventTypeDto();

        assertThrows(PreDefinedModificationForbiddenException.class, () -> eventTypeService.updateGameEventType(id, input));

        verify(eventTypeRepository, never()).save(any());
    }

    @Test
    void testDeleteGameEventType() {
        String id = "Test Event Type";
        doNothing().when(eventTypeRepository).deleteById(id);

        eventTypeService.deleteGameEventType(id);

        verify(eventTypeRepository, times(1)).deleteById(id);
    }

    @Test
    void testDeletePredefinedGameEventType() {
        String id = getPredefinedEventType().getIdentifier();

        assertThrows(PreDefinedModificationForbiddenException.class, () -> eventTypeService.deleteGameEventType(id));

        verify(eventTypeRepository, never()).deleteById(id);
    }

    @Test
    void testExistsGameEventType() {
        String id = "TEST";

        when(eventTypeRepository.findById(id)).thenReturn(Optional.of(getTestEventTypeEntity()));
        assertThat(eventTypeService.existsGameEventType(id), is(true));
        verify(eventTypeRepository, times(1)).findById(id);

        when(eventTypeRepository.findById(id)).thenReturn(Optional.empty());
        assertThat(eventTypeService.existsGameEventType(id), is(false));
        verify(eventTypeRepository, times(2)).findById(id);

        String predefinedId = getPredefinedEventType().getIdentifier();
        assertThat(eventTypeService.existsGameEventType(getPredefinedEventType().getIdentifier()), is(true));
        verify(eventTypeRepository, never()).findById(predefinedId);
    }

    private EventTypeEntity getTestEventTypeEntity() {
        return new EventTypeEntity()
                .setIdentifier("TEST")
                .setDescription("Test Event Type")
                .setEventSchema(JsonObject.of("type", "object"))
                .setDefaultVisibility(EventVisibility.GAME);
    }

    private EventType getPredefinedEventType() {
        return PredefinedEventTypes.getAll().stream().findAny().orElseThrow();
    }

    private EventTypeDto getTestEventTypeDto() {
        return EventTypeDto.builder()
                .identifier("TEST")
                .description("Test Event Type")
                .eventSchema(JsonObject.of("type", "object"))
                .defaultVisibility(EventVisibility.GAME)
                .build();
    }
}