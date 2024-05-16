package de.unistuttgart.iste.meitrex.rulesengine.persistence.repository;

import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.JsonSchemaRuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException.notFoundException;

@Repository
public interface RuleRepository extends JpaRepository<JsonSchemaRuleEntity, UUID> {

    default JsonSchemaRuleEntity findByIdOrThrow(UUID id) {
        return findById(id).orElseThrow(() -> notFoundException("Rule", id));
    }
}
