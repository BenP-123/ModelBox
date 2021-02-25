package com.modelbox.auth;

public final class forgotPassword {

    /**
     * Using MongoDB Realm, the user's password is reset by sending an email to the user's email
     * address - which is stored in the database.
     *
     * @return       0 on success, -1 on error
     */
    public static int resetPassword(){
        // Need to implement

        return 0;
    }

    /**
     * Verify if an email was provided and if the email provided matches one that is on record
     * in the database
     *
     * @return       0 on success, -1 on error
     */
    private int isEmailValid(){
        // Need to implement

        return 0;
    }
}
