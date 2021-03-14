package com.modelbox.controllers;

import com.modelbox.auth.logIn;
import com.modelbox.databaseIO.modelsIO;
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

public class loginController {

    public static dashboardController dashboard;
    public static logIn activeLogin;
    private FXMLLoader dashboardLoader;
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private Button loginBtn;
    @FXML private Button forgotPassBtn;
    @FXML private Button createAccountBtn;
    @FXML private TextField loginErrorField;

    /**
     * Handles the user login when the login button is clicked
     *
     * @param  event    a JavaFX Event
     */
    @FXML
    private void loginBtnClicked(Event event) {
        loginCurrentUser();
    }

    /**
     * Handles the user login when the enter key is pressed on the last field
     *
     * @param  event  a JavaFX KeyEvent
     */
    @FXML
    private void loginEnterKeyPressed(KeyEvent event) {
        if(event.getCode().equals((KeyCode.ENTER))) {
            loginCurrentUser();
        }
    }

    /**
     * Logs the user in and redirects the user to the dashboard
     *
     */
    private void loginCurrentUser() {
        try {
            activeLogin = new logIn();
            activeLogin.setEmailAddress(emailField == null ? "" : emailField.getText());
            activeLogin.setPassword(passField == null ? "" : passField.getText());

            if (activeLogin.didVerificationsPass()){
                loginErrorField.setVisible(false);

                // Attempt to log the user in
                if (activeLogin.logUserIn() == 0) {

                    // Redirect to dashboard
                    dashboardLoader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
                    Parent root = (Parent) dashboardLoader.load();
                    dashboard = dashboardLoader.getController();
                    loginBtn.getScene().setRoot(root);

                    // Show the my models view
                    dashboard.dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/myModels.fxml"));
                    Parent myModelsRoot = (Parent) dashboard.dashboardViewLoader.load();
                    dashboard.myModelsView = dashboard.dashboardViewLoader.getController();
                    dashboard.dashViewsAnchorPane.getChildren().setAll(myModelsRoot);

                    // Asynchronously populate the my models view and show appropriate nodes when ready
                    modelsIO.getAllModelsFromCurrentUser();

                } else {
                    loginErrorField.setText("Authentication unsuccessful. Try again!");
                    loginErrorField.setVisible(true);

                    // Clear the form and let the user try again
                    emailField.setText("");
                    passField.setText("");
                }

            } else {
                loginErrorField.setText(activeLogin.getLoginErrorMessage());
                loginErrorField.setVisible(true);
            }

        } catch(Exception e){
            // Handle errors
            e.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the forgot password view
     *
     * @param  event    a JavaFX Event
     */
    @FXML
    private void forgotPassBtnClicked(Event event) {
        FXMLLoader forgotPassLoader = new FXMLLoader();
        try {
            Parent root = forgotPassLoader.load(getClass().getResource("/views/forgotPassword.fxml"));
            forgotPassBtn.getScene().setRoot(root);
        } catch (Exception e){
            // Handle errors
            e.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the create account view
     *
     * @param  event    a JavaFX Event
     */
    @FXML
    private void createAccountBtnClicked(Event event) {
        FXMLLoader createAccountLoader = new FXMLLoader();
        try {
            Parent root = createAccountLoader.load(getClass().getResource("/views/createAccount.fxml"));
            createAccountBtn.getScene().setRoot(root);
        } catch (Exception e){
            // Handle errors
            e.printStackTrace();
        }
    }
}
