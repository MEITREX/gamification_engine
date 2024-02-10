package de.unistuttgart.iste.meitrex.rulesengine.matcher;

import de.unistuttgart.iste.meitrex.rulesengine.model.event.GameEventType;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class EventTypeMatcher extends TypeSafeDiagnosingMatcher<GameEventType> {

    private final GameEventType expected;

    public EventTypeMatcher(GameEventType expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(GameEventType item, Description description) {
        if (!expected.getIdentifier().equals(item.getIdentifier())) {
            description.appendText("Game event type identifier was ")
                    .appendValue(item.getIdentifier());
            return false;
        }

        if (!expected.getDescription().equals(item.getDescription())) {
            description.appendText("Game event type description was ")
                    .appendValue(item.getDescription());
            return false;
        }

        if (expected.getDefaultScope() != item.getDefaultScope()) {
            description.appendText("Game event type default scope was ")
                    .appendValue(item.getDefaultScope());
            return false;
        }

        if (!expected.getSchemaAsJsonObject().equals(item.getSchemaAsJsonObject())) {
            description.appendText("Game event type schema was ")
                    .appendValue(item.getSchemaAsJsonObject());
            return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a GameEventType with identifier ")
                .appendValue(expected.getIdentifier())
                .appendText(", description ")
                .appendValue(expected.getDescription())
                .appendText(", default scope ")
                .appendValue(expected.getDefaultScope())
                .appendText(", and schema ")
                .appendValue(expected.getSchemaAsJsonObject());
    }

    public static EventTypeMatcher sameEventTypeAs(GameEventType expected) {
        return new EventTypeMatcher(expected);
    }
}