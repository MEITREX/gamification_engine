package de.unistuttgart.iste.meitrex.rulesengine.persistence.repository;

import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException.notFoundException;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, UUID> {

    default GameEntity findByIdOrThrow(UUID id) {
        return findById(id).orElseThrow(() -> notFoundException("Game", id));
    }


    /**
     * Query to find all games that have a player with the given user id
     *
     * @param userId the user id
     * @return a list of games
     */
    @Query("SELECT g FROM GameEntity g JOIN g.players p WHERE p.id.userId = :userId")
    List<GameEntity> findAllByUserId(UUID userId);
}
