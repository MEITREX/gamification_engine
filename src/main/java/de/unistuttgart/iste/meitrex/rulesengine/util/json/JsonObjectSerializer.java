package de.unistuttgart.iste.meitrex.rulesengine.util.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

public class JsonObjectSerializer extends StdSerializer<JsonObject> {

    public JsonObjectSerializer() {
        this(null);
    }

    public JsonObjectSerializer(Class<JsonObject> t) {
        super(t);
    }

    @Override
    public void serialize(
            JsonObject jsonObject,
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeRawValue(jsonObject.encode());
    }
}
