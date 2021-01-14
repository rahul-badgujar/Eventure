package com.teamsar.eventure.exceptions.auth_exceptions;

import androidx.annotation.Nullable;

public abstract class AuthException extends Exception {

    static final String DEFAULT_ERROR_MESSAGE="Unknown reason";

    private String message;

    public AuthException() {
        this(DEFAULT_ERROR_MESSAGE);
    }

    public AuthException(String message) {
        this.message=message;
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.getClass().getSimpleName()+": "+this.message;
    }
}
