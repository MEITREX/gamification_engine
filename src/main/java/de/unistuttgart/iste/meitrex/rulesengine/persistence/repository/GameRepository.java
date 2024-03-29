package de.unistuttgart.iste.meitrex.rulesengine.persistence.repository;

import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException.notFoundException;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, UUID> {

    default GameEntity findByIdOrThrow(UUID id) {
        return findById(id).orElseThrow(() -> notFoundException("Game", id));
    }
}
