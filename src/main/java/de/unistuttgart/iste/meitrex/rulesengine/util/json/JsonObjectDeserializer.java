package de.unistuttgart.iste.meitrex.rulesengine.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

public class JsonObjectDeserializer extends StdDeserializer<JsonObject> {

    public JsonObjectDeserializer() {
        super(JsonObject.class);
    }

    @Override
    public JsonObject deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return new JsonObject(jsonParser.readValueAsTree().toString());
    }
}
