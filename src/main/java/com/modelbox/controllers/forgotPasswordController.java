package com.modelbox.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class forgotPasswordController {
    @FXML private TextField emailField;
    @FXML private Button resetPassBtn;

    /**
     * Sends an email to a ModelBox user giving them the ability to reset their account password and redirects the UI
     * to the login view. The login.fxml document is loaded and set as the root node of the current scene.
     * <p>
     * Note: As of right now, no email is sent.
     *
     * @param  e    a JavaFX event with the properties and methods of the element that triggered the event
     * @return      nothing of value is returned
     */
    @FXML
    private void resetPassBtnClicked(Event e) {
        // Automatically load log in view
        loginController signInController = new loginController();
        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setController(signInController);

        try {
            Parent root = loginLoader.load(getClass().getResource("/views/login.fxml"));
            resetPassBtn.getParent().getScene().setRoot(root);
        } catch (Exception fxmlLoadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }

}
