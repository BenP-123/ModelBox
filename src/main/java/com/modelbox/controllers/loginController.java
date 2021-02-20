package com.modelbox.controllers;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.stage.Stage;

import static javafx.scene.control.PopupControl.USE_COMPUTED_SIZE;

public class loginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private Button loginBtn;
    @FXML private Button forgotPassBtn;
    @FXML private Button createAccountBtn;

    /**
     * Verifies a ModelBox user using the facilities provided from the auth package and redirects the UI
     * to the dashboard. The dashboard.fxml document is loaded and set as the root node of the current scene.
     * <p>
     * Note: The database is not yet connected to the application. Therefore, no actual account verification occurs.
     *
     * @param  e    a JavaFX event with the properties and methods of the element that triggered the event
     * @return      nothing of value is returned
     */
    @FXML
    private void loginBtnClicked(Event e) {
        // Automatically load dashboard regardless of whether an account was successfully verified or not
        dashboardController dashController = new dashboardController();
        FXMLLoader dashboardLoader = new FXMLLoader();
        dashboardLoader.setController(dashController);

        try {
            Parent root = dashboardLoader.load(getClass().getResource("/views/dashboard.fxml"));
            ((Stage) loginBtn.getParent().getScene().getWindow()).setMaxWidth(USE_COMPUTED_SIZE);
            ((Stage) loginBtn.getParent().getScene().getWindow()).setMaxHeight(USE_COMPUTED_SIZE);
            loginBtn.getParent().getScene().setRoot(root);
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
            forgotPassBtn.getParent().getScene().setRoot(root);
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
            createAccountBtn.getParent().getScene().setRoot(root);
        } catch (Exception fxmlLoadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }
}
