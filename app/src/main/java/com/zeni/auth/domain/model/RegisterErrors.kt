package com.zeni.auth.domain.model

import com.zeni.R

enum class RegisterErrors {
    NONE,
    EMPTY_NAME,
    EMPTY_USERNAME,
    EMPTY_PASSWORD,
    TAKEN_USERNAME,
    INVALID_USERNAME,
    WEAK_PASSWORD,
    INVALID_PASSWORD,
    FATAL_ERROR;

    /**
     * Sealed class to register errors email related.
     */
    enum class RegisterEmailErrors(val errorRes: Int) {

        /**
         * Error when the email field is empty.
         */
        EMPTY(R.string.register_email_empty_error),

        /**
         * Error when the email is invalid, for example, when doesn't have an accepted format.
         */
        INVALID(R.string.register_email_invalid_error),

        /**
         * Error when the email is linked to another account.
         */
        TAKEN(R.string.register_email_taken_error),

        NOT_MATCH(R.string.change_password_email_not_match)
    }

    /**
     * Sealed class to register errors phone related.
     */
    enum class RegisterPhoneErrors(val errorRes: Int) {

        /**
         * Error when the phone field is empty.
         */
        EMPTY(R.string.register_phone_empty_error),

        /**
         * Error when the phone is invalid, for example, when doesn't have an accepted format.
         */
        INVALID(R.string.register_phone_invalid_error),

        /**
         * Error when the phone is linked to another account.
         */
        TAKEN(R.string.register_phone_taken_error)
    }

    /**
     * Sealed class to register errors username related.
     */
    enum class RegisterUsernameErrors(val errorRes: Int) {

        /**
         * Error when the username field is empty.
         */
        EMPTY(R.string.register_username_empty_error),

        /**
         * Error when the username already exists.
         */
        TAKEN(R.string.register_username_taken_error),
    }

    enum class RegisterBirthdateErrors(val errorRes: Int) {

        /**
         * Error when the birthdate field is empty.
         */
        EMPTY(R.string.register_birthdate_empty_error),

        /**
         * Error when the birthdate is invalid, for example, when doesn't have an accepted format.
         */
        INVALID(R.string.register_birthdate_invalid_error)
    }

    enum class RegisterAddressErrors(val errorRes: Int) {

        /**
         * Error when the address field is empty.
         */
        EMPTY(R.string.register_address_empty_error)
    }

    enum class RegisterCountryErrors(val errorRes: Int) {

        /**
         * Error when the country field is empty.
         */
        EMPTY(R.string.register_country_empty_error)
    }

    /**
     * Sealed class to register errors password related.
     */
    enum class RegisterPasswordErrors(val errorRes: Int) {

        /**
         * Error when the password field is empty.
         */
        EMPTY(R.string.register_password_empty_error),

        /**
         * Error when the password is too short, when has less than [PASSWORD_MIN_LENGTH] characters.
         */
        SHORT(R.string.register_password_short_error),

        /**
         * Error when the password is insecure.
         * TODO: Implement this error.
         */
        INSECURE(R.string.register_password_insecure_error)
    }
}