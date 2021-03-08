package com.modelbox.auth;

public final class forgotPassword {

    private String forgotPasswordErrorMessage;
    private String emailAddress;

    /**
     * Sends a password reset email to the current app user (someone not logged in)
     *
     * @return 0 on success, -1 on error
     */
    public int sendPasswordResetEmail(){
        // Need to implement

        return 0;
    }

    /*********************************************** VERIFICATION/CHECK METHODS ***************************************/

    /**
     * Verifies that all the forgot password form checks have succeeded
     *
     * @return true on success, false on error
     */
    public boolean didVerificationsPass() {
        if (areRequiredFieldsMet()) {
            return true;
        } else {
            forgotPasswordErrorMessage = "Required fields must be provided! Please fill in the required fields.";
            return false;
        }
    }

    /**
     * Verifies that all the fields on the forgot password view have been provided
     *
     * @return true on success, false on error
     */
    public boolean areRequiredFieldsMet() {
        if(!(emailAddress.equals(""))) {
            return true;
        } else{
            return false;
        }
    }

    /*************************************************** GETTER METHODS ***********************************************/

    /**
     * Gets the forgot password error of the current app user
     *
     * @return a string containing the forgot password error message that should be shown to the user
     */
    public String getForgotPasswordErrorMessage() {
        return forgotPasswordErrorMessage;
    }

    /**
     * Gets the email address provided by the app user
     *
     * @return a string containing the email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /*************************************************** SETTER METHODS ***********************************************/

    /**
     * Sets the password reset email address for the current user
     *
     * @return void
     */
    public void setEmailAddress(String email) {
        emailAddress = email;
    }


}
