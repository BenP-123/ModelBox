package com.modelbox.controllers;

import com.modelbox.auth.logIn;
import com.modelbox.databaseIO.usersIO;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class loginController {

    public static logIn activeLogin = new logIn();
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private Button loginBtn;
    @FXML private Button forgotPassBtn;
    @FXML private Button createAccountBtn;
    @FXML private AnchorPane loginAnchorPane;

    /**
     * Verifies a ModelBox user using the facilities provided from the auth package and redirects the UI
     * to the dashboard. The dashboard.fxml document is loaded and set as the root node of the current scene.
     *
     * @param  e    a JavaFX event with the properties and methods of the element that triggered the event
     * @return      nothing of value is returned
     */
    @FXML
    private void loginBtnClicked(Event e) {
        try {
            activeLogin.setPassword(passField.getText());
            activeLogin.setEmailAddress(emailField.getText());
            activeLogin.logUserIn();

            dashboardController dashController = new dashboardController();
            FXMLLoader dashboardLoader = new FXMLLoader();
            dashboardLoader.setController(dashController);
            Parent root = dashboardLoader.load(getClass().getResource("/views/dashboard.fxml"));
            loginBtn.getScene().setRoot(root);
        } catch (Exception fxmlLoadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }

    /**
     * Verifies a ModelBox user using the facilities provided from the auth package and redirects the UI
     * to the dashboard. The dashboard.fxml document is loaded and set as the root node of the current scene.
     *
     * @param  event  a JavaFX event with the properties and methods of the element that triggered the event
     * @return        nothing of value is returned
     */
    @FXML
    private void loginEnterKeyPressed(KeyEvent event) {
        try {
            if (event.getCode().equals(KeyCode.ENTER)) {
                activeLogin.setPassword(passField.getText());
                activeLogin.setEmailAddress(emailField.getText());
                activeLogin.logUserIn();

                dashboardController dashController = new dashboardController();
                FXMLLoader dashboardLoader = new FXMLLoader();
                dashboardLoader.setController(dashController);
                Parent root = dashboardLoader.load(getClass().getResource("/views/dashboard.fxml"));
                loginBtn.getScene().setRoot(root);
            }
        } catch (Exception fxmlLoadException){
            // Handle exception if fxml document fails to load and show properly
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
    private void forgotPassBtnClicked(Event e) {
        // Load and show the login fxml document if the user already has an account
        forgotPasswordController forgotPassController = new forgotPasswordController();
        FXMLLoader forgotPassLoader = new FXMLLoader();
        forgotPassLoader.setController(forgotPassController);

        try {
            Parent root = forgotPassLoader.load(getClass().getResource("/views/forgotPassword.fxml"));
            forgotPassBtn.getScene().setRoot(root);
        } catch (Exception fxmlLoadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }

    /**
     * The application UI is redirected to the create account view by loading and setting the createAccount.fxml
     * document as the root node of the current scene.
     *
     * @param  e    a JavaFX event with the properties and methods of the element that triggered the event
     * @return      nothing of value is returned
     */
    @FXML
    private void createAccountBtnClicked(Event e) {
        // Load and show the create account fxml document
        createAccountController createController = new createAccountController();
        FXMLLoader createAccountLoader = new FXMLLoader();
        createAccountLoader.setController(createController);

        try {
            Parent root = createAccountLoader.load(getClass().getResource("/views/createAccount.fxml"));
            createAccountBtn.getScene().setRoot(root);
        } catch (Exception fxmlLoadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }
}
