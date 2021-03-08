package com.modelbox.auth;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

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
            /*
            Context polyglot = Context.create("js");
            File createUsr = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("scripts/createAccount.js")).getFile());
            polyglot.eval(Source.newBuilder("js", createUsr).build());

            //Language ID is 'js' and function called is 'registerAccount'
            Value createNewUser = polyglot.getBindings("js").getMember("registerAccount");

            //Function call passing email and password, tested and passes both email and password to the function properly...
            Value createNewUserResult = createNewUser.execute(this.getEmailAddress(), this.getPassword());
            */
            return 0;
        } catch (Exception exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    /*********************************************** VERIFICATION/CHECK METHODS ***************************************/

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
        if((getFirstName() != "") && (getLastName() != "") && (getEmailAddress() != "") && (getPassword() != "")
                && (getConfirmPassword() != "")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Verifies that the provided passwords match one another
     *
     * @return true on success, false on error
     */
    public boolean doPasswordsMatch() {
        if(getPassword().equals(getConfirmPassword())){
            return true;
        } else {
            return false;
        }
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
    private boolean doesEmailAlreadyExist() {
        // Need to implement

        return true;
    }

    /*************************************************** GETTER METHODS ***********************************************/


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

    /*************************************************** SETTER METHODS ***********************************************/

    /**
     * Sets the first name for the current app user
     *
     * @return void
     */
    public void setFirstNameField(String firstNameField) {
        firstName = firstNameField;
    }

    /**
     * Sets the last name for the current app user
     *
     * @return void
     */
    public void setLastNameField(String lastNameField) {
        lastName = lastNameField;
    }

    /**
     * Sets the email address for the current app user
     *
     * @return void
     */
    public void setEmailAddress(String email) {
        emailAddress = email;
    }

    /**
     * Sets the UI-entered password for the current app user
     *
     * @return void
     */
    public void setPassword(String pass) {
        password = pass;
    }

    /**
     * Sets the UI-entered confirm password for the current app user
     *
     * @return void
     */
    public void setConfirmPassField(String confirmPass) {
        confirmPassword = confirmPass;
    }





}
