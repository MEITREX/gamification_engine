package de.unistuttgart.iste.meitrex.rulesengine.persistence.entity;

import de.unistuttgart.iste.meitrex.rulesengine.model.event.*;
import de.unistuttgart.iste.meitrex.rulesengine.service.event.PredefinedEventTypes;
import de.unistuttgart.iste.meitrex.rulesengine.util.json.JsonObjectDbConverter;
import io.vertx.core.json.JsonObject;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

import java.util.*;

@Entity
@Table(name = "game_event_type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "action", discriminatorType = DiscriminatorType.STRING)
@DiscriminatorValue("NONE")
@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true)
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EventTypeEntity implements EventType {

    @Id
    @Column(name = "identifier", nullable = false)
    @ToString.Include
    private String identifier;

    @Column(name = "description")
    @ToString.Include
    private String description;

    @Column(name = "event_schema")
    @Convert(converter = JsonObjectDbConverter.class)
    @Builder.Default
    private JsonObject eventSchema = new JsonObject();

    @Column(name = "default_visibility")
    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    @ToString.Include
    private EventVisibility defaultVisibility = EventVisibility.GAME;

    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "event_type_db_identifier", referencedColumnName = "identifier")
    @Builder.Default
    private List<GameEventEntity> events = new ArrayList<>();

    @Column(name = "action", insertable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    @Setter(AccessLevel.PROTECTED)
    @Builder.Default
    private ActionOnEvent action = ActionOnEvent.NONE;

    @Override
    public JsonObject getSchemaAsJsonObject() {
        return eventSchema;
    }

    @PreRemove
    public void preRemove() {
        // Set the event type of all events to UNKNOWN to avoid foreign key constraint violations
        events.forEach(event -> event
                .setDbEventType(null)
                .setEventTypeIdentifier(PredefinedEventTypes.UNKNOWN.getIdentifier()));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof EventTypeEntity other)) return false;

        return Objects.equals(identifier, other.identifier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier);
    }

    public static EventTypeEntity from(EventType gameEventType) {
        if (gameEventType.getAction() == ActionOnEvent.NONE) {
            return new EventTypeEntity()
                    .setIdentifier(gameEventType.getIdentifier())
                    .setDescription(gameEventType.getDescription())
                    .setEventSchema(gameEventType.getSchemaAsJsonObject())
                    .setAction(gameEventType.getAction())
                    .setDefaultVisibility(gameEventType.getDefaultVisibility());
        }
        throw new UnsupportedOperationException("Not implemented yet");
    }
}