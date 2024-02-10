package de.unistuttgart.iste.meitrex.rulesengine.service.game;

import de.unistuttgart.iste.meitrex.rulesengine.dto.game.CreatePlayerDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.PlayerDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceNotFoundException;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.PlayerEntity;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.entity.PlayerId;
import de.unistuttgart.iste.meitrex.rulesengine.persistence.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlayerService {

    private final PlayerRepository playerRepository;

    public PlayerDto createPlayer(UUID gameId, CreatePlayerDto playerDto) {
        PlayerId playerId = new PlayerId(gameId, playerDto.getUserId());

        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(playerId)
                .name(playerDto.getName())
                .build();

        playerEntity = playerRepository.save(playerEntity);

        return PlayerDto.from(playerEntity);
    }

    public List<PlayerDto> getAllPlayersOfGame(UUID gameId) {
        return playerRepository.findAllByIdGameId(gameId).stream()
                .map(PlayerDto::from)
                .toList();
    }

    public PlayerDto getPlayer(UUID gameId, UUID playerId) {
        return playerRepository.findById(new PlayerId(gameId, playerId))
                .map(PlayerDto::from)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Player %s in game %s not found".formatted(playerId, gameId)));
    }

    public void deletePlayer(UUID gameId, UUID playerId) {
        playerRepository.deleteById(new PlayerId(gameId, playerId));
    }
}
