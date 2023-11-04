package com.users.api.exception;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class PersistResourceException extends RuntimeException {
    private final String description;

    public PersistResourceException(String message) {
        super(message);
        this.description = null;
    }

    public PersistResourceException(String message, String description) {
        super(message);
        this.description = description;
    }

    public PersistResourceException(String message, Throwable cause) {
        super(message, cause);
        this.description = null;
    }

    public PersistResourceException(String message, String description, Throwable cause) {
        super(message, cause);
        this.description = description;
    }

    public PersistResourceException(Throwable cause) {
        super(cause);
        this.description = null;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        String className = getClass().getName();
        String message = getLocalizedMessage();
        String desc = getDescription();

        return className + ((StringUtils.isBlank(message) && StringUtils.isBlank(desc)) ? ""
                : ": " + StringUtils.join(Arrays.asList(message, desc), " > "));
    }

    private static final long serialVersionUID = 2841115634148003349L;
}
