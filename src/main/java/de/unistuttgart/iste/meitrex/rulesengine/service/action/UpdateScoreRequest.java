package de.unistuttgart.iste.meitrex.rulesengine.service.action;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateScoreRequest {

    private final String scoreName;
    private final int newScore;
}
