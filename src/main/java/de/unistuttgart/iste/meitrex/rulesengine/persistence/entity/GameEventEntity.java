package de.unistuttgart.iste.meitrex.rulesengine.persistence.entity;

import de.unistuttgart.iste.meitrex.rulesengine.model.event.*;
import de.unistuttgart.iste.meitrex.rulesengine.service.event.PredefinedEventTypes;
import de.unistuttgart.iste.meitrex.rulesengine.util.json.JsonObjectDbConverter;
import io.vertx.core.json.JsonObject;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.Accessors;

import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "game_event")
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class GameEventEntity implements GameEvent {
    @Id
    @NotNull
    @Column(name = "id", nullable = false)
    @ToString.Include
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "timestamp", nullable = false)
    @ToString.Include
    @NotNull
    private OffsetDateTime timestamp;

    @Column(name = "most_recent_child_timestamp", nullable = false)
    @ToString.Include
    @NotNull
    private OffsetDateTime mostRecentChildTimestamp;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "event_type_db_identifier", referencedColumnName = "identifier")
    @Nullable
    private EventTypeEntity dbEventType;

    @Column(name = "event_type_identifier", nullable = true)
    @Nullable
    @ToString.Include
    private String eventTypeIdentifier;

    @Column(name = "visibility", nullable = false)
    @NotNull
    @Builder.Default
    @Enumerated(EnumType.STRING)
    @ToString.Include
    private EventVisibility visibility = EventVisibility.GAME;

    @Column(name = "target_player_id", nullable = true)
    @ToString.Include
    @Nullable
    private UUID playerId;

    @Column(name = "game_id", nullable = false)
    @ToString.Include
    @NotNull
    private UUID gameId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_event_id", referencedColumnName = "id")
    @Nullable
    private GameEventEntity parentEvent;

    @OneToMany(mappedBy = "parentEvent", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    @Builder.Default
    @NotNull
    private List<GameEventEntity> childEvents = new ArrayList<>();

    @Column(name = "data")
    @Convert(converter = JsonObjectDbConverter.class)
    @Builder.Default
    private JsonObject data = new JsonObject();

    @NotNull
    public String getEventTypeIdentifier() {
        return eventTypeIdentifier != null ? eventTypeIdentifier : PredefinedEventTypes.UNKNOWN.getIdentifier();
    }

    @Override
    public EventType getEventType() {
        return PredefinedEventTypes.findById(getEventTypeIdentifier())
                .or(() -> Optional.ofNullable(dbEventType))
                .orElse(PredefinedEventTypes.UNKNOWN);
    }

    @Nullable
    @Override
    public UUID getParentEventId() {
        return parentEvent != null ? parentEvent.getId() : null;
    }
}