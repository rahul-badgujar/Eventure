package com.teamsar.eventure.exceptions.auth_exceptions;

import androidx.annotation.Nullable;

public class AuthExceptionNoGoogleAccountSignedIn extends AuthException {

    static final String DEFAULT_ERROR_MESSAGE="No google account signed in";

    public AuthExceptionNoGoogleAccountSignedIn() {
        super(DEFAULT_ERROR_MESSAGE);
    }

    public AuthExceptionNoGoogleAccountSignedIn(String message) {
        super(message);
    }

    @Nullable
    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
