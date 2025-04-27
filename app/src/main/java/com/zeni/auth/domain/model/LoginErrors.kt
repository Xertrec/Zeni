package com.zeni.auth.domain.model

enum class LoginErrors {
    /**
     * Represents the absence of errors.
     */
    NONE,

    /**
     * Represents the error of an empty identifier.
     */
    EMPTY_IDENTIFIER,

    /**
     * Represents the error of an empty password.
     */
    EMPTY_PASSWORD,

    /**
     * Represents the error of an invalid identifier.
     */
    INVALID_IDENTIFIER,

    /**
     * Represents the error of an invalid password.
     */
    INVALID_PASSWORD,

    /**
     * Represents a general error.
     */
    FATAL_ERROR
}