package com.teamsar.eventure.exceptions.auth_exceptions;

public class AuthExceptionNetworkError extends AuthException{
    static final String DEFAULT_ERROR_MESSAGE="Network error";

    public AuthExceptionNetworkError() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public AuthExceptionNetworkError(String message) {
        super(message);
    }
}
