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

    /**
     * Creates a new user in the application database
     *
     * @return       0 on success, -1 on error
     */
    public int createNewUser(String emailAddress, String password) throws IOException {

        //For Debugging
        System.out.println("In CreateNewUser...");
        //End Debugging

        Context polyglot = Context.create("js");
        File createUsr = new File(Objects.requireNonNull(getClass().getClassLoader().getResource("scripts/createAccount.js")).getFile());
        polyglot.eval(Source.newBuilder("js", createUsr).build());

        //Language ID is 'js' and function called is 'registerAccount'
        Value createNewUser = polyglot.getBindings("js").getMember("registerAccount");

        //Function call passing email and password, tested and passes both email and password to the function properly...
        Value createNewUserResult = createNewUser.execute(this.getEmailAddress(), this.getPassword());

        return 0;
    }

    public void setFirstNameField(String firstNameField)
    {
        firstName = firstNameField;
    }
    public void setLastNameField(String lastNameField)
    {
        lastName = lastNameField;
    }
    public void setEmailAddress(String email)
    {
        emailAddress = email;
    }
    public void setPassword(String pass)
    {
        password = pass;
    }
    public void setconfirmPassField(String confirmPass)
    {
        confirmPassword = confirmPass;
    }


    public String getFirstName()
    {
        return firstName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public String getEmailAddress()
    {
        return emailAddress;
    }
    public String getPassword()
    {
        return password;
    }
    public String getConfirmPassword()
    {
        return confirmPassword;
    }

    /**
     * Verifies that all the fields on the create account form have been provided
     *
     * @return       0 on success, -1 on error
     */
    public boolean areRequiredFieldsMet()
    {
        if((getFirstName() != "") && (getLastName() != "") && (getEmailAddress() != "") && (getPassword() != "") && (getConfirmPassword() != "")) {
            return true;
        } else {
            return false;
        }

    }

    public boolean isEmailValid()
    {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return getEmailAddress().matches(regex);
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
    public boolean doPasswordsMatch() {
        if(getPassword().equals(getConfirmPassword())){
            return true;
        } else {
            return false;
        }
    }
}
