package com.modelbox.controllers;

import com.github.robtimus.net.protocol.data.DataURLs;
import com.modelbox.databaseIO.modelsIO;
import javafx.event.Event;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;


public class myModelsController {

    private FXMLLoader previewPopUpLoader;
    @FXML public ScrollPane myModelsScrollPane;
    @FXML public FlowPane myModelsFlowPane;
    @FXML public Button noModelsBtn;
    @FXML public AnchorPane myModelsAnchorPane;

    /**
     *	If the user does not have any models uploaded and clicks the prompt button on the myModels view, they are
     *	immediately forwarded to the uploadModelsView.
     *
     *  @param  e       a JavaFX event with the properties and methods of the element that triggered the event
     *	@return void    no value returned
     */
    @FXML
    private void noModelsBtnClicked(Event e) {
        try {
            loginController.dashboard.dashboardViewLoader.setController(loginController.dashboard.uploadModelsView);
            Parent root = loginController.dashboard.dashboardViewLoader.load(getClass().getResource("/views/uploadModels.fxml"));
            loginController.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception loadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }

    /**
     *   Populates the UI with a user's models. The Models are displayed and are downloadable by clicking the
     *   download button. Likewise, an interactive and larger preview can be seen by clicking the eyeball button.
     *
     *   @param  modelFile  a 3D model file
     *	 @return void       no value returned
     */
    public void addMyModelsPreviewCard(byte[] modelFile) {
        try {
            String previousValue = System.getProperty("java.protocol.handler.pkgs") == null ? "" : System.getProperty("java.protocol.handler.pkgs")+"|";
            System.setProperty("java.protocol.handler.pkgs", previousValue + "com.github.robtimus.net.protocol");

            loginController.dashboard.stlImporter.read(DataURLs.builder(modelFile).withBase64Data(true).withMediaType("model/stl").build());
        } catch (NullPointerException nullException) {
            nullException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create the models so that it can go in each model card that's generated
        TriangleMesh modelMesh = loginController.dashboard.stlImporter.getImport();
        MeshView modelMeshView = new MeshView(modelMesh);

        // Create a delete btn for each model card generated
        ImageView deleteModelIcon = new ImageView("/images/delete-btn.png");
        deleteModelIcon.setFitHeight(25);
        deleteModelIcon.setFitWidth(25);
        Button deleteModelBtn = new Button();
        deleteModelBtn.setGraphic(deleteModelIcon);
        deleteModelBtn.setStyle("-fx-background-color: none;");
        deleteModelBtn.setOnAction(e -> {
            StackPane currentModel = (StackPane) ((Button) e.getSource()).getParent();

            // Display a warning message prior to removing from the database and UI


            // Delete the model from the database
            modelsIO.deleteModelDocument(currentModel.getId());

            // Delete the model from the myModels view
            myModelsFlowPane.getChildren().remove(currentModel);
            loginController.dashboard.dbModelsList.remove(
                    loginController.dashboard.dbModelsList.get(
                            loginController.dashboard.getModelByteIndex(
                                    loginController.dashboard.dbModelsList, currentModel.getId()
                            )
                    )
            );

            // Modify UI depending on how many models remain
            if(loginController.dashboard.dbModelsList.isEmpty()){
                loginController.dashboard.myModelsView.myModelsScrollPane.setVisible(false);
                loginController.dashboard.myModelsView.noModelsBtn.setVisible(true);
            }

        });

        // Create a preview btn for each model card generated
        ImageView previewModelIcon = new ImageView("/images/preview-btn.png");
        previewModelIcon.setFitHeight(25);
        previewModelIcon.setFitWidth(25);
        Button previewModelBtn = new Button();
        previewModelBtn.setGraphic(previewModelIcon);
        previewModelBtn.setStyle("-fx-background-color: none;");
        previewModelBtn.setOnAction(e -> {
            StackPane currentModel = (StackPane) ((Button) e.getSource()).getParent();
            Parent previewRoot = null;

            // Load a preview pop-up window
            try {
                previewPopUpLoader = new FXMLLoader(getClass().getResource("/views/previewPopUp.fxml"));
                previewRoot = previewPopUpLoader.load();
                loginController.dashboard.previewPopUpView = previewPopUpLoader.getController();
            } catch (Exception loadException) {
                // Handle error if document cannot load properly

            }

            // Load the model file from database
            try {
                int modelIndex = loginController.dashboard.getModelByteIndex(loginController.dashboard.dbModelsList, currentModel.getId());
                byte[] currentModelFile = loginController.dashboard.dbModelsList.get(modelIndex);
                loginController.dashboard.stlImporter.read(DataURLs.builder(currentModelFile).withBase64Data(true).withMediaType("model/stl").build());

            } catch (Exception loadException) {
                // Handle exceptions
            }

            // Create the model in JavaFX
            TriangleMesh currentModelMesh = loginController.dashboard.stlImporter.getImport();
            MeshView currentModelMeshView = new MeshView(currentModelMesh);

            // Set the id of the previewModelSubScene to be equal to the modelName
            loginController.dashboard.previewPopUpView.previewModelAnchorPane.setId(currentModel.getId());

            // Set the modelNameText and modelTypeText labels
            loginController.dashboard.previewPopUpView.modelNameText.setText(FilenameUtils.removeExtension(currentModel.getId()));
            loginController.dashboard.previewPopUpView.modelTypeText.setText(FilenameUtils.getExtension(currentModel.getId()).toUpperCase());

            // Add the mesh to the preview pop-up group
            Group previewModelGroup = new Group(currentModelMeshView);

            // Add the group to the sub-scene for manipulation
            loginController.dashboard.previewPopUpView.previewModelSubScene.setRoot(previewModelGroup);

            // Add the camera to the sub-scene
            Camera camera = new PerspectiveCamera();
            loginController.dashboard.previewPopUpView.previewModelSubScene.setCamera(camera);

            // Position the sub-scene so that it extends to the modelAnchorPane bounds
            loginController.dashboard.previewPopUpView.previewModelSubScene.widthProperty().bind(loginController.dashboard.previewPopUpView.previewModelAnchorPane.widthProperty());
            loginController.dashboard.previewPopUpView.previewModelSubScene.heightProperty().bind(loginController.dashboard.previewPopUpView.previewModelAnchorPane.heightProperty());

            // Make the model so that it can be manipulated in 3D space
            loginController.dashboard.previewPopUpView.initMouseControl(previewModelGroup, loginController.dashboard.previewPopUpView.previewModelSubScene, (Stage) myModelsAnchorPane.getScene().getWindow());

            // Actually launch the preview pop-up
            myModelsAnchorPane.getChildren().add(previewRoot);
        });

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
            modelsIO.downloadModelFile(currentModel.getId(), (fileSaver.showSaveDialog(loginController.dashboard.dashboardAnchorPane.getScene().getWindow())).toPath());
        });

        // Manipulate the features of the model card and the arrangement of its internals
        StackPane modelMeshPane = new StackPane(modelMeshView, deleteModelBtn, previewModelBtn, downloadModelBtn);
        modelMeshPane.setId(modelsIO.getModelName(modelFile));
        modelMeshPane.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 8 8 8 8");
        modelMeshPane.setMinWidth(150);
        modelMeshPane.setMinHeight(250);
        modelMeshPane.setMaxWidth(150);
        modelMeshPane.setMaxHeight(250);
        StackPane.setAlignment(modelMeshView, Pos.CENTER);
        StackPane.setAlignment(deleteModelBtn, Pos.TOP_RIGHT);
        StackPane.setAlignment(previewModelBtn, Pos.BOTTOM_RIGHT);
        previewModelBtn.setTranslateX(-30);
        StackPane.setAlignment(downloadModelBtn, Pos.BOTTOM_RIGHT);
        myModelsFlowPane.getChildren().add(modelMeshPane);
    }
}
