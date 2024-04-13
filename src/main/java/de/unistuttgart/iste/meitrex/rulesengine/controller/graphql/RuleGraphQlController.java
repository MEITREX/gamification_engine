package de.unistuttgart.iste.meitrex.rulesengine.controller.graphql;

import de.unistuttgart.iste.meitrex.rulesengine.controller.RuleController;
import de.unistuttgart.iste.meitrex.rulesengine.dto.rule.RuleDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.graphql.data.method.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.UUID;

/**
 * The controller for the GraphQL API of the rules.
 */
@Controller
@RequiredArgsConstructor
public class RuleGraphQlController {

    private final RuleController ruleController;

    @MutationMapping("createRule")
    public RuleDto createRule(
            @Valid @Argument(name = "rule")
            RuleDto ruleDto
    ) {
        return ruleController.createRule(ruleDto);
    }

    @MutationMapping("updateRule")
    public RuleDto updateRule(
            @Argument(name = "ruleId")
            UUID ruleId,

            @Valid @Argument(name = "rule")
            RuleDto ruleDto
    ) {
        return ruleController.updateRule(ruleId, ruleDto).getBody();
    }

    @MutationMapping("deleteRule")
    public boolean deleteRule(
            @Argument(name = "ruleId")
            UUID ruleId
    ) {
        ruleController.deleteRule(ruleId);
        return true;
    }

    @QueryMapping("rule")
    public RuleDto getRule(
            @Argument(name = "ruleId")
            UUID ruleId
    ) {
        return ruleController.getRule(ruleId);
    }

    @QueryMapping("rules")
    public List<RuleDto> getAllRules() {
        return ruleController.getRules();
    }
}
