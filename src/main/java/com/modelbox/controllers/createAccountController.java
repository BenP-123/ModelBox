package com.modelbox.controllers;

import com.modelbox.auth.createAccount;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;

public class createAccountController {

    public static createAccount activeCreateAccount;
    private FXMLLoader dashboardLoader;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private PasswordField confirmPassField;
    @FXML private Button loginBtn;
    @FXML private TextField createAccountErrorField;

    /**
     * Handles the creation of a user account when the create account button is clicked
     *
     * @param  event    a JavaFX Event
     * @return void
     */
    @FXML
    private void createAccountBtnClicked(Event event) {
        createNewUserAccount();
    }

    /**
     * Handles the creation of a user account when the enter key is pressed on the last field
     *
     * @param  event    a JavaFX KeyEvent
     * @return void
     */
    @FXML
    private void createAccountEnterKeyPressed(KeyEvent event) {
        if(event.getCode().equals((KeyCode.ENTER))) {
            createNewUserAccount();
        }
    }

    private void createNewUserAccount() {
        try {
            activeCreateAccount = new createAccount();
            activeCreateAccount.setFirstNameField(firstNameField == null ? "" : firstNameField.getText());
            activeCreateAccount.setLastNameField(lastNameField == null ? "" : lastNameField.getText());
            activeCreateAccount.setEmailAddress(emailField == null ? "" : emailField.getText());
            activeCreateAccount.setPassword(passField == null ? "" : passField.getText());
            activeCreateAccount.setConfirmPassField(confirmPassField == null ? "" : confirmPassField.getText());

            if(activeCreateAccount.didVerificationsPass()) {
                createAccountErrorField.setVisible(false);

                // Attempt to create the new user
                if(activeCreateAccount.createNewUser() == 0){
                    FXMLLoader confirmAccountLoader = new FXMLLoader();

                    try {
                        Parent root = confirmAccountLoader.load(getClass().getResource("/views/accountCreated.fxml"));
                        loginBtn.getScene().setRoot(root);
                    } catch (Exception exception){
                        // Handle errors
                        exception.printStackTrace();
                    }

                } else {
                    createAccountErrorField.setText("Attempt to create a new account was unsuccessful. Try again!");
                    createAccountErrorField.setVisible(true);

                    // Clear the form and let the user try again
                    firstNameField.setText("");
                    lastNameField.setText("");
                    emailField.setText("");
                    passField.setText("");
                    confirmPassField.setText("");
                }
            } else {
                createAccountErrorField.setText(activeCreateAccount.getCreateAccountErrorMessage());
                createAccountErrorField.setVisible(true);
            }

        } catch(Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the log in view
     *
     * @param  event a JavaFX Event
     * @return void
     */
    @FXML
    private void loginBtnClicked(Event event) {
        FXMLLoader loginLoader = new FXMLLoader();
        try {
            Parent root = loginLoader.load(getClass().getResource("/views/login.fxml"));
            loginBtn.getScene().setRoot(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }
}
