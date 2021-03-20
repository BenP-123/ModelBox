package com.modelbox.auth;

import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class changeEmail {

    private String changeEmailErrorMessage;
    private String newEmail;

    public int sendChangeEmail(){
        try {
            WebView browser = new WebView();
            WebEngine engine = browser.getEngine();

            engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    String functionCall = "sendChangeEmail(\"" + newEmail + "\");";
                    engine.executeScript(functionCall);
                }
            });

            // Load the Realm Web SDK and Reset Password Script
            engine.load("https://modelbox-vqzyc.mongodbstitch.com/change-email/index.html");

            return 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    /**
     * Verifies that all the forgot password form checks have succeeded
     *
     * @return true on success, false on error
     */
    public boolean didVerificationsPass() {
        if (!(areRequiredFieldsMet())) {
            changeEmailErrorMessage = "Required fields must be provided! Please fill in the required fields.";
            return false;
        } else if(!(isEmailValid())){
            changeEmailErrorMessage = "Provided email address is invalid.";
            return false;
        } else{
            return true;
        }
    }

    /**
     * Verifies that all the fields on the forgot password view have been provided
     *
     * @return true on success, false on error
     */
    public boolean areRequiredFieldsMet() {
        return !(newEmail.equals(""));
    }

    /**
     * Verifies that the provided email address is in the form of an actual email address
     *
     * @return true if email is satisfactory, false if email is not
     */
    public boolean isEmailValid() {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return getNewEmail().matches(regex);
    }

    /**
     * Gets the forgot password error of the current app user
     *
     * @return a String containing the forgot password error message that should be shown to the user
     */
    public String getChangeEmailErrorMessage() {
        return changeEmailErrorMessage;
    }

    public String getNewEmail(){
        return newEmail;
    }

    /**
     * Sets the password reset email address for the current user
     *
     * @param email a String containing the new email address to use for the user
     */
    public void setEmailAddress(String email) {
        newEmail = email;
    }

}
