package de.unistuttgart.iste.meitrex.rulesengine.persistence.entity;

import de.unistuttgart.iste.meitrex.rulesengine.model.game.Game;
import de.unistuttgart.iste.meitrex.rulesengine.util.json.JsonObjectDbConverter;
import io.vertx.core.json.JsonObject;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.*;


@Entity
@Table(name = "game")
@Getter
@Setter
@Accessors(chain = true)
@Builder
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity implements Game {

    @Id
    @Column(name = "id", nullable = false)
    @ToString.Include
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "name")
    @ToString.Include
    private String name;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PlayerEntity> players = new ArrayList<>();

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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof GameEntity other)) return false;

        return Objects.equals(id, other.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public static GameEntity from(Game game) {
        return GameEntity.builder()
                .id(game.getId())
                .name(game.getName())
                .scores(game.getScores())
                .flags(game.getFlags())
                .additionalData(game.getAdditionalData())
                .build();
    }
}