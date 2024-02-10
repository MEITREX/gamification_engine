package de.unistuttgart.iste.meitrex.rulesengine.dto.game;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.UUID;

@Schema(name = "CreatePlayerDto", description = "The data transfer object for creating a player.")
@Value
@RequiredArgsConstructor
@With
@Jacksonized
@Builder(toBuilder = true)
public class CreatePlayerDto {

    @Schema(description = "The name of the player",
            example = "Player 1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull String name;

    @Schema(description = "The user ID associated with the player",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    @NotNull UUID userId;

}
