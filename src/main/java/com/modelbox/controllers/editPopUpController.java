package com.modelbox.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class editPopUpController {

    @FXML public StackPane editModelStackPane;
    @FXML public TextField modelNameTextField;
    @FXML public Text modelTypeText;

    /**
     * Closes and removes the edit pop-up from view
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void closeEditPaneBtnClicked(Event event) {
        AnchorPane currentEditPopUp = (AnchorPane) ((Button) event.getSource()).getParent().getParent();
        loginController.dashboard.verifyModelsView.verifyModelsAnchorPane.getChildren().remove(currentEditPopUp);
    }
}
