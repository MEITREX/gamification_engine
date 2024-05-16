package de.unistuttgart.iste.meitrex.rulesengine.persistence.repository;

import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.GameEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameEventRepository extends JpaRepository<GameEventEntity, UUID> {

    /**
     * Returns all events for a specific player in a game.
     * Filters out events that are not visible to the player.
     * Only includes root events.
     * By default, this is not sorted. Use the pageable to sort the events.
     *
     * @param gameId   The game id
     * @param playerId The player id
     * @param pageable The pageable to use for pagination and sorting
     * @return The events
     */
    @Query("SELECT e FROM GameEventEntity e WHERE e.gameId = :gameId " +
           "AND NOT e.visibility = 'ADMIN' " +
           "AND (e.visibility = 'GAME' OR e.playerId = :playerId) " +
           "AND e.parentEvent IS NULL")
    Page<GameEventEntity> findAllForPlayer(UUID gameId, UUID playerId, Pageable pageable);

    /**
     * Returns all events for a game. Only includes root events.
     *
     * @param gameId   The game id
     * @param pageable The pageable to use for pagination and sorting
     * @return The events
     */
    @Query("SELECT e FROM GameEventEntity e WHERE e.gameId = :gameId " +
           "AND e.parentEvent IS NULL")
    Page<GameEventEntity> findAllForGame(UUID gameId, Pageable pageable);
}
