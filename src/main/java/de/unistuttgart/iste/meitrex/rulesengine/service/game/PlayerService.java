package de.unistuttgart.iste.meitrex.rulesengine.service.game;

import de.unistuttgart.iste.meitrex.rulesengine.dto.game.CreatePlayerDto;
import de.unistuttgart.iste.meitrex.rulesengine.dto.game.PlayerDto;
import de.unistuttgart.iste.meitrex.rulesengine.exception.ResourceAlreadyExistsException;
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

        if (playerRepository.existsById(playerId)) {
            throw new ResourceAlreadyExistsException(
                    "Player %s in game %s already exists".formatted(playerId.getUserId(), gameId));
        }

        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(playerId)
                .name(playerDto.getName())
                .build();

        playerEntity = playerRepository.save(playerEntity);

        // TODO create log event that player joined

        return PlayerDto.from(playerEntity);
    }

    public List<PlayerDto> getAllPlayersOfGame(UUID gameId) {
        return playerRepository.findAllByIdGameId(gameId).stream()
                .map(PlayerDto::from)
                .toList();
    }

    public PlayerDto getPlayer(UUID gameId, UUID playerId) {
        PlayerId id = new PlayerId(gameId, playerId);
        return PlayerDto.from(playerRepository.findByIdOrThrow(id));
    }

    public void deletePlayer(UUID gameId, UUID playerId) {
        playerRepository.deleteById(new PlayerId(gameId, playerId));
    }
}
