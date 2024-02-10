package de.unistuttgart.iste.meitrex.rulesengine.service.game;

import de.unistuttgart.iste.meitrex.rulesengine.dto.game.CreateOrUpdateGameDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.GameDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.GameEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRepository gameRepository;

    public GameDto createGame(CreateOrUpdateGameDto gameDto) {
        GameEntity gameEntity = new GameEntity()
                .setName(gameDto.getName());

        gameEntity = gameRepository.save(gameEntity);

        return GameDto.from(gameEntity);
    }

    public List<GameDto> getAllGames() {
        return gameRepository.findAll().stream()
                .map(GameDto::from)
                .toList();
    }

    public GameDto getGameById(UUID id) {
        return gameRepository.findById(id)
                .map(GameDto::from)
                .orElseThrow(() -> new ResourceNotFoundException("Game", id));
    }

    public boolean existsGame(UUID id) {
        return gameRepository.existsById(id);
    }

    public GameDto updateOrCreateGame(UUID id, CreateOrUpdateGameDto gameDto) {
        GameEntity gameEntity = gameRepository.findById(id)
                .orElseGet(() -> new GameEntity().setId(id));

        gameEntity.setName(gameDto.getName());

        gameEntity = gameRepository.save(gameEntity);

        return GameDto.from(gameEntity);
    }

    public void deleteGame(UUID id) {
        gameRepository.deleteById(id);
    }
}
