package com.teamsar.eventure.exceptions.auth_exceptions;

public class AuthExceptionInternalError extends AuthException {
    static final String DEFAULT_ERROR_MESSAGE="Internal error";

    public AuthExceptionInternalError() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public AuthExceptionInternalError(String message) {
        super(message);
    }
}
