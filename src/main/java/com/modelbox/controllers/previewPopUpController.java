package com.modelbox.controllers;

import com.modelbox.databaseIO.modelsIO;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class previewPopUpController {

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);

    @FXML public AnchorPane previewModelAnchorPane;
    @FXML public Text modelNameText;
    @FXML public Text modelTypeText;
    @FXML public SubScene previewModelSubScene;


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

    public void initMouseControl(Group group, SubScene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        group.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );
        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);

        scene.setOnMousePressed(event -> {
            anchorX = event.getSceneX();
            anchorY = event.getSceneY();
            anchorAngleX = angleX.get();
            anchorAngleY = angleY.get();
        });

        scene.setOnMouseDragged(event -> {
            angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
            angleY.set(anchorAngleY + anchorX - event.getSceneX());
        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            group.translateZProperty().set(group.getTranslateZ() - delta);
        });
    }
}
