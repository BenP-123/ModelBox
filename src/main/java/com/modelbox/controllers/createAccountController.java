package com.modelbox.controllers;

import com.modelbox.auth.createAccount;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


public class createAccountController {

    public static createAccount activeCreateAccount;

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private PasswordField confirmPassField;
    @FXML private Button createAccountBtn;
    @FXML private Button loginBtn;
    @FXML private Pane createAccountErrorPopout;
    @FXML private TextField createAccountErrorTxtField;

    /**
     * Creates a new ModelBox user using the facilities provided from the auth package and redirects the UI
     * to the dashboard. The dashboard.fxml document is loaded and set as the root node of the current scene.
     * <p>
     * Note: The database is not yet connected to the application. Therefore, no actual account creation occurs.
     *
     * @param  e    a JavaFX event with the properties and methods of the element that triggered the event
     * @return      nothing of value is returned
     */
    @FXML
    private void createAccountBtnClicked(Event e) {

        // Load and show the login fxml document if the user already has an account
        try {
            activeCreateAccount = new createAccount();
            activeCreateAccount.setFirstNameField(firstNameField == null ? "" : firstNameField.getText());
            activeCreateAccount.setLastNameField(lastNameField == null ? "" : lastNameField.getText());
            activeCreateAccount.setEmailAddress(emailField == null ? "" : emailField.getText());
            activeCreateAccount.setPassword(passField == null ? "" : passField.getText());
            activeCreateAccount.setconfirmPassField(confirmPassField == null ? "" : confirmPassField.getText());

            if(activeCreateAccount.areRequiredFieldsMet() && activeCreateAccount.doPasswordsMatch() && activeCreateAccount.isEmailValid()){

                // Do NOT show error pop up
                createAccountErrorPopout.setVisible(false);

                if(activeCreateAccount.createNewUser(activeCreateAccount.getEmailAddress(), activeCreateAccount.getPassword()) == 0){

                } else {
                    // Clear the createAccount fields
                    firstNameField.setText("");
                    lastNameField.setText("");
                    emailField.setText("");
                    passField.setText("");
                    confirmPassField.setText("");
                }
            } else{
                //Show error pop up
                if(!(activeCreateAccount.areRequiredFieldsMet())) {
                    createAccountErrorTxtField.setText("Required fields are not met. Please fill in the required fields.");
                    createAccountErrorPopout.setVisible(true);
                }
                else if(!(activeCreateAccount.doPasswordsMatch()) && !(activeCreateAccount.isEmailValid())){
                    createAccountErrorTxtField.setText("Passwords do not match and the provided email address is invalid.");
                    createAccountErrorPopout.setVisible(true);
                }
                else if(!(activeCreateAccount.doPasswordsMatch())){
                    createAccountErrorTxtField.setText("Passwords do not match!");
                    createAccountErrorPopout.setVisible(true);
                }
                else if(!(activeCreateAccount.isEmailValid())){
                    createAccountErrorTxtField.setText("Email is not a valid email address.");
                    createAccountErrorPopout.setVisible(true);
                }
            }

        } catch(Exception exception){
            // Handle errors
            System.err.println(exception);
        }
    }

    /**
     * The application UI is redirected to the login view by loading and setting the login.fxml document as
     * the root node of the current scene.
     *
     * @param  e    a JavaFX event with the properties and methods of the element that triggered the event
     * @return      nothing of value is returned
     */
    @FXML
    private void loginBtnClicked(Event e) {
        // Load and show the login fxml document if the user already has an account
        loginController signInController = new loginController();
        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setController(signInController);

        try {
            Parent root = loginLoader.load(getClass().getResource("/views/login.fxml"));
            loginBtn.getScene().setRoot(root);
        } catch (Exception fxmlLoadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }
}
