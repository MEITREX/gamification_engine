package de.unistuttgart.iste.meitrex.rulesengine.persistence.repository;

import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.JsonSchemaRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException.notFoundException;

@Repository
public interface RuleRepository extends JpaRepository<JsonSchemaRuleEntity, UUID> {

    /**
     * Find all rules by trigger event type
     *
     * @param eventType the event type that triggers the rule
     * @return a list of rules that are triggered by the given event type
     */
    List<JsonSchemaRuleEntity> findAllByTriggerEventTypesContaining(String eventType);

    default JsonSchemaRuleEntity findByIdOrThrow(UUID id) {
        return findById(id).orElseThrow(() -> notFoundException("Rule", id));
    }
}
