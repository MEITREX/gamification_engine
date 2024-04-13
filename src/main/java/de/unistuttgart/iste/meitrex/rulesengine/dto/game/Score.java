package de.unistuttgart.iste.meitrex.rulesengine.dto.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Value
@RequiredArgsConstructor
@With
public class Score {

    @NotNull @NotBlank
    String key;

    int value;
}
