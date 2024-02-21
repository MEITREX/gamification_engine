package de.unistuttgart.iste.meitrex.rulesengine.model.action;

import de.unistuttgart.iste.meitrex.rulesengine.model.event.EventType;
import de.unistuttgart.iste.meitrex.rulesengine.model.game.Game;
import de.unistuttgart.iste.meitrex.rulesengine.model.game.Player;

import java.util.Optional;

public interface EventContext {

    Object getData();

    <A extends EventType> A getEventType();

    Game getGame();

    Optional<Player> getPlayer();
}
