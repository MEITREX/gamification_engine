package de.unistuttgart.iste.meitrex.rulesengine.util.json;

import io.vertx.core.json.JsonObject;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import lombok.extern.slf4j.Slf4j;

@Converter
@Slf4j
public class JsonObjectDbConverter implements AttributeConverter<JsonObject, String> {

    private final JsonObjectDeserializer deserializer = new JsonObjectDeserializer();
    private final JsonObjectSerializer serializer = new JsonObjectSerializer();

    @Override
    public String convertToDatabaseColumn(JsonObject entries) {
        return serializer.encode(entries);
    }

    @Override
    public JsonObject convertToEntityAttribute(String s) {
        try {
            return deserializer.deserialize(s);
        } catch (Exception e) {
            log.error("Error deserializing JSON from database, returning an empty object", e);
            return new JsonObject().put("invalidData", s);
        }
    }
}
