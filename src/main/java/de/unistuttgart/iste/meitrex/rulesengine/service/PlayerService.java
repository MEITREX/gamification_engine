package de.unistuttgart.iste.meitrex.rulesengine.service;

import de.unistuttgart.iste.meitrex.rulesengine.dto.CreatePlayerDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.PlayerDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PlayerService {
    public PlayerDto createPlayer(CreatePlayerDto playerDto) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public List<PlayerDto> getAllPlayers(UUID gameId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public PlayerDto getPlayer(UUID gameId, UUID playerId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public void deletePlayer(UUID gameId, UUID playerId) {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
