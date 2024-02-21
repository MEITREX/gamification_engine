package de.unistuttgart.iste.meitrex.rulesengine.persistence.repository;

import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.PlayerEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.PlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.UUID;

import static de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException.notFoundException;

@Repository
public interface PlayerRepository extends JpaRepository<PlayerEntity, PlayerId> {
    Collection<PlayerEntity> findAllByIdGameId(UUID gameId);

    default PlayerEntity findByIdOrThrow(PlayerId id) {
        return findById(id)
                .orElseThrow(() -> notFoundException(
                        "Player %s in game %s not found".formatted(id.getUserId(), id.getGameId())));
    }
}
