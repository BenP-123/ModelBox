package com.modelbox.auth;

import org.graalvm.polyglot.Context;

public final class createAccount {

    /**
     * Creates a new user in the application database
     *
     * @return       0 on success, -1 on error
     */
    public static int createNewUser(String emailAddress, String password){

        try (Context context = Context.create()) {
            context.eval("js", "print('Hello JavaScript!');");
        }

        return 0;
    }

    /**
     * Verifies that all the fields on the create account form have been provided
     *
     * @return       0 on success, -1 on error
     */
    private int areRequiredFieldsMet() {
        // Need to implement

        return 0;
    }

    /**
     * Verifies that the provided email does not already exist in the application's database
     *
     * @return       -1 if email does exist, 0 if email is okay to use
     */
    private int doesEmailAlreadyExist() {
        // Need to implement

        return 0;
    }

    /**
     * Verifies that the provided passwords match one another
     *
     * i.e: The 'Password' field matches the 'Confirm Password' field
     *
     * @return       0 on success, -1 on error
     */
    private int doPasswordsMatch() {
        // Need to implement

        return 0;
    }
}
