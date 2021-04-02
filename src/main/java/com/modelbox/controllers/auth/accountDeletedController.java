package com.modelbox.controllers.auth;

import com.modelbox.app;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;

public class accountDeletedController {

    @FXML private Button createNewAccountBtn;

    /**
     * Handles the UI redirect to the create account view
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void createNewAccountBtnClicked(Event event) {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/createAccount.fxml"));
            Parent root = app.viewLoader.load();
            app.createAccountView = app.viewLoader.getController();
            createNewAccountBtn.getScene().setRoot(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

}
