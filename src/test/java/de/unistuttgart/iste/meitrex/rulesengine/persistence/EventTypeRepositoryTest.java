package de.unistuttgart.iste.meitrex.rulesengine.persistence;

import de.unistuttgart.iste.meitrex.rulesengine.model.event.EventVisibility;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.EventTypeEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.GameEventEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.EventTypeRepository;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.GameEventRepository;
import de.unistuttgart.iste.meitrex.rulesengine.service.event.PredefinedEventTypes;
import de.unistuttgart.iste.meitrex.util.MeitrexPostgresSqlContainer;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.OffsetDateTime;
import java.util.UUID;

import static de.unistuttgart.iste.meitrex.rulesengine.matcher.EventTypeMatcher.sameEventTypeAs;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@Testcontainers
@ExtendWith(MeitrexPostgresSqlContainer.class)
class EventTypeRepositoryTest {

    @Autowired
    private EventTypeRepository eventTypeRepository;
    @Autowired
    private GameEventRepository gameEventRepository;

    @Test
    @Commit
    void testSaveAndFind() {
        EventTypeEntity eventTypeEntity = EventTypeEntity.builder()
                .identifier("Test Event Type")
                .description("This is a test event type")
                .defaultVisibility(EventVisibility.GAME)
                .eventSchema(new JsonObject().put("key", "value"))
                .build();

        eventTypeRepository.save(eventTypeEntity);

        EventTypeEntity foundEventTypeEntity = eventTypeRepository.findById(eventTypeEntity.getIdentifier()).orElseThrow();

        assertThat(foundEventTypeEntity, is(sameEventTypeAs(eventTypeEntity)));
    }

    @Test
    @Commit
    void testDeleteWithExistingEvent() {
        EventTypeEntity eventTypeEntity = EventTypeEntity.builder()
                .identifier("Test Event Type")
                .description("This is a test event type")
                .defaultVisibility(EventVisibility.GAME)
                .eventSchema(new JsonObject().put("key", "value"))
                .build();
        eventTypeRepository.save(eventTypeEntity);

        GameEventEntity gameEventEntity = GameEventEntity.builder()
                .dbEventType(eventTypeEntity)
                .eventTypeIdentifier(eventTypeEntity.getIdentifier())
                .visibility(EventVisibility.GAME)
                .gameId(UUID.randomUUID())
                .timestamp(OffsetDateTime.now())
                .mostRecentChildTimestamp(OffsetDateTime.now())
                .build();
        gameEventRepository.save(gameEventEntity);

        eventTypeRepository.deleteById(eventTypeEntity.getIdentifier());

        GameEventEntity newFoundEvent = gameEventRepository.findById(gameEventEntity.getId()).orElseThrow();
        assertThat(newFoundEvent.getEventType(), is(PredefinedEventTypes.UNKNOWN));

        assertThat(eventTypeRepository.existsById(eventTypeEntity.getIdentifier()), is(false));
    }
}