package com.modelbox.controllers.auth;

import com.modelbox.app;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import javax.swing.*;

public class loginController {

    @FXML public TextField loginErrorField;
    @FXML public TextField emailField;
    @FXML public PasswordField passField;
    @FXML private Button forgotPassBtn;
    @FXML private Button createAccountBtn;
    @FXML private CheckBox checkBox;
    @FXML private TextField passwordPlainTxt;


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
    public void loginCurrentUser() {
        try {
            String functionCall = String.format("ModelBox.Authentication.logInCurrentUser('%s', '%s');", emailField.getText(), passField.getText());
            app.mongoApp.eval(functionCall);
        } catch(Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the forgot password view
     *
     * @param  event    a JavaFX Event
     */
    @FXML
    private void forgotPassBtnClicked(Event event) {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/forgotPassword.fxml"));
            Parent root = app.viewLoader.load();
            app.forgotPasswordView = app.viewLoader.getController();
            forgotPassBtn.getScene().setRoot(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the create account view
     *
     * @param  event    a JavaFX Event
     */
    @FXML
    private void createAccountBtnClicked(Event event) {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/createAccount.fxml"));
            Parent root = app.viewLoader.load();
            app.createAccountView = app.viewLoader.getController();
            createAccountBtn.getScene().setRoot(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }


    /**
     * Toggles the visibility of the entered password from masked to plain text.
     *
     * @param  actionEvent   a JavaFX Event
     */
    public void checkBoxSelected(ActionEvent actionEvent) {
            if (checkBox.isSelected()) {
                passwordPlainTxt.setText(passField.getText());
                passwordPlainTxt.setVisible(true);
                passField.setVisible(false);
                return;
            }
            passField.setText(passwordPlainTxt.getText());
            passField.setVisible(true);
            passwordPlainTxt.setVisible(false);
    }

}
