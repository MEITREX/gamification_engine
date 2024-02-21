package de.unistuttgart.iste.meitrex.rulesengine.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

/**
 * Exception that is thrown when a resource is not found.
 * This exception is mapped to a 404 status code.
 */
public class ResourceNotFoundException extends ResponseStatusException {
    public ResourceNotFoundException(String entityName, Object id) {
        this("Resource " + entityName + " with id " + id + " not found");
    }

    public ResourceNotFoundException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }

    public static ResourceNotFoundException notFoundException(String entityName, Object id) {
        return new ResourceNotFoundException(entityName, id);
    }

    public static ResourceNotFoundException notFoundException(String reason) {
        return new ResourceNotFoundException(reason);
    }
}
