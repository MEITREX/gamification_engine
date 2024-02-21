package de.unistuttgart.iste.meitrex.rulesengine.model.action;

public interface ActionExecutor {

    void executeAction(EventContext actionContext);


    ActionExecutor NOP = actionContext -> {
        // do nothing
    };
}
