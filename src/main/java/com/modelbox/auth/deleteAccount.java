package com.modelbox.auth;

import com.modelbox.databaseIO.usersIO;
import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class deleteAccount {

    private String DeleteAccountErrorMessage;
    private String deletePassword;
    private String confirmDeletePassword;

    public int deleteUsersAccount() {
        try {
            WebView browser = new WebView();
            WebEngine engine = browser.getEngine();

            engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    //String functionCall = "sendDeleteAccountConfirmation(\"" + emailAddress + "\");";
                    //engine.executeScript(functionCall);
                }
            });

            // Load the Realm Web SDK and Delete Account Confirmation Script
            engine.load("https://modelbox-vqzyc.mongodbstitch.com/delete-account/index.html");

            return 0;
        } catch (Exception exception){
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
            DeleteAccountErrorMessage = "Required fields must be provided! Please fill in the required fields.";
            return false;
        } else if(!(doPasswordsMatch())){
            DeleteAccountErrorMessage = "Passwords do not match!";
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
        return (!getDeletePassword().equals(""))
                && (!getConfirmDeletePassword().equals(""));
    }

    /**
     * Verifies that the provided passwords match one another
     *
     * @return true on success, false on error
     */
    public boolean doPasswordsMatch() {
        return getDeletePassword().equals(getConfirmDeletePassword());
    }

    public String getDeleteAccountError() {
        return DeleteAccountErrorMessage;
    }

    /**
     * Gets the UI-entered password of the current app user
     *
     * @return a string containing the UI-entered password of the app user
     */
    public String getDeletePassword() {
        return deletePassword;
    }

    /**
     * Gets the UI-entered confirm password of the current app user
     *
     * @return a string containing the UI-entered confirm password of the app user
     */
    public String getConfirmDeletePassword() {
        return confirmDeletePassword;
    }

    /**
     * Sets the UI-entered password for the current app user
     *
     * @param pass a String containing the password of the current app user
     */
    public void setDeletePassword(String pass) {
        deletePassword = pass;
    }

    /**
     * Sets the UI-entered confirm password for the current app user
     *
     * @param confirmPass a String containing the confirmed password of the current app user
     */
    public void setConfirmDeletePassword(String confirmPass) {
        confirmDeletePassword = confirmPass;
    }
}
