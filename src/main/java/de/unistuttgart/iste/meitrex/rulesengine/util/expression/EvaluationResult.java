package de.unistuttgart.iste.meitrex.rulesengine.util.expression;

import lombok.*;

import java.util.Collections;
import java.util.List;

/**
 * Represents the result of an evaluation of an expression.
 * Allows to return the result and additional details.
 *
 * @param <T> the type of the result
 */
@RequiredArgsConstructor
@Getter
@ToString
public class EvaluationResult<T> {

    private final T result;
    private final List<String> details;

    public EvaluationResult(T result) {
        this.result = result;
        this.details = Collections.emptyList();
    }
}
