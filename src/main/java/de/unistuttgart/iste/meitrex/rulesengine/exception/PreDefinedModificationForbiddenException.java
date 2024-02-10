package de.unistuttgart.iste.meitrex.rulesengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PreDefinedModificationForbiddenException extends ResponseStatusException {

    public PreDefinedModificationForbiddenException(String reason) {
        super(HttpStatus.FORBIDDEN, reason);
    }

    public PreDefinedModificationForbiddenException(String resourceName, Object id) {
        this(resourceName + " with id " + id + " is predefined and cannot be modified.");
    }
}
