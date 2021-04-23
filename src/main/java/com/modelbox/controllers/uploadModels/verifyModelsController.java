package com.modelbox.controllers.uploadModels;

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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import org.apache.commons.io.FilenameUtils;
import org.bson.BsonDocument;


public class verifyModelsController {

    @FXML public ScrollPane verifyModelsScrollPane;
    @FXML public FlowPane verifyModelsFlowPane;
    @FXML public AnchorPane verifyModelsAnchorPane;
    @FXML public Text verifyModelsTextHeading;
    @FXML public AnchorPane removedModelsPopup;
    @FXML public Text removedModelsText;

    /**
     *	Uploads the selected and verified models to the database and generates the preview cards on the my models view
     *
     *  @param event a JavaFX Event
     */
    @FXML
    private void uploadModelsBtnClicked(Event event){
        try {
            // Prep the view
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/myModels.fxml"));
            Parent root = app.viewLoader.load();
            app.myModelsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);

            // Store models to the database and asynchronously populate the my models view
            BsonDocument modelsToUpload = new BsonDocument().append("modelsToInsert", app.dashboard.verifyModelsList);
            String functionCall = String.format("ModelBox.Models.uploadCurrentUserModels('%s');", modelsToUpload.toJson());
            app.mongoApp.eval(functionCall);
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
    public void addVerifyModelsPreviewCard(BsonDocument model){
        try {
            String previousValue = System.getProperty("java.protocol.handler.pkgs") == null ? "" : System.getProperty("java.protocol.handler.pkgs")+"|";
            System.setProperty("java.protocol.handler.pkgs", previousValue + "com.github.robtimus.net.protocol");

            app.dashboard.stlImporter.read(DataURLs.builder(model.get("modelFile").asBinary().getData()).withBase64Data(true).withMediaType("model/stl").build());
        } catch (Exception exception) {
            // Handle exceptions
            exception.printStackTrace();
        }

        // Create the models so that it can go in each model card that's generated
        TriangleMesh modelMesh = app.dashboard.stlImporter.getImport();
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
        StackPane modelMeshPane = new StackPane(modelSubScene, cancelUploadBtn, editModelBtn);
        modelMeshPane.setId(model.get("_id").asObjectId().getValue().toHexString());
        modelMeshPane.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 8 8 8 8");
        modelMeshPane.setMinWidth(150);
        modelMeshPane.setMinHeight(250);
        modelMeshPane.setMaxWidth(150);
        modelMeshPane.setMaxHeight(250);
        StackPane.setAlignment(modelSubScene, Pos.CENTER);
        StackPane.setAlignment(cancelUploadBtn, Pos.TOP_RIGHT);
        StackPane.setAlignment(editModelBtn, Pos.BOTTOM_RIGHT);

        //Add the model card to the view
        verifyModelsFlowPane.getChildren().add(modelMeshPane);

    }
    @FXML
    public void closeRemovedModelsBtnClicked(Event event){
        removedModelsPopup.setVisible(false);
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
            app.dashboard.verifyModelsList.remove(
                    app.dashboard.verifyModelsList.get(
                            app.dashboard.getDocumentIndexByModelID(
                                    app.dashboard.verifyModelsList, currentModel.getId()
                            )
                    )
            );

            if (app.dashboard.verifyModelsList.isEmpty()) {
                try {
                    app.viewLoader = new FXMLLoader(getClass().getResource("/views/uploadModels/uploadModels.fxml"));
                    Parent root = app.viewLoader.load();
                    app.uploadModelsView = app.viewLoader.getController();
                    app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
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
                app.viewLoader = new FXMLLoader(getClass().getResource("/views/uploadModels/editPopUp.fxml"));
                editRoot = app.viewLoader.load();
                app.editPopUpView = app.viewLoader.getController();
            } catch (Exception exception) {
                // Handle errors
                exception.printStackTrace();
            }

            // Load the model file from the verify models list
            int modelIndex = app.dashboard.getDocumentIndexByModelID(app.dashboard.verifyModelsList, currentModel.getId());
            byte[] currentModelFile = app.dashboard.verifyModelsList.get(modelIndex).asDocument().get("modelFile").asBinary().getData();

            try {
                app.dashboard.stlImporter.read(DataURLs.builder(currentModelFile).withBase64Data(true).withMediaType("model/stl").build());
            } catch (Exception exception) {
                // Handle errors
                exception.printStackTrace();
            }

            // Create the model in JavaFX
            TriangleMesh currentModelMesh = app.dashboard.stlImporter.getImport();
            MeshView currentModelMeshView = new MeshView(currentModelMesh);

            StackPane editModelGroup = new StackPane(currentModelMeshView);
            editModelGroup.setStyle("-fx-background-radius: 0 15 15 0; -fx-background-color: #eeeeee");
            editModelGroup.setCenterShape(true);
            app.editPopUpView.editModelSubScene.setRoot(editModelGroup);
            Camera camera = new PerspectiveCamera();
            app.editPopUpView.editModelSubScene.setCamera(camera);

            app.editPopUpView.editModelSubScene.widthProperty().bind(app.editPopUpView.editModelAnchorPane.widthProperty());
            app.editPopUpView.editModelSubScene.heightProperty().bind(app.editPopUpView.editModelAnchorPane.heightProperty());

            // Set the modelNameText and modelTypeText labels
            String currentModelName = app.dashboard.verifyModelsList.get(modelIndex).asDocument().get("modelName").asString().getValue();
            app.editPopUpView.modelNameTextField.setText(FilenameUtils.removeExtension(currentModelName));
            app.editPopUpView.modelTypeText.setText(FilenameUtils.getExtension(currentModelName).toUpperCase());

            app.editPopUpView.editInfoAnchorPane.setId(currentModel.getId());

            // Actually launch the edit pop-up
            verifyModelsAnchorPane.getChildren().add(editRoot);
        }
    };
}
