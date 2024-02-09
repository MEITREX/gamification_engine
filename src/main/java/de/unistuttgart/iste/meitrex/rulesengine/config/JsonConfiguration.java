package de.unistuttgart.iste.meitrex.rulesengine.config;

import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import de.unistuttgart.iste.meitrex.rulesengine.util.json.JsonObjectDeserializer;
import de.unistuttgart.iste.meitrex.rulesengine.util.json.JsonObjectSerializer;
import io.vertx.core.json.JsonObject;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for JSON serialization and deserialization.
 * This configuration is used to serialize and deserialize {@link JsonObject} objects.
 */
@Configuration
public class JsonConfiguration {

    @Bean
    public Module jsonModule() {
        return new SimpleModule()
                .addSerializer(JsonObject.class, new JsonObjectSerializer())
                .addDeserializer(JsonObject.class, new JsonObjectDeserializer());
    }

    public static ObjectMapper createObjectMapper() {
        return new ObjectMapper().registerModule(new JsonConfiguration().jsonModule());
    }

}
