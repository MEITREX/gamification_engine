package de.unistuttgart.iste.meitrex.rulesengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception to be thrown when a resource already exists, but it is tried to be created again.
 * Will be translated to a 400 Bad Request response.
 */
public class ResourceAlreadyExistsException extends ResponseStatusException {
    public ResourceAlreadyExistsException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }

    public ResourceAlreadyExistsException(String resourceName, Object id) {
        this(resourceName + " with id " + id + " already exists.");
    }

    public ResourceAlreadyExistsException(Class<?> resourceClass, Object id) {
        this(resourceClass.getSimpleName() + " with id " + id + " already exists.");
    }
}
