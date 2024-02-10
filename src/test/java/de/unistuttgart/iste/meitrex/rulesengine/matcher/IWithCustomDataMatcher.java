package de.unistuttgart.iste.meitrex.rulesengine.matcher;

import de.unistuttgart.iste.meitrex.rulesengine.model.IWithCustomData;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeDiagnosingMatcher;

public class IWithCustomDataMatcher extends TypeSafeDiagnosingMatcher<IWithCustomData> {

    private final IWithCustomData expected;

    public IWithCustomDataMatcher(IWithCustomData expected) {
        this.expected = expected;
    }

    @Override
    protected boolean matchesSafely(IWithCustomData item, Description description) {
        if (!expected.getAdditionalData().equals(item.getAdditionalData())) {
            description.appendText("Additional data was ")
                    .appendValue(item.getAdditionalData());
            return false;
        }

        if (!expected.getScores().equals(item.getScores())) {
            description.appendText("Scores were ")
                    .appendValue(item.getScores());
            return false;
        }

        if (!expected.getFlags().equals(item.getFlags())) {
            description.appendText("Flags were ")
                    .appendValue(item.getFlags());
            return false;
        }

        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("additional data ")
                .appendValue(expected.getAdditionalData())
                .appendText(", scores ")
                .appendValue(expected.getScores())
                .appendText(" and flags ")
                .appendValue(expected.getFlags());
    }

    public static IWithCustomDataMatcher sameCustomDataAs(IWithCustomData expected) {
        return new IWithCustomDataMatcher(expected);
    }
}
