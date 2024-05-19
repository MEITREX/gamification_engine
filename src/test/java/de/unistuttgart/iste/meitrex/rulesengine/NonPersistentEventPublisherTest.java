package de.unistuttgart.iste.meitrex.rulesengine;

import de.unistuttgart.iste.meitrex.rulesengine.util.NonPersistentEventPublisher;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class NonPersistentEventPublisherTest {

    @Test
    void test_publishEvent_publishesEventToSubscribers() {
        // Arrange
        NonPersistentEventPublisher<String> publisher = new NonPersistentEventPublisher<>();
        Flux<String> eventStream = publisher.getEventStream();

        StepVerifier stepVerifier = StepVerifier.create(eventStream)
                .expectNext("Test Event")
                .thenCancel()
                .verifyLater();

        // Act
        String event = publisher.publishEvent("Test Event");

        // Assert
        stepVerifier.verify();

        assertEquals("Test Event", event);
    }

}
