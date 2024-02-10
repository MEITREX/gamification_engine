package de.unistuttgart.iste.meitrex.rulesengine.dto.game;

import de.unistuttgart.iste.meitrex.rulesengine.model.game.Player;
import io.swagger.v3.oas.annotations.media.Schema;
import io.vertx.core.json.JsonObject;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.util.*;

/**
 * DTO for {@link de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.PlayerEntity}
 */
@Schema(name = "PlayerDto", description = "The data transfer object for a player")
@Value
@RequiredArgsConstructor
@With
@Jacksonized
@Builder(toBuilder = true)
public class PlayerDto implements Player {

    @Schema(description = "The name of the player",
            example = "Player 1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull
    String name;

    @Schema(description = "The game associated with the player",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            nullable = false)
    @NotNull
    GameDto game;

    @Schema(description = "The user ID associated with the player",
            example = "123e4567-e89b-12d3-a456-426614174000",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            nullable = false)
    @NotNull
    UUID userId;


    @Schema(description = "Additional data of the player. Can be used to store custom data of the player.",
            example = "{\"key\": \"value\"}",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = false,
            defaultValue = "{}")
    @NotNull
    @Builder.Default
    JsonObject additionalData = new JsonObject();

    @Schema(description = "The scores of the player. " +
                          "They cannot be set with this API, but only posting a SET_SCORE or ADD_SCORE event.",
            example = "{\"xp\": 100, \"health\": 200}",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            defaultValue = "{}")
    @NotNull
    @Builder.Default
    Map<String, Integer> scores = new HashMap<>();

    @Schema(description = "The flags of the game. " +
                          "Can be used to implement achievements or other features." +
                          "The flags are unique and case sensitive." +
                          "They cannot be set with this API, but only posting a SET_FLAG event.",
            example = "[\"flag1\", \"flag2\"]",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            accessMode = Schema.AccessMode.READ_ONLY,
            defaultValue = "[]")
    @NotNull
    @Builder.Default
    Set<String> flags = new HashSet<>();

    @NotNull
    public static PlayerDto from(@NotNull Player player) {
        return PlayerDto.builder()
                .name(player.getName())
                .game(GameDto.from(player.getGame()))
                .userId(player.getUserId())
                .additionalData(player.getAdditionalData())
                .scores(player.getScores())
                .flags(player.getFlags())
                .build();
    }
}