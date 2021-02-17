package com.modelbox.controllers;

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
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;


public class dashboardController {
    private List<File> myModelsList = new ArrayList<>();
    private List<File> uploadModelsList = new ArrayList<>();
    private StlMeshImporter stlImporter = new StlMeshImporter();

    @FXML private Pane navMenuPanel;
    @FXML private Pane accountMenuPanel;
    @FXML private AnchorPane myModelsView;
    @FXML private AnchorPane uploadModelView;
    @FXML private AnchorPane verifyUploadsView;
    @FXML private AnchorPane NoModelsView;
    @FXML private FlowPane myModelsFlowPanel;
    @FXML private FlowPane verifyUploadsFlowPanel;
    @FXML private AnchorPane dashboardRootNode;
    @FXML private AnchorPane settingsView;
    @FXML private AnchorPane profileView;

    @FXML
    private void navMenuBtnClicked(Event e){
        navMenuPanel.setVisible(!navMenuPanel.visibleProperty().get());
    }

    @FXML
    private void accountMenuBtnClicked(Event e){
        accountMenuPanel.setVisible(!accountMenuPanel.visibleProperty().get());
    }

    @FXML
    private void myModelsViewBtnClicked(Event e){
        verifyUploadsView.setVisible(false);
        uploadModelView.setVisible(false);
        myModelsFlowPanel.getChildren().clear();

        for (File model : myModelsList) {
            addMyModelsPreviewCard(model);
        }
        if(myModelsList.isEmpty()){
            NoModelsView.setVisible(true);
        }
        else {
            myModelsView.setVisible(true);
        }
        navMenuPanel.setVisible(false);
    }

    @FXML
    private void uploadModelViewBtnClicked(Event e){
        verifyUploadsView.setVisible(false);
        myModelsView.setVisible(false);
        NoModelsView.setVisible(false);
        uploadModelView.setVisible(true);
        navMenuPanel.setVisible(false);
    }

    @FXML
    private void NoModelsBtnClicked(Event e){
        NoModelsView.setVisible(false);
        verifyUploadsView.setVisible(false);
        myModelsView.setVisible(false);
        uploadModelView.setVisible(true);
        navMenuPanel.setVisible(false);
    }

    @FXML
    private void uploadBtnClicked(Event e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("STL Files","*.stl"));
        fileChooser.setTitle("Open 3D Model");

        // Clear old list to make room for new
        if (uploadModelsList != null) {
            for (int i = 0; i < uploadModelsList.size(); i++) {
                uploadModelsList.remove(i);
            }
        }

        uploadModelsList.addAll(fileChooser.showOpenMultipleDialog(dashboardRootNode.getScene().getWindow()));

        if (uploadModelsList != null) {
            uploadModelView.setVisible(false);
            verifyUploadsFlowPanel.getChildren().clear();
            for (File model : uploadModelsList) {
                addVerifyModelCard(model);
            }
            verifyUploadsView.setVisible(true);
        }
    }

    private void addVerifyModelCard(File modelFile){
        try {
            URL modelUrl = new URL("file:///" + modelFile.getAbsolutePath());
            stlImporter.read(modelUrl);
        }
        catch (Exception loadException) {
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
            StackPane currentModel = (StackPane) ((Button) e.getSource()).getParent();
            verifyUploadsFlowPanel.getChildren().remove(currentModel);
            uploadModelsList.remove(uploadModelsList.get(getModelIndexByName(uploadModelsList, currentModel.getId())));
        });

        // Manipulate the features of the model card and the arrangement of its internals
        StackPane modelMeshPane = new StackPane(modelMeshView, cancelUploadBtn);
        modelMeshPane.setId(modelFile.getName());
        modelMeshPane.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 8 8 8 8");
        modelMeshPane.setMinWidth(150);
        modelMeshPane.setMinHeight(250);
        modelMeshPane.setMaxWidth(150);
        modelMeshPane.setMaxHeight(250);
        StackPane.setAlignment(modelMeshView, Pos.CENTER);
        StackPane.setAlignment(cancelUploadBtn, Pos.TOP_RIGHT);

        //Add the model card to the view
        verifyUploadsFlowPanel.getChildren().add(modelMeshPane);
    }

    private int getModelIndexByName(List<File> modelList, String modelFileName) {
        int index = 0;
        for (int i = 0; i < modelList.size(); i++) {
            if (modelFileName.equals((modelList.get(i)).getName())) {
                index = i;
            }
        }
        return index;
    }

    @FXML
    private void uploadModelsBtnClicked(Event e){
        verifyUploadsView.setVisible(false);
        // FIXME: Execute database upload operations asynchronously
        // FIXME: For now files are just being loaded via a List containing all absolute paths
        myModelsList.addAll(uploadModelsList);
        myModelsFlowPanel.getChildren().clear();

        for (File model : myModelsList) {
            addMyModelsPreviewCard(model);
        }
        myModelsView.setVisible(true);
    }

    private void addMyModelsPreviewCard(File modelFile){
        try {
            URL modelUrl = new URL("file:///" + modelFile.getAbsolutePath());
            stlImporter.read(modelUrl);
        }
        catch (Exception loadException) {
            // Handle exceptions
        }

        // Create the models so that it can go in each model card that's generated
        TriangleMesh modelMesh = stlImporter.getImport();
        MeshView modelMeshView = new MeshView(modelMesh);

        // Create a download btn for each model card generated
        ImageView downloadModelIcon = new ImageView("/images/download-btn.png");
        downloadModelIcon.setFitHeight(25);
        downloadModelIcon.setFitWidth(25);
        Button downloadModelBtn = new Button();
        downloadModelBtn.setGraphic(downloadModelIcon);
        downloadModelBtn.setStyle("-fx-background-color: none;");
        downloadModelBtn.setOnAction(e -> {
            StackPane currentModel = (StackPane) ((Button) e.getSource()).getParent();
            FileChooser fileSaver = new FileChooser();
            fileSaver.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("STL File","*.stl"));
            fileSaver.setTitle("Save 3D Model");

            // Just copy the file to its new destination for now. We'll download from MongoDB later
            try {
                Files.copy(myModelsList.get(getModelIndexByName(myModelsList, currentModel.getId())).toPath(), (fileSaver.showSaveDialog(dashboardRootNode.getScene().getWindow())).toPath());
            } catch (Exception saveException) {
                // Handle exceptions
            }
        });

        // Manipulate the features of the model card and the arrangement of its internals
        StackPane modelMeshPane = new StackPane(modelMeshView, downloadModelBtn);
        modelMeshPane.setId(modelFile.getName());
        modelMeshPane.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 8 8 8 8");
        modelMeshPane.setMinWidth(150);
        modelMeshPane.setMinHeight(250);
        modelMeshPane.setMaxWidth(150);
        modelMeshPane.setMaxHeight(250);
        StackPane.setAlignment(modelMeshView, Pos.CENTER);
        StackPane.setAlignment(downloadModelBtn, Pos.BOTTOM_RIGHT);
        myModelsFlowPanel.getChildren().add(modelMeshPane);
    }

    private void settingsBtnClicked(Event e){
        NoModelsView.setVisible(false);
        verifyUploadsView.setVisible(false);
        myModelsView.setVisible(false);
        uploadModelView.setVisible(false);
        navMenuPanel.setVisible(false);
        settingsView.setVisible(true);

    }

    /*private void profileBtnClicked(Event e){
        NoModelsView.setVisible(false);
        verifyUploadsView.setVisible(false);
        myModelsView.setVisible(false);
        uploadModelView.setVisible(false);
        navMenuPanel.setVisible(false);
        profileView.setVisible(true);

    }*/
}
