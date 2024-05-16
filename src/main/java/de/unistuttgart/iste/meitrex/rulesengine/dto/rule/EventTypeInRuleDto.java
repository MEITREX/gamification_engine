package de.unistuttgart.iste.meitrex.rulesengine.dto.rule;

import de.unistuttgart.iste.meitrex.rulesengine.model.rule.EventTypeInRule;
import io.vertx.core.json.JsonObject;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Value
@RequiredArgsConstructor
@With
@Jacksonized
@Builder(toBuilder = true)
public class EventTypeInRuleDto implements EventTypeInRule {

    String eventTypeIdentifier;

    JsonObject dataTemplate;

    public static EventTypeInRuleDto from(EventTypeInRule actionInRule) {
        return EventTypeInRuleDto.builder()
                .eventTypeIdentifier(actionInRule.getEventTypeIdentifier())
                .dataTemplate(actionInRule.getDataTemplate())
                .build();
    }
}
