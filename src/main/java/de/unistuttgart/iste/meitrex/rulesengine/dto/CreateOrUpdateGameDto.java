package de.unistuttgart.iste.meitrex.rulesengine.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

@Schema(name = "CreateOrUpdateGameDto", description = "Create request data for a game")
@Value
@With
@Jacksonized
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class CreateOrUpdateGameDto {
    @Schema(description = "The name of the game",
            example = "Game A",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @NotBlank
    String name;
}
