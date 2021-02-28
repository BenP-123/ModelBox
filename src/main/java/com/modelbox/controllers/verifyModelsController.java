package com.modelbox.controllers;

import com.modelbox.databaseIO.modelsIO;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;

import java.io.File;
import java.net.URL;

public class verifyModelsController {

    @FXML private AnchorPane verifyModelsAnchorPane;
    @FXML private Text verifyModelsTextHeading;
    @FXML private ScrollPane verifyModelsScrollPane;
    @FXML private FlowPane verifyModelsFlowPane;
    @FXML private Button uploadModelsBtn;

    /**
     *	Upon clicking the upload models button the models are uploaded to MongoDB. The files are then converted into
     *	strings of bytes and uploaded to a MongoDB "models" collection. All models are tied to the user by the
     *	"owner_id" JSON field.
     *
     *   @param  e      a JavaFX event with the properties and methods of the element that triggered the event
     *	 @return void   no value returned
     */
    @FXML
    private void uploadModelsBtnClicked(Event e){
        // Add new files to myModelsList
        loginController.dashboard.myModelsList.addAll(loginController.dashboard.verifyModelsList);

        try {
            loginController.dashboard.dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/myModels.fxml"));
            Parent root = loginController.dashboard.dashboardViewLoader.load();
            loginController.dashboard.myModelsView = loginController.dashboard.dashboardViewLoader.getController();

            if (loginController.dashboard.myModelsList.isEmpty()){
                loginController.dashboard.myModelsView.myModelsScrollPane.setVisible(false);
                loginController.dashboard.myModelsView.noModelsBtn.setVisible(true);
            } else {
                // Clear all the UI cards from the myModelsFlowPane on the myModelsView
                loginController.dashboard.myModelsView.myModelsFlowPane.getChildren().clear();

                // Add the UI cards for the myModels view and store model to the database
                for (File model : loginController.dashboard.verifyModelsList) {
                    loginController.dashboard.myModelsView.addMyModelsPreviewCard(model);
                    modelsIO.setModelFile(model);
                }

                loginController.dashboard.myModelsView.noModelsBtn.setVisible(false);
                loginController.dashboard.myModelsView.myModelsScrollPane.setVisible(true);
            }

            // Show myModelsView
            loginController.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception loadException){
            // Handle exception if fxml document fails to load and show properly
        }

    }

    /**
     *	Adds the models to the verifyModels view as a card with a button to delete the model from the batch of models to
     *  be uploaded.
     *
     *   @param  modelFile   the 3D Model File selected by the user.
     *	 @return void        no value returned
     */
    public void addVerifyModelsPreviewCard(File modelFile){
        try {
            URL modelUrl = new URL("file:///" + modelFile.getAbsolutePath());
            loginController.dashboard.stlImporter.read(modelUrl);
        }
        catch (Exception loadException) {
            // Handle exceptions
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
        cancelUploadBtn.setOnAction(e -> {
            StackPane currentModel = (StackPane) ((Button) e.getSource()).getParent();
            verifyModelsFlowPane.getChildren().remove(currentModel);
            loginController.dashboard.verifyModelsList.remove(
                    loginController.dashboard.verifyModelsList.get(
                            loginController.dashboard.getModelIndexByName(
                                    loginController.dashboard.verifyModelsList, currentModel.getId()
                            )
                    )
            );
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
        verifyModelsFlowPane.getChildren().add(modelMeshPane);
    }
}
