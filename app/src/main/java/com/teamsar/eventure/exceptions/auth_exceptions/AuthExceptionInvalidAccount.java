package com.teamsar.eventure.exceptions.auth_exceptions;

public class AuthExceptionInvalidAccount extends AuthException{
    static final String DEFAULT_ERROR_MESSAGE="Invalid account";

    public AuthExceptionInvalidAccount() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public AuthExceptionInvalidAccount(String message) {
        super(message);
    }
}
