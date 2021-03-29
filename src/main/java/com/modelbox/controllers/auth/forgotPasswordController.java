package com.modelbox.controllers.auth;

import com.modelbox.app;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class forgotPasswordController {

    @FXML public TextField emailField;
    @FXML private Button loginBtn;
    @FXML public AnchorPane sendResetEmailPrompt;
    @FXML public AnchorPane passwordResetInstructions;
    @FXML public TextField forgotPasswordErrorField;

    /**
     * Handles resetting the user's password when the reset password button is clicked
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void resetPassBtnClicked(Event event) {
        resetUserPassword();
    }

    /**
     * Handles resetting the user's password when the enter key is pressed on the last field
     *
     * @param  event  a JavaFX KeyEvent
     */
    @FXML
    private void resetPassEnterKeyPressed(KeyEvent event) {
        if(event.getCode().equals((KeyCode.ENTER))) {
            resetUserPassword();
        }
    }

    /**
     * Attempts to reset the user's password using the information provided in the forgotPassword view
     * and modifies the view accordingly to handle errors
     *
     */
    private void resetUserPassword() {
        try {
            String functionCall = String.format("ModelBox.Authentication.sendPasswordResetEmail('%s');", emailField.getText());
            app.mongoApp.eval(functionCall);
        } catch(Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the log in view
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void loginBtnClicked(Event event) {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/login.fxml"));
            Parent root = app.viewLoader.load();
            loginBtn.getScene().setRoot(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

}
