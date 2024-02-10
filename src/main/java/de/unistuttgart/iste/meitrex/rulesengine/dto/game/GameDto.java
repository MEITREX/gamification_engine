package de.unistuttgart.iste.meitrex.rulesengine.dto.game;

import de.unistuttgart.iste.meitrex.rulesengine.model.game.Game;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vertx.core.json.JsonObject;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.*;

/**
 * DTO for {@link de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.GameEntity}
 */
@Schema(name = "GameDto", description = "The data transfer object for a game")
@Value
@With
@Jacksonized
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class GameDto implements Game {

    @Schema(description = "The unique identifier of the game",
            example = "123e4567-e89b-12d3-a456-426614174000",
            accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull UUID id;

    @Schema(description = "The name of the game",
            example = "Game A",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    @NotBlank String name;

    @Schema(description = "The scores of the game. " +
                          "Can be used to allow game wide scores or other features." +
                          "The scores are unique and case sensitive." +
                          "They cannot be set with this API, but only posting a SET_SCORE or ADD_SCORE event.",
            example = "{\"score1\": 100, \"score2\": 200}",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            defaultValue = "{}")
    @NotNull
    @Builder.Default
    Map<String, Integer> scores = new HashMap<>();

    @Schema(description = "The flags of the game. " +
                          "Can be used to allow game wide achievements or other features." +
                          "The flags are unique and case sensitive." +
                          "They cannot be set with this API, but only posting a SET_FLAG event.",
            example = "[\"flag1\", \"flag2\"]",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            defaultValue = "[]")
    @NotNull
    @Builder.Default
    Set<String> flags = new HashSet<>();

    @Schema(description = "Custom data of the game",
            example = "{\"key\": \"value\"}",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            defaultValue = "{}")
    @NotNull
    @Builder.Default
    JsonObject additionalData = new JsonObject();

    @NotNull
    public static GameDto from(@NotNull Game game) {
        return GameDto.builder()
                .id(game.getId())
                .name(game.getName())
                .scores(game.getScores())
                .flags(game.getFlags())
                .additionalData(game.getAdditionalData())
                .build();
    }


}
