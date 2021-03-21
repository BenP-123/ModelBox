package com.modelbox.controllers;

import com.github.robtimus.net.protocol.data.DataURLs;
import com.modelbox.databaseIO.modelsIO;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;

public class verifyModelsController {

    private FXMLLoader editPopUpLoader;
    @FXML private FlowPane verifyModelsFlowPane;
    @FXML public AnchorPane verifyModelsAnchorPane;

    /**
     *	Uploads the selected and verified models to the database and generates the preview cards on the my models view
     *
     *  @param  event a JavaFX Event
     */
    @FXML
    private void uploadModelsBtnClicked(Event event){
        try {
            // Store models to the database
            for (Document model : loginController.dashboard.verifyModelsList) {
                modelsIO.createNewModel(model);
            }

            // Prep the view
            loginController.dashboard.dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/myModels.fxml"));
            Parent root = loginController.dashboard.dashboardViewLoader.load();
            loginController.dashboard.myModelsView = loginController.dashboard.dashboardViewLoader.getController();
            loginController.dashboard.dashViewsAnchorPane.getChildren().setAll(root);

            // Asynchronously populate the my models view and show appropriate nodes when ready
            modelsIO.getAllModelsFromCurrentUser();

        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }

    }

    /**
     *	Populates the UI with a single preview card for all of a user's selected 3D models
     *
     *  @param  model the 3D model document selected by the user
     */
    public void addVerifyModelsPreviewCard(Document model){
        try {
            String previousValue = System.getProperty("java.protocol.handler.pkgs") == null ? "" : System.getProperty("java.protocol.handler.pkgs")+"|";
            System.setProperty("java.protocol.handler.pkgs", previousValue + "com.github.robtimus.net.protocol");

            loginController.dashboard.stlImporter.read(DataURLs.builder(modelsIO.getModelFile(model)).withBase64Data(true).withMediaType("model/stl").build());
        } catch (Exception exception) {
            // Handle exceptions
            exception.printStackTrace();
        }

        // Create the models so that it can go in each model card that's generated
        TriangleMesh modelMesh = loginController.dashboard.stlImporter.getImport();
        MeshView modelMeshView = new MeshView(modelMesh);

        // Create a cancel btn for each model card generated
        ImageView cancelUploadIcon = new ImageView("/images/delete-btn.png");
        cancelUploadIcon.setFitHeight(25);
        cancelUploadIcon.setFitWidth(25);
        Button cancelUploadBtn = new Button();
        cancelUploadBtn.setGraphic(cancelUploadIcon);
        cancelUploadBtn.setStyle("-fx-background-color: none;");
        cancelUploadBtn.setOnAction(cancelModelUploadBtnClicked);

        // Create an edit btn for each model card generated
        ImageView editModelIcon = new ImageView("/images/edit-btn.png");
        editModelIcon.setFitHeight(25);
        editModelIcon.setFitWidth(25);
        Button editModelBtn = new Button();
        editModelBtn.setGraphic(editModelIcon);
        editModelBtn.setStyle("-fx-background-color: none;");
        editModelBtn.setOnAction(editModelBtnClicked);

        // Manipulate the features of the model card and the arrangement of its internals
        StackPane modelMeshPane = new StackPane(modelMeshView, cancelUploadBtn, editModelBtn);
        modelMeshPane.setId(modelsIO.getModelID(model));
        modelMeshPane.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 8 8 8 8");
        modelMeshPane.setMinWidth(150);
        modelMeshPane.setMinHeight(250);
        modelMeshPane.setMaxWidth(150);
        modelMeshPane.setMaxHeight(250);
        StackPane.setAlignment(modelMeshView, Pos.CENTER);
        StackPane.setAlignment(cancelUploadBtn, Pos.TOP_RIGHT);
        StackPane.setAlignment(editModelBtn, Pos.BOTTOM_RIGHT);

        //Add the model card to the view
        verifyModelsFlowPane.getChildren().add(modelMeshPane);

    }

    /********************************************* PREVIEW CARD HANDLERS **********************************************/


    EventHandler<ActionEvent> cancelModelUploadBtnClicked = new EventHandler<ActionEvent>() {

        /**
         *   Deletes a model preview card from the verify models view and removes the model from the list of models to be
         *   uploaded to the database
         *
         *   @param event a JavaFX ActionEvent
         */
        @Override
        public void handle(ActionEvent event) {
            StackPane currentModel = (StackPane) ((Button) event.getSource()).getParent();
            verifyModelsFlowPane.getChildren().remove(currentModel);
            loginController.dashboard.verifyModelsList.remove(
                    loginController.dashboard.verifyModelsList.get(
                            loginController.dashboard.getDocumentIndexByModelID(
                                    loginController.dashboard.verifyModelsList, currentModel.getId()
                            )
                    )
            );

            if (loginController.dashboard.verifyModelsList.isEmpty()) {
                try {
                    loginController.dashboard.dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/uploadModels.fxml"));
                    Parent root = loginController.dashboard.dashboardViewLoader.load();
                    loginController.dashboard.uploadModelsView = loginController.dashboard.dashboardViewLoader.getController();
                    loginController.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
                } catch (Exception exception){
                    // Handle errors
                    exception.printStackTrace();
                }
            }
        }
    };

    EventHandler<ActionEvent> editModelBtnClicked = new EventHandler<ActionEvent>() {

        /**
         *   Allows the user to edit the attributes of the selected model
         *
         *   @param event a JavaFX ActionEvent
         */
        @Override
        public void handle(ActionEvent event) {
            StackPane currentModel = (StackPane) ((Button) event.getSource()).getParent();
            Parent editRoot = null;

            // Load an edit pop-up window
            try {
                editPopUpLoader = new FXMLLoader(getClass().getResource("/views/editPopUp.fxml"));
                editRoot = editPopUpLoader.load();
                loginController.dashboard.editPopUpView = editPopUpLoader.getController();
            } catch (Exception exception) {
                // Handle errors
                exception.printStackTrace();
            }

            // Load the model file from the verify models list
            int modelIndex = loginController.dashboard.getDocumentIndexByModelID(loginController.dashboard.verifyModelsList, currentModel.getId());
            byte[] currentModelFile = modelsIO.getModelFile(loginController.dashboard.verifyModelsList.get(modelIndex));

            try {
                loginController.dashboard.stlImporter.read(DataURLs.builder(currentModelFile).withBase64Data(true).withMediaType("model/stl").build());
            } catch (Exception exception) {
                // Handle errors
                exception.printStackTrace();
            }

            // Create the model in JavaFX
            TriangleMesh currentModelMesh = loginController.dashboard.stlImporter.getImport();
            MeshView currentModelMeshView = new MeshView(currentModelMesh);

            loginController.dashboard.editPopUpView.editModelStackPane.getChildren().add(currentModelMeshView);
            StackPane.setAlignment(currentModelMeshView, Pos.CENTER);

            // Set the modelNameText and modelTypeText labels
            String currentModelName = modelsIO.getModelName(loginController.dashboard.verifyModelsList.get(modelIndex));
            loginController.dashboard.editPopUpView.modelNameTextField.setText(FilenameUtils.removeExtension(currentModelName));
            loginController.dashboard.editPopUpView.modelTypeText.setText(FilenameUtils.getExtension(currentModelName).toUpperCase());

            // Actually launch the edit pop-up
            verifyModelsAnchorPane.getChildren().add(editRoot);
        }
    };
}
