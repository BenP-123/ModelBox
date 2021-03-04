package com.modelbox.auth;

import org.graalvm.polyglot.Context;

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
    public int createNewUser(String emailAddress, String password){

        try (Context context = Context.create()) {
            context.eval("js", "print('Hello JavaScript!');");
        }

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
