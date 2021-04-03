package com.modelbox.controllers.myModels;

import com.github.robtimus.net.protocol.data.DataURLs;
import com.modelbox.app;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.bson.BsonDocument;
import java.nio.file.Files;


public class myModelsController {

    @FXML public ScrollPane myModelsScrollPane;
    @FXML public FlowPane myModelsFlowPane;
    @FXML public Button noModelsBtn;
    @FXML public AnchorPane myModelsAnchorPane;
    @FXML public AnchorPane loadingAnchorPane;
    @FXML public TextField modelSearchField;

    /**
     * Handles the search query specified by the user
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void modelSearchEnterKeyPressed(Event event) {
        try {

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the upload models view
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void noModelsBtnClicked(Event event) {
        try {
            // Show upload models view
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/uploadModels/uploadModels.fxml"));
            Parent root = app.viewLoader.load();
            app.uploadModelsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Populates the UI with a single preview card for all of a user's uploaded 3D models
     *
     * @param model a Document containing all the information for a 3D model
     */
    public void addMyModelsPreviewCard(BsonDocument model) {
        try {
            String previousValue = System.getProperty("java.protocol.handler.pkgs") == null ? "" : System.getProperty("java.protocol.handler.pkgs") + "|";
            System.setProperty("java.protocol.handler.pkgs", previousValue + "com.github.robtimus.net.protocol");

            app.dashboard.stlImporter.read(DataURLs.builder(model.get("modelFile").asBinary().getData()).withBase64Data(true).withMediaType("model/stl").build());

            // Create the models so that it can go in each model card that's generated
            TriangleMesh modelMesh = app.dashboard.stlImporter.getImport();
            MeshView modelMeshView = new MeshView(modelMesh);

            // Create a delete btn for each model card generated
            ImageView deleteModelIcon = new ImageView("/images/delete-btn.png");
            deleteModelIcon.setFitHeight(25);
            deleteModelIcon.setFitWidth(25);
            Button deleteModelBtn = new Button();
            deleteModelBtn.setGraphic(deleteModelIcon);
            deleteModelBtn.setStyle("-fx-background-color: none;");
            deleteModelBtn.setOnAction(deleteModelBtnClicked);

            // Create a preview btn for each model card generated
            ImageView previewModelIcon = new ImageView("/images/preview-btn.png");
            previewModelIcon.setFitHeight(25);
            previewModelIcon.setFitWidth(25);
            Button previewModelBtn = new Button();
            previewModelBtn.setGraphic(previewModelIcon);
            previewModelBtn.setStyle("-fx-background-color: none;");
            previewModelBtn.setOnAction(previewModelBtnClicked);

            // Create a download btn for each model card generated
            ImageView downloadModelIcon = new ImageView("/images/download-btn.png");
            downloadModelIcon.setFitHeight(25);
            downloadModelIcon.setFitWidth(25);
            Button downloadModelBtn = new Button();
            downloadModelBtn.setGraphic(downloadModelIcon);
            downloadModelBtn.setStyle("-fx-background-color: none;");
            downloadModelBtn.setOnAction(downloadModelBtnClicked);

            // Create a share btn for each model card generated
            Button shareModelBtn = new Button();
            ImageView shareModelIcon = model.get("isShared").asBoolean().getValue() ? new ImageView("/images/multi-user-btn.png") : new ImageView("/images/share-btn.png");
            shareModelIcon.setFitHeight(25);
            shareModelIcon.setFitWidth(25);
            shareModelBtn.setGraphic(shareModelIcon);
            shareModelBtn.setStyle("-fx-background-color: none;");
            shareModelBtn.setOnAction(shareModelBtnClicked);

            // Add the mesh to the model card group
            Group modelCardGroup = new Group(modelMeshView);
            SubScene modelSubScene = new SubScene(modelCardGroup, 150, 250);

            // Center the model in the sub-scene of the card
            modelCardGroup.setTranslateX(75);
            modelCardGroup.setTranslateY(125);

            // Add the camera to the sub-scene
            Camera camera = new PerspectiveCamera();
            modelSubScene.setCamera(camera);

            // Manipulate the features of the model card and the arrangement of its internals
            StackPane modelMeshPane = new StackPane(modelSubScene, deleteModelBtn, previewModelBtn, downloadModelBtn, shareModelBtn);
            modelMeshPane.setId(model.get("_id").asObjectId().getValue().toHexString());
            modelMeshPane.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 8 8 8 8");
            modelMeshPane.setMinWidth(150);
            modelMeshPane.setMinHeight(250);
            modelMeshPane.setMaxWidth(150);
            modelMeshPane.setMaxHeight(250);
            StackPane.setAlignment(modelSubScene, Pos.CENTER);
            StackPane.setAlignment(deleteModelBtn, Pos.TOP_RIGHT);
            StackPane.setAlignment(previewModelBtn, Pos.BOTTOM_RIGHT);
            previewModelBtn.setTranslateX(-30);
            StackPane.setAlignment(shareModelBtn, Pos.BOTTOM_RIGHT);
            shareModelBtn.setTranslateX(-60);
            StackPane.setAlignment(downloadModelBtn, Pos.BOTTOM_RIGHT);
            myModelsFlowPane.getChildren().add(modelMeshPane);
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }
    }

    /********************************************* PREVIEW CARD HANDLERS **********************************************/

    EventHandler<ActionEvent> deleteModelBtnClicked = new EventHandler<ActionEvent>() {

        /**
         * Deletes a model preview card from the my models view and removes the corresponding model from the database
         *
         * @param event a JavaFX ActionEvent
         */
        @Override
        public void handle(ActionEvent event) {
            StackPane currentModel = (StackPane) ((Button) event.getSource()).getParent();

            // Delete the model from the myModels view
            myModelsFlowPane.getChildren().remove(currentModel);
            app.dashboard.dbModelsList.remove(
                    app.dashboard.dbModelsList.get(
                            app.dashboard.getDocumentIndexByModelID(
                                    app.dashboard.dbModelsList, currentModel.getId()
                            )
                    )
            );

            loadingAnchorPane.setVisible(true);
            noModelsBtn.setVisible(false);
            myModelsScrollPane.setVisible(false);

            // Delete the model from the database
            String functionCall = String.format("ModelBox.Models.deleteCurrentUserModel('%s');", currentModel.getId());
            app.mongoApp.eval(functionCall);

        }
    };

    EventHandler<ActionEvent> previewModelBtnClicked = new EventHandler<ActionEvent>() {

        /**
         * Opens a preview pop-up panel for the user to interact with and learn more about a specific model
         *
         * @param event a JavaFX ActionEvent
         */
        @Override
        public void handle(ActionEvent event) {
            StackPane currentModel = (StackPane) ((Button) event.getSource()).getParent();
            Parent previewRoot = null;


            // Load a preview pop-up window
            try {
                app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/previewPopUp.fxml"));
                previewRoot = app.viewLoader.load();
                app.previewPopUpView = app.viewLoader.getController();
            } catch (Exception exception) {
                // Handle errors
                exception.printStackTrace();
            }

            // Load the model file from the database models list
            int modelIndex = app.dashboard.getDocumentIndexByModelID(app.dashboard.dbModelsList, currentModel.getId());
            BsonDocument model = app.dashboard.dbModelsList.get(modelIndex).asDocument();

            int numBytes = model.get("modelFile").asBinary().getData().length;

            try {
                app.dashboard.stlImporter.read(DataURLs.builder(model.get("modelFile").asBinary().getData()).withBase64Data(true).withMediaType("model/stl").build());
            } catch (Exception exception) {
                // Handle errors
                exception.printStackTrace();
            }

            // Create the model in JavaFX
            TriangleMesh currentModelMesh = app.dashboard.stlImporter.getImport();
            MeshView currentModelMeshView = new MeshView(currentModelMesh);

            // Set the id of the previewModelSubScene to be equal to the model id
            app.previewPopUpView.previewModelAnchorPane.setId(currentModel.getId());

            // Set the modelNameText, modelTypeText, modelSizeText, and modelDateText model labels
            String currentModelName = model.get("modelName").asString().getValue();
            app.previewPopUpView.modelNameTextField.setText(FilenameUtils.removeExtension(currentModelName));
            app.previewPopUpView.modelTypeText.setText(FilenameUtils.getExtension(currentModelName).toUpperCase());
            app.previewPopUpView.modelSizeText.setText(app.models.getModelSize(numBytes));
            String modelId = currentModel.getId();
            app.previewPopUpView.modelDateText.setText(app.models.getModelTimestamp(modelId));

            // Add the mesh to the preview pop-up group
            Group previewModelGroup = new Group(currentModelMeshView);

            // Add the group to the sub-scene for manipulation
            app.previewPopUpView.previewModelSubScene.setRoot(previewModelGroup);

            // Add the camera to the sub-scene
            Camera camera = new PerspectiveCamera();
            app.previewPopUpView.previewModelSubScene.setCamera(camera);

            // Position the sub-scene so that it extends to the modelAnchorPane bounds
            app.previewPopUpView.previewModelSubScene.widthProperty().bind(app.previewPopUpView.previewModelAnchorPane.widthProperty());
            app.previewPopUpView.previewModelSubScene.heightProperty().bind(app.previewPopUpView.previewModelAnchorPane.heightProperty());

            // Make the model so that it can be manipulated in 3D space
            app.previewPopUpView.initMouseControl(previewModelGroup, app.previewPopUpView.previewModelSubScene, (Stage) myModelsAnchorPane.getScene().getWindow());

            // Actually launch the preview pop-up
            myModelsAnchorPane.getChildren().add(previewRoot);
        }
    };

    EventHandler<ActionEvent> shareModelBtnClicked = new EventHandler<ActionEvent>() {

        /**
         * Opens a share pop-up panel for the user to share and edit permissions for a specific model
         *
         * @param event a JavaFX ActionEvent
         */
        @Override
        public void handle(ActionEvent event) {
            StackPane currentModel = (StackPane) ((Button) event.getSource()).getParent();

            // Get the collaborators for the current model
            String functionCall = String.format("ModelBox.Models.getCurrentModelCollaborators('%s');", currentModel.getId());
            app.mongoApp.eval(functionCall);
        }
    };

    EventHandler<ActionEvent> downloadModelBtnClicked = new EventHandler<ActionEvent>() {

        /**
         * Downloads (really saves) the selected model to the users local computer
         *
         * @param event a JavaFX ActionEvent
         */
        @Override
        public void handle(ActionEvent event) {
            try {
                StackPane currentModel = (StackPane) ((Button) event.getSource()).getParent();
                FileChooser fileSaver = new FileChooser();
                fileSaver.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("STL File","*.stl"));
                fileSaver.setTitle("Save 3D Model");

                // Load the model file from the database models list
                int modelIndex = app.dashboard.getDocumentIndexByModelID(app.dashboard.dbModelsList, currentModel.getId());
                BsonDocument model = app.dashboard.dbModelsList.get(modelIndex).asDocument();

                Files.write(fileSaver.showSaveDialog(app.dashboard.dashboardAnchorPane.getScene().getWindow()).toPath(), model.get("modelFile").asBinary().getData());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    };
}
