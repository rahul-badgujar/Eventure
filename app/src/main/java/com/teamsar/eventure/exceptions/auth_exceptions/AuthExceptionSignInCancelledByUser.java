package com.teamsar.eventure.exceptions.auth_exceptions;

import androidx.annotation.Nullable;

public class AuthExceptionSignInCancelledByUser extends AuthException{
    static final String DEFAULT_ERROR_MESSAGE="Sign in attempt cancelled by user";

    public AuthExceptionSignInCancelledByUser() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public AuthExceptionSignInCancelledByUser(String message) {
        super(message);
    }

}
