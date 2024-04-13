package de.unistuttgart.iste.meitrex.rulesengine.service.rule;

import de.unistuttgart.iste.meitrex.rulesengine.dto.event.CreateGameEventDto;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.*;
import de.unistuttgart.iste.meitrex.rulesengine.model.game.Game;
import de.unistuttgart.iste.meitrex.rulesengine.model.game.Player;
import de.unistuttgart.iste.meitrex.rulesengine.model.rule.GameRule;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.JsonSchemaRuleEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.RuleRepository;
import de.unistuttgart.iste.meitrex.rulesengine.service.event.ValidatorService;
import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.OutputUnit;
import io.vertx.json.schema.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuleEngine {

    private final RuleRepository ruleRepository;
    private final ValidatorService validatorService;

    public List<CreateGameEventDto> runRulesForEvent(GameEvent event, Game game, Player player) {
        EventType eventType = event.getEventType();

        var rules = ruleRepository.findAllByTriggerEventTypesContaining(eventType.getIdentifier());

        for (JsonSchemaRuleEntity rule : rules) {
            Validator validator = validatorService.getValidatorForRule(rule);
            OutputUnit outputUnit = validator.validate(buildEventData(event, player, game));

            if (Boolean.TRUE.equals(outputUnit.getValid())) {
                return buildConsequenceEvents(event, rule);
            } else {
                log.info("Rule {} did not match event {}", rule.getId(), event.getId());
                log.info("Validation errors: {}", outputUnit.getAnnotations());
            }
        }
        return List.of();
    }

    private List<CreateGameEventDto> buildConsequenceEvents(GameEvent event, GameRule rule) {
        List<CreateGameEventDto> result = new ArrayList<>();

        for (var eventTypeInRule : rule.getCreateEventTypes()) {
            result.add(CreateGameEventDto.builder()
                    .playerId(event.getPlayerId())
                    .eventType(eventTypeInRule.getEventTypeIdentifier())
                    .data(eventTypeInRule.getDataTemplate())
                    .visibility(EventVisibility.PLAYER) // TODO: make this configurable
                    //.parentEventId(event.getId())
                    .build());
        }
        return result;
    }

    private JsonObject buildEventData(GameEvent event, Player player, Game game) {
        return new JsonObject()
                .put("game", game)
                .put("player", player)
                .put("data", event.getData());
    }
}
