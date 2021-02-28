package com.modelbox.controllers;

import com.modelbox.databaseIO.modelsIO;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

public class previewPopUpController {

    @FXML public Group previewModelGroup;
    @FXML public AnchorPane previewModelAnchorPane;
    @FXML public Text modelNameText;
    @FXML public Text modelTypeText;


    @FXML
    private void closePreviewBtnClicked(Event e) {
        AnchorPane currentPreview = (AnchorPane) ((Button) e.getSource()).getParent().getParent();
        loginController.dashboard.myModelsView.myModelsAnchorPane.getChildren().remove(currentPreview);
    }

    @FXML
    private void downloadModelBtnClicked(Event e) {
        AnchorPane currentModel = (AnchorPane) ((Button) e.getSource()).getParent();
        FileChooser fileSaver = new FileChooser();
        fileSaver.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("STL File","*.stl"));
        fileSaver.setTitle("Save 3D Model");
        modelsIO.downloadModelFile(currentModel.getId(), (fileSaver.showSaveDialog(loginController.dashboard.dashboardAnchorPane.getScene().getWindow())).toPath());
    }
}
