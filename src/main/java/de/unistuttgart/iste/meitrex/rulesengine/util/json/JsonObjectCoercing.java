package de.unistuttgart.iste.meitrex.rulesengine.util.json;

import graphql.language.StringValue;
import graphql.schema.*;
import io.vertx.core.json.JsonObject;
import jakarta.validation.constraints.NotNull;

/**
 * Coercing for {@link JsonObject}
 * This allows to use {@link JsonObject} as a scalar in the GraphQL schema.
 * It serializes the {@link JsonObject} to a JSON string and parses it back from a JSON string.
 * <p>
 * Note: We override the deprecated methods, thus the deprecation warning is suppressed.
 * We suppress the nullable problems warning, as we use a different NotNull annotation than the one from the graphql package.
 */
@SuppressWarnings({"deprecation", "NullableProblems"})
public class JsonObjectCoercing implements Coercing<JsonObject, String> {

    @Override
    public String serialize(@NotNull Object dataFetcherResult) throws CoercingSerializeException {
        if (dataFetcherResult instanceof JsonObject jsonObject) {
            return jsonObject.encode();
        } else {
            throw new CoercingSerializeException("Can't serialize %s as a JsonObject".formatted(dataFetcherResult));
        }
    }

    @Override
    public JsonObject parseValue(@NotNull Object input) throws CoercingParseValueException {
        if (input instanceof String jsonString) {
            return new JsonObject(jsonString);
        } else {
            throw new CoercingParseValueException("Can't parse %s as a JsonObject".formatted(input));
        }
    }

    @Override
    public JsonObject parseLiteral(@NotNull Object input) throws CoercingParseLiteralException {
        if (input instanceof StringValue stringValue) {
            return new JsonObject(stringValue.getValue());
        } else {
            throw new CoercingParseLiteralException("Can't parse %s as a JsonObject".formatted(input));
        }
    }

    @Override
    public StringValue valueToLiteral(@NotNull Object input) {
        String value = serialize(input);
        return StringValue.newStringValue(value).build();
    }
}
