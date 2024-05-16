package de.unistuttgart.iste.meitrex.rulesengine.util.json;

import io.vertx.json.schema.common.dsl.StringSchemaBuilder;
import lombok.NoArgsConstructor;

import static de.unistuttgart.iste.meitrex.rulesengine.util.json.MeitrexKeywords.description;
import static de.unistuttgart.iste.meitrex.rulesengine.util.json.MeitrexKeywords.example;
import static io.vertx.json.schema.common.dsl.Schemas.stringSchema;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MeitrexSchemas {

    private static final String UUID_PATTERN = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$";

    public static StringSchemaBuilder uuidSchema() {
        return stringSchema()
                .withKeyword("pattern", UUID_PATTERN)
                .with(example("123e4567-e89b-12d3-a456-426614174000"));
    }

    public static StringSchemaBuilder playerIdSchema() {
        return uuidSchema()
                .with(description("The unique identifier of the player. Should be an existing player."));
    }
}
