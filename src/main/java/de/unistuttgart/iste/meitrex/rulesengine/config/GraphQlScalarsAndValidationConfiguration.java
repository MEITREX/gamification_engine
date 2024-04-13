package de.unistuttgart.iste.meitrex.rulesengine.config;

import de.unistuttgart.iste.meitrex.rulesengine.util.json.JsonObjectCoercing;
import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import graphql.validation.rules.OnValidationErrorStrategy;
import graphql.validation.rules.ValidationRules;
import graphql.validation.schemawiring.ValidationSchemaWiring;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

/**
 * This class sets up the validation rules for the GraphQL schema and the scalar types.
 */
@Configuration
public class GraphQlScalarsAndValidationConfiguration {

    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {

        ValidationRules validationRules = ValidationRules.newValidationRules()
                .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
                .build();

        ValidationSchemaWiring schemaWiring = new ValidationSchemaWiring(validationRules);

        return wiringBuilder -> wiringBuilder
                .directiveWiring(schemaWiring)
                .scalar(ExtendedScalars.DateTime)
                .scalar(ExtendedScalars.Date)
                .scalar(ExtendedScalars.Time)
                .scalar(ExtendedScalars.LocalTime)
                .scalar(ExtendedScalars.UUID)
                .scalar(ExtendedScalars.Url)
                .scalar(GraphQLScalarType.newScalar()
                        .name("JsonObject")
                        .description("A JSON scalar type")
                        .coercing(new JsonObjectCoercing())
                        .build());
    }
}
