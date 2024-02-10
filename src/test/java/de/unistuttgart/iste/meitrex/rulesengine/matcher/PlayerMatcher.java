package de.unistuttgart.iste.meitrex.rulesengine.matcher;

import de.unistuttgart.iste.meitrex.rulesengine.model.game.Player;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class PlayerMatcher extends TypeSafeDiagnosingMatcher<Player> {

    private final Player expected;
    private final IWithCustomDataMatcher iWithCustomDataMatcher;

    public PlayerMatcher(Player expected) {
        this.expected = expected;
        this.iWithCustomDataMatcher = IWithCustomDataMatcher.sameCustomDataAs(expected);
    }

    @Override
    protected boolean matchesSafely(Player item, Description description) {
        if (!expected.getUserId().equals(item.getUserId())) {
            description.appendText("Player user id was ")
                    .appendValue(item.getUserId());
            return false;
        }

        if (!expected.getName().equals(item.getName())) {
            description.appendText("Player name was ")
                    .appendValue(item.getName());
            return false;
        }

        if (!iWithCustomDataMatcher.matches(item)) {
            iWithCustomDataMatcher.describeMismatch(item, description);
            return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("a Player with user id ")
                .appendValue(expected.getUserId())
                .appendText(", name ")
                .appendValue(expected.getName())
                .appendText(" and ")
                .appendDescriptionOf(iWithCustomDataMatcher);
    }

    public static PlayerMatcher samePlayerAs(Player expected) {
        return new PlayerMatcher(expected);
    }
}
