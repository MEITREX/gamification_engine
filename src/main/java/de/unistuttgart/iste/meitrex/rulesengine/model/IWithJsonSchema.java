package de.unistuttgart.iste.meitrex.rulesengine.model;

import io.vertx.core.json.JsonObject;
import io.vertx.json.schema.JsonSchema;
import jakarta.validation.constraints.NotNull;

public interface IWithJsonSchema {

    @NotNull JsonObject getSchemaAsJsonObject();

    @NotNull
    default JsonSchema getJsonSchema() {
        return JsonSchema.of(getSchemaAsJsonObject());
    }
}
