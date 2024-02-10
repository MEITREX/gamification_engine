package de.unistuttgart.iste.meitrex.rulesengine.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

/**
 * The primary key of the player entity.
 * It consists of the game id and the user id as a player is a user in a game.
 */
@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerId implements Serializable {

    @Column(name = "game_id")
    private UUID gameId;

    @Column(name = "user_id")
    private UUID userId;
}