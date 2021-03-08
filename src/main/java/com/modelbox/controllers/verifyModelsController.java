package com.modelbox.controllers;

import com.modelbox.databaseIO.modelsIO;
import com.modelbox.databaseIO.usersIO;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import java.io.File;
import java.net.URL;

public class verifyModelsController {

    @FXML private FlowPane verifyModelsFlowPane;

    /**
     *	Uploads the selected and verified models to the database and generates the preview cards on the my models view
     *
     *   @param  event a JavaFX Event
     *	 @return void
     */
    @FXML
    private void uploadModelsBtnClicked(Event event){
        try {
            // Store models to the database
            for (File model : loginController.dashboard.verifyModelsList) {
                modelsIO.setModelFile(model);
            }

            // Prep the view
            loginController.dashboard.dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/myModels.fxml"));
            Parent root = loginController.dashboard.dashboardViewLoader.load();
            loginController.dashboard.myModelsView = loginController.dashboard.dashboardViewLoader.getController();

            // Clear all the UI cards from the myModelsFlowPane on the myModelsView
            loginController.dashboard.myModelsView.myModelsFlowPane.getChildren().clear();

            // Clear the byte[] list with the old models stored
            loginController.dashboard.dbModelsList.clear();

            // Get the updated list of byte[]'s from the database
            modelsIO.getMyModels(usersIO.getOwnerID());

            // Add the UI cards for the myModels view
            for (byte[] model : loginController.dashboard.dbModelsList) {
                loginController.dashboard.myModelsView.addMyModelsPreviewCard(model);
            }

            loginController.dashboard.myModelsView.noModelsBtn.setVisible(false);
            loginController.dashboard.myModelsView.myModelsScrollPane.setVisible(true);

            // Show the myModelsView
            loginController.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }

    }

    /**
     *	Populates the UI with a single preview card for all of a user's selected 3D models
     *
     *   @param  modelFile the 3D Model File selected by the user
     *	 @return void
     */
    public void addVerifyModelsPreviewCard(File modelFile){
        try {
            URL modelUrl = new URL("file:///" + modelFile.getAbsolutePath());
            loginController.dashboard.stlImporter.read(modelUrl);
        }
        catch (Exception exception) {
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

    /********************************************* PREVIEW CARD HANDLERS **********************************************/

    /**
     *   Deletes a model preview card from the verify models view and removes the model from the list of models to be
     *   uploaded to the database
     *
     *   @param  e    a JavaFX ActionEvent
     *	 @return void
     */
    EventHandler<ActionEvent> cancelModelUploadBtnClicked = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent e) {
            StackPane currentModel = (StackPane) ((Button) e.getSource()).getParent();
            verifyModelsFlowPane.getChildren().remove(currentModel);
            loginController.dashboard.verifyModelsList.remove(
                    loginController.dashboard.verifyModelsList.get(
                            loginController.dashboard.getModelIndexByName(
                                    loginController.dashboard.verifyModelsList, currentModel.getId()
                            )
                    )
            );
        }
    };
}
