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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.bson.BsonDocument;

import java.nio.file.Files;


public class myModelsController {

    @FXML public HBox myModelsToolBar;
    @FXML public ScrollPane myModelsScrollPane;
    @FXML public FlowPane myModelsFlowPane;
    @FXML public Button noModelSearchResultsBtn;
    @FXML public Button noModelsBtn;
    @FXML public AnchorPane myModelsAnchorPane;
    @FXML public AnchorPane loadingAnchorPane;
    @FXML public TextField modelSearchField;
    private int checkboxCount = 0;

    /**
     * Handles the search query specified by the user
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void searchModelBtnClicked(Event event) {
        try {
            searchForModels();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the search query specified by the user
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void searchModelEnterKeyPressed(KeyEvent event) {
        try {
            if(event.getCode().equals((KeyCode.ENTER))) {
                searchForModels();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void searchForModels() {
        try {
            myModelsToolBar.setVisible(false);
            noModelsBtn.setVisible(false);
            myModelsScrollPane.setVisible(false);
            loadingAnchorPane.setVisible(true);

            // Start the search
            String functionCall = String.format("ModelBox.Models.getCurrentUserModelSearch('%s');", modelSearchField.getText());
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Handles clearing the search query specified by the user and refreshing the view
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void cancelSearchModelBtnClicked(Event event) {
        try {
            // Show my models view
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/myModels.fxml"));
            Parent root = app.viewLoader.load();
            app.myModelsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);

            // Asynchronously populate the my models view and show appropriate nodes when ready
            String functionCall = String.format("ModelBox.Models.getCurrentUserModels();");
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles showing a checkbox on each model card to compare two selected models
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void compareModelsBtnClicked(Event event) {
        /*
        for (int i = 0; i < myModelsFlowPane.getChildren().size(); i++) {
            // For each model card activate (make visible) the checkbox to compare two models.
            ((StackPane) myModelsFlowPane.getChildren().get(i))
        }*/
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

            // Create a hidden checkbox for each model card generated for use when comparing two models
            CheckBox compareCheckbox = new CheckBox();
            compareCheckbox.setVisible(false);
            compareCheckbox.setStyle("-fx-background-color: transparent; -fx-border-color: #181a1d; -fx-border-radius: 5;");
            compareCheckbox.setOnAction(compareCheckboxClicked);

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
            StackPane modelMeshPane = new StackPane(modelSubScene, deleteModelBtn, previewModelBtn, downloadModelBtn, compareCheckbox);
            modelMeshPane.setId(model.get("_id").asObjectId().getValue().toHexString());
            modelMeshPane.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 8 8 8 8");
            modelMeshPane.setMinWidth(150);
            modelMeshPane.setMinHeight(250);
            modelMeshPane.setMaxWidth(150);
            modelMeshPane.setMaxHeight(250);
            StackPane.setAlignment(modelSubScene, Pos.CENTER);
            StackPane.setAlignment(deleteModelBtn, Pos.TOP_RIGHT);
            StackPane.setAlignment(compareCheckbox, Pos.BOTTOM_LEFT);
            StackPane.setAlignment(previewModelBtn, Pos.BOTTOM_RIGHT);
            previewModelBtn.setTranslateX(-30);
            StackPane.setAlignment(downloadModelBtn, Pos.BOTTOM_RIGHT);

            if (model.get("owner_id").asString().getValue().equals(app.users.ownerId) && model.get("isShared").asBoolean().getValue()) {
                // Current user is the owner and the model is shared

                // Show multi-user btn on models that apply
                Button shareModelBtn = new Button();
                ImageView shareModelIcon = new ImageView("/images/multi-user-btn.png");
                shareModelIcon.setFitHeight(25);
                shareModelIcon.setFitWidth(25);
                shareModelBtn.setGraphic(shareModelIcon);
                shareModelBtn.setStyle("-fx-background-color: none;");
                shareModelBtn.setOnAction(shareModelBtnClicked);

                modelMeshPane.getChildren().add(shareModelBtn);
                StackPane.setAlignment(shareModelBtn, Pos.BOTTOM_RIGHT);
                shareModelBtn.setTranslateX(-60);

            } else if (!model.get("owner_id").asString().getValue().equals(app.users.ownerId) && model.get("isShared").asBoolean().getValue()) {
                // Current user is not the owner and the model is shared

                // Show 'Shared with me' banner on models that apply
                Text sharedBannerLabel = new Text("Shared with me");
                sharedBannerLabel.setStyle("-fx-fill: #55b0ff; -fx-font-size: 12px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");
                StackPane sharedBannerPane = new StackPane(sharedBannerLabel);
                StackPane.setAlignment(sharedBannerLabel, Pos.CENTER);
                sharedBannerPane.setMinWidth(105);
                sharedBannerPane.setMinHeight(30);
                sharedBannerPane.setMaxWidth(105);
                sharedBannerPane.setMaxHeight(30);
                sharedBannerPane.setStyle("-fx-background-color: #171a1d; -fx-background-radius: 8 0 8 0;");
                modelMeshPane.getChildren().add(sharedBannerPane);
                StackPane.setAlignment(sharedBannerPane, Pos.TOP_LEFT);
            } else {
                // Current user is the owner and the model is not shared
                Button shareModelBtn = new Button();
                ImageView shareModelIcon = new ImageView("/images/share-btn.png");
                shareModelIcon.setFitHeight(25);
                shareModelIcon.setFitWidth(25);
                shareModelBtn.setGraphic(shareModelIcon);
                shareModelBtn.setStyle("-fx-background-color: none;");
                shareModelBtn.setOnAction(shareModelBtnClicked);

                modelMeshPane.getChildren().add(shareModelBtn);
                StackPane.setAlignment(shareModelBtn, Pos.BOTTOM_RIGHT);
                shareModelBtn.setTranslateX(-60);
            }

            myModelsFlowPane.getChildren().add(modelMeshPane);
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }
    }

    /********************************************* PREVIEW CARD HANDLERS **********************************************/

    EventHandler<ActionEvent> deleteModelBtnClicked = new EventHandler<ActionEvent>() {

        /**
         * If the model is a shared model, the user removes themselves from the collaboration.
         * Otherwise, the model is not shared and therefore is deleted from the my models view
         * and removes the corresponding model from the database.
         *
         * @param event a JavaFX ActionEvent
         */
        @Override
        public void handle(ActionEvent event) {
            StackPane currentModel = (StackPane) ((Button) event.getSource()).getParent();

            BsonDocument modelDocument = app.dashboard.dbModelsList.get(app.dashboard.getDocumentIndexByModelID(app.dashboard.dbModelsList, currentModel.getId())).asDocument();

            // Remove the model from the myModels view no matter what
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

            if (modelDocument.get("isShared").asBoolean().getValue() && !modelDocument.get("owner_id").asString().getValue().equals(app.users.ownerId)) {
                // Remove this user from the collaboration
                String functionCall = String.format("ModelBox.Models.terminateModelCollaboration('%s');", currentModel.getId());
                app.mongoApp.eval(functionCall);
            } else {
                // Delete the model from the database
                String functionCall = String.format("ModelBox.Models.deleteCurrentUserModel('%s');", currentModel.getId());
                app.mongoApp.eval(functionCall);
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

            // Get the collaborators for the current model and fill in the previewPopUpView
            String functionCall = String.format("ModelBox.Models.getCurrentModelPreview('%s');", currentModel.getId());
            app.mongoApp.eval(functionCall);
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

            // Get the collaborators for the current model and fill in the sharePopUpView
            String functionCall = String.format("ModelBox.Models.getCurrentModelCollaborators('%s');", currentModel.getId());
            app.mongoApp.eval(functionCall);
        }
    };

    EventHandler<ActionEvent> compareCheckboxClicked = new EventHandler<ActionEvent>() {

        /**
         * Opens a share pop-up panel for the user to share and edit permissions for a specific model
         *
         * @param event a JavaFX ActionEvent
         */
        @Override
        public void handle(ActionEvent event) {
            StackPane currentModel = (StackPane) ((CheckBox) event.getSource()).getParent();
            checkboxCount++;

            if (checkboxCount == 2) {
                // Load a share pop-up window
                try {
                    checkboxCount = 0;
                    Parent shareRoot = null;

                    app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/sharePopUp.fxml"));
                    shareRoot = app.viewLoader.load();
                    app.sharePopUpView = app.viewLoader.getController();

                    // Set the id of the shareRootAnchorPane to be equal to the model id
                    app.sharePopUpView.shareRootAnchorPane.setId(currentModel.getId());

                    // Actually launch the share pop-up
                    app.myModelsView.myModelsAnchorPane.getChildren().add(shareRoot);
                } catch (Exception exception) {
                    // Handle errors
                    exception.printStackTrace();
                }
            }
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
