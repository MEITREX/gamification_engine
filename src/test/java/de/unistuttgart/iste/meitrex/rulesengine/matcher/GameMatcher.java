package de.unistuttgart.iste.meitrex.rulesengine.matcher;

import de.unistuttgart.iste.meitrex.rulesengine.model.game.Game;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class GameMatcher extends TypeSafeDiagnosingMatcher<Game> {

    private final Game expected;
    private final IWithCustomDataMatcher iWithCustomDataMatcher;

    public GameMatcher(Game expected) {
        this.expected = expected;
        this.iWithCustomDataMatcher = IWithCustomDataMatcher.sameCustomDataAs(expected);
    }

    @Override
    protected boolean matchesSafely(Game item, Description description) {
        if (!expected.getId().equals(item.getId())) {
            description.appendText("Game id was ")
                    .appendValue(item.getId());
            return false;
        }

        if (!expected.getName().equals(item.getName())) {
            description.appendText("Game name was ")
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
        description.appendText("a Game with id ")
                .appendValue(expected.getId())
                .appendText(" and name ")
                .appendValue(expected.getName())
                .appendText(" and ")
                .appendDescriptionOf(iWithCustomDataMatcher);
    }

    public static GameMatcher sameGameAs(Game expected) {
        return new GameMatcher(expected);
    }
}