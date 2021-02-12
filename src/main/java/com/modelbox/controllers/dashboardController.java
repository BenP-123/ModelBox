package com.modelbox.controllers;

import com.interactivemesh.jfx.importer.ImportException;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import java.io.File;
import java.net.URL;
import java.util.List;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;


public class dashboardController {
    @FXML private Pane navMenuPanel;
    @FXML private Pane accountMenuPanel;
    @FXML private AnchorPane myModelsView;
    @FXML private AnchorPane uploadModelView;
    @FXML private AnchorPane verifyUploadsView;
    @FXML private FlowPane verifyUploadsFlowPanel;
    @FXML private AnchorPane dashboardRootNode;

    @FXML
    private void navMenuBtnClicked(Event e){
        if (navMenuPanel.visibleProperty().get()) {
            navMenuPanel.setVisible(false);
        } else {
            navMenuPanel.setVisible(true);
        }
    }

    @FXML
    private void accountMenuBtnClicked(Event e){
        if (accountMenuPanel.visibleProperty().get()) {
            accountMenuPanel.setVisible(false);
        } else {
            accountMenuPanel.setVisible(true);
        }
    }

    @FXML
    private void myModelsViewBtnClicked(Event e){
        verifyUploadsView.setVisible(false);
        uploadModelView.setVisible(false);
        myModelsView.setVisible(true);
        navMenuPanel.setVisible(false);

    }

    @FXML
    private void uploadModelViewBtnClicked(Event e){
        verifyUploadsView.setVisible(false);
        myModelsView.setVisible(false);
        uploadModelView.setVisible(true);
        navMenuPanel.setVisible(false);
    }

    @FXML
    private void uploadBtnClicked(Event e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("3D Files","*.stl"));
        fileChooser.setTitle("Open 3D Model");

        // FIXME: Store the models in a class-wide available list for use in other private methods
        // FIXME: (i.e. delete a model on the upload verification view)
        List<File> modelList = fileChooser.showOpenMultipleDialog(dashboardRootNode.getScene().getWindow());

        if (modelList != null) {
            uploadModelView.setVisible(false);
            verifyUploadsView.setVisible(true);
            for (File model : modelList) {
                addVerifyModelCard(model);
            }
        }
    }

    private void addVerifyModelCard(File modelFile){
        // FIXME: Add a preview card to the uploadPreviewView for each file before being "uploaded".
        StlMeshImporter stlImporter = new StlMeshImporter();
        try {
            URL modelUrl = new URL("file:///" + modelFile.getAbsolutePath());
            stlImporter.read(modelUrl);
        }
        catch (Exception e) {
            // Handle exceptions
        }

        // Create the models so that it can go in each model card that's generated
        TriangleMesh modelMesh = stlImporter.getImport();
        MeshView modelMeshView = new MeshView(modelMesh);

        // Create a cancel btn for each model card generated
        ImageView cancelUploadIcon = new ImageView("/images/plus-circle.png");
        cancelUploadIcon.setRotate(45);
        cancelUploadIcon.setFitHeight(25);
        cancelUploadIcon.setFitWidth(25);
        Button cancelUploadBtn = new Button();
        cancelUploadBtn.setGraphic(cancelUploadIcon);
        cancelUploadBtn.setStyle("-fx-background-color: none;");
        cancelUploadBtn.setOnAction(e -> {
            ((Button) e.getSource()).getParent().setVisible(false);
        });

        // Manipulate the features of the model card and the arrangement of its internals
        StackPane modelMeshPane = new StackPane(modelMeshView, cancelUploadBtn);
        modelMeshPane.setId("uploadCard-" + modelFile.getName());
        modelMeshPane.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 8 8 8 8");
        modelMeshPane.setMinWidth(150);
        modelMeshPane.setMinHeight(250);
        modelMeshPane.setMaxWidth(150);
        modelMeshPane.setMaxHeight(250);
        modelMeshPane.setAlignment(modelMeshView, Pos.CENTER);
        modelMeshPane.setAlignment(cancelUploadBtn, Pos.TOP_RIGHT);
        verifyUploadsFlowPanel.getChildren().add(modelMeshPane);
    }

    @FXML
    private void uploadModelsBtnClicked(Event e){
        verifyUploadsView.setVisible(false);
        // FIXME: Execute database upload operations asynchronously


        myModelsView.setVisible(true);
    }
}
