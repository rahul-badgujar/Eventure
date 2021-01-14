package com.teamsar.eventure.exceptions.auth_exceptions;

public class AuthExceptionSignInCurrentlyInProgress extends AuthException {
    static final String DEFAULT_ERROR_MESSAGE="Sign in currently in progress";

    public AuthExceptionSignInCurrentlyInProgress() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public AuthExceptionSignInCurrentlyInProgress(String message) {
        super(message);
    }
}
