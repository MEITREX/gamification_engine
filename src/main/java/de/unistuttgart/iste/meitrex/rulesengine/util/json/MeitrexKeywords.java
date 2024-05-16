package de.unistuttgart.iste.meitrex.rulesengine.util.json;

import io.vertx.json.schema.common.dsl.StringKeyword;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = lombok.AccessLevel.PRIVATE)
public class MeitrexKeywords {

    public static StringKeyword description(String description) {
        return new StringKeyword("description", description);
    }

    public static StringKeyword example(String example) {
        return new StringKeyword("example", example);
    }

    public static StringKeyword title(String title) {
        return new StringKeyword("title", title);
    }

    public static StringKeyword pattern(String pattern) {
        return new StringKeyword("pattern", pattern);
    }

}
