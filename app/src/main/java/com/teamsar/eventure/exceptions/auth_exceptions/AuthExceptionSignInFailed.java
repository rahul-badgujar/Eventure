package com.teamsar.eventure.exceptions.auth_exceptions;

public class AuthExceptionSignInFailed extends AuthException{
    static final String DEFAULT_ERROR_MESSAGE="Sign in attempt failed, declined from servers";

    public AuthExceptionSignInFailed() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public AuthExceptionSignInFailed(String message) {
        super(message);
    }
}
