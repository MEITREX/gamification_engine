package de.unistuttgart.iste.meitrex.rulesengine.service;

import de.unistuttgart.iste.meitrex.rulesengine.dto.CreateOrUpdateGameDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.GameDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GameService {
    public GameDto createGame(CreateOrUpdateGameDto gameDto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<GameDto> getAllGames() {
        return List.of();
    }

    public GameDto getGameById(UUID id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }


    public boolean existsGame(UUID id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public GameDto updateGame(UUID id, CreateOrUpdateGameDto gameDto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deleteGame(UUID id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
