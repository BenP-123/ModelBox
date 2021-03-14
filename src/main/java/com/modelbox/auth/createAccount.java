package com.modelbox.auth;

import javafx.concurrent.Worker;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.util.prefs.Preferences;

public final class createAccount {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;
    private String confirmPassword;
    private String createAccountErrorMessage;

    /**
     * Creates a new user in the MongoDB database for the app
     *
     * @return 0 on success, -1 on error
     */
    public int createNewUser() {

        try {
            WebView browser = new WebView();
            WebEngine engine = browser.getEngine();

            engine.getLoadWorker().stateProperty().addListener((obs, oldValue, newValue) -> {
                if (newValue == Worker.State.SUCCEEDED) {
                    String functionCall = "createAccount(\"" + emailAddress + "\", \"" + password + "\");";
                    engine.executeScript(functionCall);
                }
            });

            // Load the Realm Web SDK and Create Account Script
            engine.load("https://modelbox-vqzyc.mongodbstitch.com/create-account/index.html");

            // Save the other create account fields so they can be added to the users custom_data object
            Preferences prefs = Preferences.userRoot().node("/com/modelbox");
            prefs.put("displayName", firstName + " " + lastName);
            prefs.put("firstName", firstName);
            prefs.put("lastName", lastName);

            return 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    //********************************************** VERIFICATION/CHECK METHODS **************************************//

    /**
     * Verifies that all the create account form checks have succeeded
     *
     * @return true on success, false on error
     */
    public boolean didVerificationsPass() {
        if (!areRequiredFieldsMet()) {
            createAccountErrorMessage = "Required fields must be provided! Please fill in the required fields.";
            return false;
        } else if (!doPasswordsMatch() && !isEmailValid()) {
            createAccountErrorMessage = "Passwords do not match. Provided email address is invalid.";
            return false;
        } else if (!doPasswordsMatch()) {
            createAccountErrorMessage = "Passwords do not match!";
            return false;
        } else if (!isEmailValid()) {
            createAccountErrorMessage = "Provided email address is invalid.";
            return false;
        } else if(!isEmailAlreadyInTheDatabase()){
            createAccountErrorMessage = "The provided email already has an account.";
            return false;
        } else {
            return true;
        }
    }

    /**
     * Verifies that all the fields on the create account view have been provided
     *
     * @return true on success, false on error
     */
    public boolean areRequiredFieldsMet() {
        return (!getFirstName().equals("")) && (!getLastName().equals(""))
                && (!getEmailAddress().equals(""))
                && (!getPassword().equals(""))
                && (!getConfirmPassword().equals(""));
    }

    /**
     * Verifies that the provided passwords match one another
     *
     * @return true on success, false on error
     */
    public boolean doPasswordsMatch() {
        return getPassword().equals(getConfirmPassword());
    }

    /**
     * Verifies that the provided email address is in the form of an actual email address
     *
     * @return true if email is satisfactory, false if email is not
     */
    public boolean isEmailValid() {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return getEmailAddress().matches(regex);
    }

    /**
     * Verifies that the provided email address does not already exist in the app database
     *
     * @return true if email exists, false if email doesn't exist
     */
    public boolean doesEmailAlreadyExist() {
        // Need to implement

        return true;
    }

    /**
     * Verifies that the email provided is not user in the database
     *
     * @return true on success, false on error
     */
    public boolean isEmailAlreadyInTheDatabase() {
        // Need to implement

        return true;
    }

    //************************************************** GETTER METHODS **********************************************//


    /**
     * Gets the create account error of the current app user
     *
     * @return a string containing the create account error message that should be shown to the user
     */
    public String getCreateAccountErrorMessage() {
        return createAccountErrorMessage;
    }

    /**
     * Gets the first name of the current app user
     *
     * @return a string containing the first name of the app user
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Gets the last name of the current app user
     *
     * @return a string containing the last name of the app user
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Gets the email address of the current app user
     *
     * @return a string containing the email address of the app user
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Gets the UI-entered password of the current app user
     *
     * @return a string containing the UI-entered password of the app user
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the UI-entered confirm password of the current app user
     *
     * @return a string containing the UI-entered confirm password of the app user
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    //************************************************** SETTER METHODS **********************************************//

    /**
     * Sets the first name for the current app user
     *
     * @param firstNameField a String containing the first name of the current app user
     */
    public void setFirstNameField(String firstNameField) {
        firstName = firstNameField;
    }

    /**
     * Sets the last name for the current app user
     *
     * @param lastNameField a String containing the last name of the current app user
     */
    public void setLastNameField(String lastNameField) {
        lastName = lastNameField;
    }

    /**
     * Sets the email address for the current app user
     *
     * @param email a String containing the email of the current app user
     */
    public void setEmailAddress(String email) {
        emailAddress = email;
    }

    /**
     * Sets the UI-entered password for the current app user
     *
     * @param pass a String containing the password of the current app user
     */
    public void setPassword(String pass) {
        password = pass;
    }

    /**
     * Sets the UI-entered confirm password for the current app user
     *
     * @param confirmPass a String containing the confirmed password of the current app user
     */
    public void setConfirmPassField(String confirmPass) {
        confirmPassword = confirmPass;
    }

}
