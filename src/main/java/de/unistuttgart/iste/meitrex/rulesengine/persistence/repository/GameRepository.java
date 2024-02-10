package de.unistuttgart.iste.meitrex.rulesengine.persistence.repository;

import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.GameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface GameRepository extends JpaRepository<GameEntity, UUID> {

}
