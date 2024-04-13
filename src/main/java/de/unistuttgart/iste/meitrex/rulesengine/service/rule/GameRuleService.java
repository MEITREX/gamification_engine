package de.unistuttgart.iste.meitrex.rulesengine.service.rule;

import de.unistuttgart.iste.meitrex.rulesengine.dto.rule.RuleDto;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.JsonSchemaRuleEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.RuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameRuleService {

    private final RuleRepository ruleRepository;

    public RuleDto createRule(RuleDto ruleDto) {
        JsonSchemaRuleEntity entity = JsonSchemaRuleEntity.from(ruleDto);

        entity = ruleRepository.save(entity);

        return RuleDto.from(entity);
    }

    public RuleDto updateRule(UUID id, RuleDto ruleDto) {
        // logic is the same as createRule
        return createRule(ruleDto.withId(id));
    }

    public boolean existsRule(UUID id) {
        return ruleRepository.existsById(id);
    }

    public void deleteRule(UUID ruleId) {
        ruleRepository.deleteById(ruleId);
    }

    public RuleDto getRule(UUID ruleId) {
        JsonSchemaRuleEntity rule = ruleRepository.findByIdOrThrow(ruleId);

        return RuleDto.from(rule);
    }

    public List<RuleDto> getAllRules() {
        return ruleRepository.findAll().stream()
                .map(RuleDto::from)
                .toList();
    }
}
