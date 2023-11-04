package com.users.api.exception;

/**
 * @author Reem Gharib
 */
public class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = -1L;

    public UserAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException(Throwable cause) {
        super(cause);
    }


}
