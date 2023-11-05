package com.users.api.handler;

import com.users.api.dto.ErrorResponse;
import com.users.api.exception.InvalidRoleException;
import com.users.api.exception.UserAlreadyExistsException;
import com.users.api.exception.UserNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Reem Gharib
 */
@Log4j2
@ControllerAdvice
public class RestExceptionHandler {

    @ResponseBody
    @ExceptionHandler({Throwable.class})
    public ResponseEntity<ErrorResponse> handleExceptions(Throwable e) {

        return new ResponseEntity<>(this.getErrorResponse("ERR_", "An error occurred, contact the admin to check server logs", e)
                , null,
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseBody
    @ExceptionHandler({InvalidRoleException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestExceptions(InvalidRoleException e) {

        return new ResponseEntity<>(this.getErrorResponse("ERR_", e.getMessage(), e), null, HttpStatus.BAD_REQUEST);
    }

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException e) {
        return new ResponseEntity<>(this.getErrorResponse("ERR_", e.getMessage(), e), null, HttpStatus.NOT_FOUND);
    }

    @ResponseBody
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ResponseEntity<>(this.getErrorResponse("ERR_", e.getMessage(), e), null, HttpStatus.CONFLICT);
    }

    /**
     * Get Error response
     *
     * @param code
     * @param message
     * @param e
     * @return ErrorResponse
     */
    protected ErrorResponse getErrorResponse(String code, String message, Throwable e) {

        return ErrorResponse.builder()
                .code(code)
                .description(StringUtils.isBlank(message) ? e.getMessage() : message)
                .reason(e.getClass().getSimpleName())
                .build();
    }
}
