package de.unistuttgart.iste.meitrex.rulesengine.persistence.entity;

import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEventScope;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEventType;
import de.unistuttgart.iste.meitrex.rulesengine.util.json.JsonObjectDbConverter;
import io.vertx.core.json.JsonObject;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Objects;

@Entity
@Table(name = "game_event_type")
@Getter
@Setter
@Accessors(chain = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventTypeEntity implements GameEventType {

    @Id
    @Column(name = "identifier", nullable = false)
    private String identifier;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "default_scope")
    private GameEventScope defaultScope;

    @Column(name = "event_schema")
    @Convert(converter = JsonObjectDbConverter.class)
    private JsonObject eventSchema;

    @Override
    public JsonObject getSchemaAsJsonObject() {
        return eventSchema;
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

    public static EventTypeEntity from(GameEventType gameEventType) {
        return EventTypeEntity.builder()
                .identifier(gameEventType.getIdentifier())
                .description(gameEventType.getDescription())
                .defaultScope(gameEventType.getDefaultScope())
                .eventSchema(gameEventType.getSchemaAsJsonObject())
                .build();
    }
}