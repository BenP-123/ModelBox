package com.modelbox.auth;

import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public final class forgotPassword {

    private String forgotPasswordErrorMessage;
    private String emailAddress;

    /**
     * Sends a password reset email to the current app user
     *
     * @return 0 on success, -1 on error
     */
    public int sendPasswordResetEmail(){
        try {
            WebView browser = new WebView();
            WebEngine engine = browser.getEngine();

            engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    String functionCall = "sendResetEmail(\"" + emailAddress + "\");";
                    engine.executeScript(functionCall);
                }
            });

            // Load the Realm Web SDK and Reset Password Script
            engine.load("https://modelbox-vqzyc.mongodbstitch.com/forgot-password/index.html");

            return 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    //********************************************** VERIFICATION/CHECK METHODS **************************************//

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
        return !(emailAddress.equals(""));
    }

    //************************************************** GETTER METHODS **********************************************//

    /**
     * Gets the forgot password error of the current app user
     *
     * @return a String containing the forgot password error message that should be shown to the user
     */
    public String getForgotPasswordErrorMessage() {
        return forgotPasswordErrorMessage;
    }

    /**
     * Gets the email address provided by the app user
     *
     * @return a String containing the email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    //************************************************** SETTER METHODS **********************************************//

    /**
     * Sets the password reset email address for the current user
     *
     * @param email a String containing the new email address to use for the user
     */
    public void setEmailAddress(String email) {
        emailAddress = email;
    }

}
