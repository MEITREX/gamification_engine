package de.unistuttgart.iste.meitrex.rulesengine.persistence;

import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEventScope;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.EventTypeEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.EventTypeRepository;
import de.unistuttgart.iste.meitrex.util.MeitrexPostgresSqlContainer;
import io.vertx.core.json.JsonObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@Testcontainers
@ExtendWith(MeitrexPostgresSqlContainer.class)
class EventTypeRepositoryTest {

    @Autowired
    private EventTypeRepository eventTypeRepository;

    @Test
    @Commit
    void testSaveAndFind() {
        EventTypeEntity eventTypeEntity = EventTypeEntity.builder()
                .identifier("Test Event Type")
                .description("This is a test event type")
                .defaultScope(GameEventScope.GAME)
                .eventSchema(new JsonObject().put("key", "value"))
                .build();

        eventTypeRepository.save(eventTypeEntity);

        EventTypeEntity foundEventTypeEntity = eventTypeRepository.findById(eventTypeEntity.getIdentifier()).orElseThrow();

        assertThat(foundEventTypeEntity.getIdentifier(), is(eventTypeEntity.getIdentifier()));
        assertThat(foundEventTypeEntity.getDescription(), is(eventTypeEntity.getDescription()));
        assertThat(foundEventTypeEntity.getDefaultScope(), is(eventTypeEntity.getDefaultScope()));
        assertThat(foundEventTypeEntity.getSchemaAsJsonObject(), is(eventTypeEntity.getSchemaAsJsonObject()));
    }

    @Test
    @Commit
    void testDelete() {
        EventTypeEntity eventTypeEntity = EventTypeEntity.builder()
                .identifier("Test Event Type")
                .description("This is a test event type")
                .defaultScope(GameEventScope.GAME)
                .eventSchema(new JsonObject().put("key", "value"))
                .build();

        eventTypeRepository.save(eventTypeEntity);

        eventTypeRepository.deleteById(eventTypeEntity.getIdentifier());

        assertThat(eventTypeRepository.existsById(eventTypeEntity.getIdentifier()), is(false));
    }
}