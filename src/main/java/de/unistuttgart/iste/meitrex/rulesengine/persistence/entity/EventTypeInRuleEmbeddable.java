package de.unistuttgart.iste.meitrex.rulesengine.persistence.entity;

import de.unistuttgart.iste.meitrex.rulesengine.model.rule.EventTypeInRule;
import de.unistuttgart.iste.meitrex.rulesengine.util.json.JsonObjectDbConverter;
import io.vertx.core.json.JsonObject;
import jakarta.persistence.Convert;
import jakarta.persistence.Embeddable;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

@Embeddable
@Getter
@Setter
@ToString
@Accessors(chain = true)
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EventTypeInRuleEmbeddable implements EventTypeInRule {

    private String eventTypeIdentifier;

    @Convert(converter = JsonObjectDbConverter.class)
    @ToString.Exclude
    private JsonObject dataTemplate;

    public static EventTypeInRuleEmbeddable from(EventTypeInRule eventTypeInRule) {
        return EventTypeInRuleEmbeddable.builder()
                .eventTypeIdentifier(eventTypeInRule.getEventTypeIdentifier())
                .dataTemplate(eventTypeInRule.getDataTemplate())
                .build();
    }

}
