package com.modelbox.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class accountCreatedController {

    @FXML
    private Button loginBtn;

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
