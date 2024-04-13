package de.unistuttgart.iste.meitrex.rulesengine.persistence;

import de.unistuttgart.iste.meitrex.common.testutil.MeitrexPostgresSqlContainer;
import de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.JsonSchemaRuleEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.RuleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

// Todo properly implement the test
@SpringBootTest
@Testcontainers
@ExtendWith(MeitrexPostgresSqlContainer.class)
class RuleRepositoryTest {

    @Autowired
    private RuleRepository ruleRepository;

    @Test
    @Commit
    void testSaveAndDelete() {
        JsonSchemaRuleEntity ruleEntity = JsonSchemaRuleEntity.builder()
                // fill in the fields here
                .build();

        ruleRepository.save(ruleEntity);
        JsonSchemaRuleEntity createdRuleEntity = ruleRepository.findByIdOrThrow(ruleEntity.getId());

        assertThat(createdRuleEntity, is(equalTo(ruleEntity)));

        ruleRepository.deleteById(ruleEntity.getId());

        assertThat(ruleRepository.existsById(ruleEntity.getId()), is(false));

    }

    @Test
    @Commit
    void testFindAllByTriggerEventTypesContaining() {
        JsonSchemaRuleEntity ruleEntity1 = JsonSchemaRuleEntity.builder()
                // fill in the fields here
                .build();
        JsonSchemaRuleEntity ruleEntity2 = JsonSchemaRuleEntity.builder()
                // fill in the fields here
                .build();

        ruleRepository.saveAll(Arrays.asList(ruleEntity1, ruleEntity2));

        List<JsonSchemaRuleEntity> rules = ruleRepository.findAllByTriggerEventTypesContaining("eventType");

        assertThat(rules, hasItems(ruleEntity1, ruleEntity2));
    }

    @Test
    void testFindByIdOrThrow() {
        JsonSchemaRuleEntity ruleEntity = JsonSchemaRuleEntity.builder()
                // fill in the fields here
                .build();

        ruleRepository.save(ruleEntity);

        JsonSchemaRuleEntity foundRuleEntity = ruleRepository.findByIdOrThrow(ruleEntity.getId());

        assertThat(foundRuleEntity, is(equalTo(ruleEntity)));

        UUID nonExistentId = UUID.randomUUID();

        assertThrows(ResourceNotFoundException.class, () -> ruleRepository.findByIdOrThrow(nonExistentId));
    }
}
