package de.unistuttgart.iste.meitrex.rulesengine.service;

import de.unistuttgart.iste.meitrex.rulesengine.dto.GameEventTypeDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameEventTypeService {
    public GameEventTypeDto createGameEventType(GameEventTypeDto gameEventTypeDto) {
        return gameEventTypeDto;
    }


    public List<GameEventTypeDto> getAllGameEventTypes() {
        return List.of();
    }

    public GameEventTypeDto getGameEventTypeById(String id) {
        return GameEventTypeDto.builder().identifier(id).build();
    }

    public GameEventTypeDto updateGameEventType(String id, GameEventTypeDto gameEventTypeDto) {
        return gameEventTypeDto.withIdentifier(id);
    }

    public void deleteGameEventType(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean existsGameEventType(String id) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
