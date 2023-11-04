package com.users.api.exception;


import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class InvalidRoleException extends RuntimeException {
    private final String description;

    public InvalidRoleException(String message) {
        super(message);
        this.description = null;
    }

    public InvalidRoleException(String message, String description) {
        super(message);
        this.description = description;
    }

    public InvalidRoleException(String message, Throwable cause) {
        super(message, cause);
        this.description = null;
    }

    public InvalidRoleException(String message, String description, Throwable cause) {
        super(message, cause);
        this.description = description;
    }

    public InvalidRoleException(Throwable cause) {
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

