package de.unistuttgart.iste.meitrex.rulesengine.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Helper class to define responses in openapi
 */
@Data
@AllArgsConstructor
public class SpringErrorPayload {
    private String message;
    private String error;
    private String path;
    private int status;
    private String timestamp;
    private String trace;
}
