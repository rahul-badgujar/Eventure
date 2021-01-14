package com.teamsar.eventure.exceptions.auth_exceptions;

public class AuthExceptionSignInRequired extends AuthException {
    static final String DEFAULT_ERROR_MESSAGE="Sign in required for this account";

    public AuthExceptionSignInRequired() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public AuthExceptionSignInRequired(String message) {
        super(message);
    }
}
