package de.unistuttgart.iste.meitrex.rulesengine.controller.graphql;

import de.unistuttgart.iste.meitrex.rulesengine.controller.EventTypeController;
import de.unistuttgart.iste.meitrex.rulesengine.dto.event.EventTypeDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
public class EventTypeGraphQlController {

    private final EventTypeController eventTypeController;

    @MutationMapping("createEventType")
    public EventTypeDto createEventType(
            @Valid @Argument(name = "eventType")
            EventTypeDto eventTypeDto
    ) {
        return eventTypeController.createEventType(eventTypeDto);
    }

    @MutationMapping("updateEventType")
    public EventTypeDto updateEventType(
            @Argument(name = "eventTypeId")
            UUID eventTypeId,

            @Valid @Argument(name = "eventType")
            EventTypeDto eventTypeDto
    ) {
        return eventTypeController.updateGameEventType(eventTypeId.toString(), eventTypeDto).getBody();
    }

    @MutationMapping("deleteEventType")
    public boolean deleteEventType(
            @Argument(name = "eventTypeId")
            UUID eventTypeId
    ) {
        eventTypeController.deleteGameEventType(eventTypeId.toString());
        return true;
    }

    @QueryMapping("eventType")
    public EventTypeDto getEventType(
            @Argument(name = "eventTypeId")
            UUID eventTypeId
    ) {
        return eventTypeController.getGameEventTypeById(eventTypeId.toString());
    }

    @QueryMapping("eventTypes")
    public List<EventTypeDto> getAllEventTypes() {
        return eventTypeController.getAllEventTypes();
    }
}
