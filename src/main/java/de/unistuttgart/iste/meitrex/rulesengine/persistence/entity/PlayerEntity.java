package de.unistuttgart.iste.meitrex.rulesengine.persistence.entity;

import de.unistuttgart.iste.meitrex.rulesengine.model.game.Player;
import de.unistuttgart.iste.meitrex.rulesengine.util.json.JsonObjectDbConverter;
import io.vertx.core.json.JsonObject;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.*;


@Entity
@Table(name = "player")
@Getter
@Setter
@Accessors(chain = true)
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerEntity implements Player {

    @EmbeddedId
    @ToString.Include
    private PlayerId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("gameId")
    @JoinColumn(name = "game_id")
    private GameEntity game;

    @Column(name = "name")
    @ToString.Include
    private String name;

    @ElementCollection
    @MapKeyColumn(name = "score_key")
    @Column(name = "score_value")
    @Builder.Default
    private Map<String, Integer> scores = new HashMap<>();

    @ElementCollection
    @Builder.Default
    private Set<String> flags = new HashSet<>();

    @Column(name = "additional_data")
    @Convert(converter = JsonObjectDbConverter.class)
    @Builder.Default
    private JsonObject additionalData = new JsonObject();

    @Override
    public UUID getUserId() {
        return id.getUserId();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof PlayerEntity other)) return false;

        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}