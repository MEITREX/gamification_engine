package de.unistuttgart.iste.meitrex.rulesengine.service.event;

import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEventScope;
import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEventType;
import io.vertx.core.json.JsonObject;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PredefinedEventType implements GameEventType {

    private String identifier;
    private String description;
    private GameEventScope defaultScope;
    private JsonObject eventSchema;

    @Override
    public JsonObject getSchemaAsJsonObject() {
        return eventSchema;
    }
}
