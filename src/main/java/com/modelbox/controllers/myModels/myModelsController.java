package com.modelbox.controllers.myModels;

import com.github.robtimus.net.protocol.data.DataURLs;
import com.modelbox.app;
import com.mongodb.client.model.Filters;
import com.sun.org.apache.xpath.internal.operations.Bool;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;


public class myModelsController {

    @FXML public ScrollPane myModelsScrollPane;
    @FXML public FlowPane myModelsFlowPane;
    @FXML public Button noModelsBtn;
    @FXML public AnchorPane myModelsAnchorPane;
    @FXML public AnchorPane loadingAnchorPane;

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
    public void addMyModelsPreviewCard(Document model) {
        try {
            //Make it a little clearer with my else/ if statements
            String modelOwnerId = (String) model.get("owner_id");
            String userLoggedInId = app.users.ownerId;
            Boolean isShared = (Boolean) model.get("isShared");

            String previousValue = System.getProperty("java.protocol.handler.pkgs") == null ? "" : System.getProperty("java.protocol.handler.pkgs") + "|";
            System.setProperty("java.protocol.handler.pkgs", previousValue + "com.github.robtimus.net.protocol");

            app.dashboard.stlImporter.read(DataURLs.builder(((Binary) model.get("modelFile")).getData()).withBase64Data(true).withMediaType("model/stl").build());

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
            if ((isShared && modelOwnerId.equals(userLoggedInId))) {
                //Icon if shared by owner to others
                ImageView shareModelIcon = new ImageView("/images/multi-user-btn.png");
                shareModelIcon.setFitHeight(25);
                shareModelIcon.setFitWidth(25);
                shareModelBtn.setGraphic(shareModelIcon);
                shareModelBtn.setStyle("-fx-background-color: none;");
                shareModelBtn.setOnAction(shareModelBtnClicked);
            } else if (!isShared && modelOwnerId.equals(userLoggedInId)){
                //Icon if not shared, but belongs to owner
                ImageView shareModelIcon = new ImageView("/images/share-btn.png");
                shareModelIcon.setFitHeight(25);
                shareModelIcon.setFitWidth(25);
                shareModelBtn.setGraphic(shareModelIcon);
                shareModelBtn.setStyle("-fx-background-color: none;");
                shareModelBtn.setOnAction(shareModelBtnClicked);
            } else {
                //Icon if shared from another user to currently logged in user.
                ImageView shareModelIcon = new ImageView("/images/shared-with-usr.png");
                shareModelIcon.setFitHeight(25);
                shareModelIcon.setFitWidth(25);
                shareModelBtn.setGraphic(shareModelIcon);
                shareModelBtn.setStyle("-fx-background-color: none;");
                shareModelBtn.setOnAction(sharedWithMeBtnClicked);
            }

            // Manipulate the features of the model card and the arrangement of its internals
            StackPane modelMeshPane = new StackPane(modelMeshView, deleteModelBtn, previewModelBtn, downloadModelBtn, shareModelBtn);
            modelMeshPane.setId(((ObjectId) model.get("_id")).toString());
            modelMeshPane.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 8 8 8 8");
            modelMeshPane.setMinWidth(150);
            modelMeshPane.setMinHeight(250);
            modelMeshPane.setMaxWidth(150);
            modelMeshPane.setMaxHeight(250);
            StackPane.setAlignment(modelMeshView, Pos.CENTER);
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

            // Delete the model from the database
            app.models.deleteModelDocument(currentModel.getId());

            // Delete the model from the myModels view
            myModelsFlowPane.getChildren().remove(currentModel);
            app.dashboard.dbModelsList.remove(
                    app.dashboard.dbModelsList.get(
                            app.dashboard.getDocumentIndexByModelID(
                                    app.dashboard.dbModelsList, currentModel.getId()
                            )
                    )
            );

            // Modify UI depending on how many models remain
            if (app.dashboard.dbModelsList.isEmpty()) {
                app.myModelsView.myModelsScrollPane.setVisible(false);
                app.myModelsView.noModelsBtn.setVisible(true);
            }
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
            Document model = app.dashboard.dbModelsList.get(modelIndex);

            try {
                app.dashboard.stlImporter.read(DataURLs.builder(((Binary) model.get("modelFile")).getData()).withBase64Data(true).withMediaType("model/stl").build());
            } catch (Exception exception) {
                // Handle errors
                exception.printStackTrace();
            }

            // Create the model in JavaFX
            TriangleMesh currentModelMesh = app.dashboard.stlImporter.getImport();
            MeshView currentModelMeshView = new MeshView(currentModelMesh);

            // Set the id of the previewModelSubScene to be equal to the model id
            app.previewPopUpView.previewModelAnchorPane.setId(currentModel.getId());

            // Set the modelNameText and modelTypeText labels
            String currentModelName = (String) model.get("modelName");
            app.previewPopUpView.modelNameText.setText(FilenameUtils.removeExtension(currentModelName));
            app.previewPopUpView.modelTypeText.setText(FilenameUtils.getExtension(currentModelName).toUpperCase());

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
            Parent shareRoot = null;

            // Load a share pop-up window
            try {
                app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/sharePopUp.fxml"));
                shareRoot = app.viewLoader.load();
                app.sharePopUpView = app.viewLoader.getController();
            } catch (Exception exception) {
                // Handle errors
                exception.printStackTrace();
            }

            // Set the id of the shareRootAnchorPane to be equal to the model id
            app.sharePopUpView.shareRootAnchorPane.setId(currentModel.getId());

            ArrayList collaborators = app.models.getModelCollaborators(currentModel.getId());

            if (!collaborators.isEmpty()) {
                for (Object collaborator : collaborators){
                    app.sharePopUpView.collaboratorPermissionsList.getChildren().add(new Text(collaborator.toString() + " is a collaborator."));
                }
            }


            // Actually launch the share pop-up
            myModelsAnchorPane.getChildren().add(shareRoot);
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
            StackPane currentModel = (StackPane) ((Button) event.getSource()).getParent();
            FileChooser fileSaver = new FileChooser();
            fileSaver.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("STL File","*.stl"));
            fileSaver.setTitle("Save 3D Model");
            app.models.saveModelFile(currentModel.getId(), (fileSaver.showSaveDialog(app.dashboard.dashboardAnchorPane.getScene().getWindow())).toPath());
        }
    };


    EventHandler<ActionEvent> sharedWithMeBtnClicked = new EventHandler<ActionEvent>() {

        /**
         * Views information on Models shared from other users.
         *
         * @param event a JavaFX ActionEvent
         */
        @Override
        public void handle(ActionEvent event) {

            StackPane currentModel = (StackPane) ((Button) event.getSource()).getParent();
            Parent shareRoot = null;

            // Load a share pop-up window
            try {
                app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/sharedToUser.fxml"));
                shareRoot = app.viewLoader.load();
                app.sharePopUpView = app.viewLoader.getController();
            } catch (Exception exception) {
                // Handle errors
                exception.printStackTrace();
            }

            // Set the id of the shareRootAnchorPane to be equal to the model id
            app.sharePopUpView.shareRootAnchorPane.setId(currentModel.getId());

            /*
            ArrayList collaborators = app.models.getModelsSharedCollection(currentModel.getId());
            ArrayList collabNames = app.users.getUserInfo(collaborators);

            if (!collabNames.isEmpty()) {
                for (Object collabName : collabNames){
                    app.sharePopUpView.collaboratorPermissionsList.getChildren().add(new Text(collabName + " is a collaborator."));
                }
            }

             */

            // Actually launch the share pop-up
            myModelsAnchorPane.getChildren().add(shareRoot);
        }
    };

}
