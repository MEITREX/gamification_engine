package de.unistuttgart.iste.meitrex.rulesengine.model.event;

import de.unistuttgart.iste.meitrex.rulesengine.model.action.ActionExecutor;
import org.apache.naming.factory.BeanFactory;

import java.util.function.Function;
import java.util.function.Supplier;

public enum ActionOnEvent {

    NONE;

    public static final String NONE_DB_NAME = "NONE";


    private final Function<BeanFactory, ActionExecutor> executorFactory;

    ActionOnEvent(Function<BeanFactory, ActionExecutor> executorFactory) {
        this.executorFactory = executorFactory;
    }

    ActionOnEvent(Supplier<ActionExecutor> executorSupplier) {
        this(beanFactory -> executorSupplier.get());
    }

    ActionOnEvent() {
        this(() -> ActionExecutor.NOP);
    }

    public ActionExecutor getExecutor(BeanFactory beanFactory) {
        return executorFactory.apply(beanFactory);
    }
}
