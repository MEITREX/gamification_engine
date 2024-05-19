package de.unistuttgart.iste.meitrex.rulesengine.util;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

/**
 * A class for publishing events and managing an event stream. This class allows for the publishing of events and
 * provides a Flux stream of events for subscribers. It ensures that events are persisted and handles the case where
 * there are no subscribers or an error occurs in the event stream.
 *
 * @param <E> the event type
 * @param <R> the event request type
 */
@Slf4j
@RequiredArgsConstructor
public class EventPublisher<E, R> {

    private Sinks.Many<E> sink        = initSink();
    private Flux<E>       eventStream = null;

    @Getter
    private final EventPersistence<E, R> eventPersistence;

    /**
     * Publishes an event based on the given event request. If the event already exists, it is retrieved and returned.
     * Otherwise, the event is persisted and then emitted to the event stream. If emitting the event fails, it logs the
     * error accordingly.
     *
     * @param eventRequest the event request to publish
     * @return the published or retrieved event
     */
    public E publishEvent(R eventRequest) {
        if (eventPersistence.exists(eventRequest)) {
            return eventPersistence.getEvent(eventRequest);
        }

        E event = eventPersistence.persistEvent(eventRequest);

        Sinks.EmitResult result = emitRetryOnFail(event);

        if (result.isFailure()) {
            if (result == Sinks.EmitResult.FAIL_ZERO_SUBSCRIBER) {
                log.debug("Trying to emit event without subscribers");
            } else {
                log.error("Failed to emit event");
            }
        }

        return event;
    }

    /**
     * Closes the event stream. Should only be used when the stream is no longer needed.
     */
    protected void close() {
        sink.emitComplete(Sinks.EmitFailureHandler.FAIL_FAST);
    }

    /**
     * Provides a Flux stream of events that can be subscribed to.
     *
     * @return the Flux stream of events
     */
    public synchronized Flux<E> getEventStream() {
        if (eventStream == null) {
            eventStream = initEventStream();
        }
        return eventStream;
    }

    private synchronized Flux<E> initEventStream() {
        return sink.asFlux().doOnTerminate(() -> log.warn("Event stream terminated"));
    }

    private synchronized <T> Sinks.Many<T> initSink() {
        // many() to allow multiple signals
        // multicast() to share the sink among multiple subscribers
        // onBackpressureBuffer() to buffer signals until they can be processed
        return Sinks.many().multicast().onBackpressureBuffer();
    }

    private Sinks.EmitResult emitRetryOnFail(E event) {
        Sinks.EmitResult result = sink.tryEmitNext(event);

        if (result.isFailure() && result != Sinks.EmitResult.FAIL_ZERO_SUBSCRIBER) {
            return sink.tryEmitNext(event);
        }

        return result;
    }
}
