package de.unistuttgart.iste.meitrex.rulesengine.persistence.entity;

import de.unistuttgart.iste.meitrex.rulesengine.dto.rule.RuleDto;
import de.unistuttgart.iste.meitrex.rulesengine.model.rule.JsonSchemaRule;
import de.unistuttgart.iste.meitrex.rulesengine.util.json.JsonObjectDbConverter;
import io.vertx.core.json.JsonObject;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@Table(name = "game_rule")
@Getter
@Setter
@Builder
@ToString(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
public class JsonSchemaRuleEntity implements JsonSchemaRule {
    @ToString.Include
    @Id
    @Column(name = "id", nullable = false)
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Column(name = "event_types", nullable = false)
    @ElementCollection
    private Set<String> triggerEventTypes;

    @Column(name = "condition_schema", nullable = false)
    @Convert(converter = JsonObjectDbConverter.class)
    private JsonObject conditionSchema;

    @ElementCollection
    private List<EventTypeInRuleEmbeddable> createEventTypes;

    public static JsonSchemaRuleEntity from(RuleDto ruleDto) {
        return JsonSchemaRuleEntity.builder()
                .id(ruleDto.getOptionalId().orElseGet(UUID::randomUUID))
                .triggerEventTypes(ruleDto.getTriggerEventTypes())
                .conditionSchema(ruleDto.getConditionSchema())
                .createEventTypes(ruleDto.getCreateEventTypes().stream()
                        .map(EventTypeInRuleEmbeddable::from)
                        .toList())
                .build();
    }
}
