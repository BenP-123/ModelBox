package com.modelbox.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import java.io.File;

public class uploadModelsController {

    private FileChooser modelFileChooser;
    @FXML private Button browseModelsBtn;


    /**
     *   Constructs an uploadModelsController object
     *
     */
    public uploadModelsController() {
        modelFileChooser = new FileChooser();
        modelFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("STL Files","*.stl"));
        modelFileChooser.setTitle("Open 3D Model");
    }

    /**
     *  Opens a file browser to select local 3D models to upload
     *
     *  @param  event a JavaFX Event
     */
    @FXML
    private void browseModelsBtnClicked(Event event){
        // Clear old list to make room for the new selection
        if (!loginController.dashboard.browseModelsList.isEmpty()) {
            loginController.dashboard.browseModelsList.clear();
        }

        // Add the selected files to the list
        loginController.dashboard.browseModelsList.addAll(
                modelFileChooser.showOpenMultipleDialog(
                        browseModelsBtn.getScene().getWindow()
                )
        );

        if (!loginController.dashboard.browseModelsList.isEmpty()) {

            try {
                loginController.dashboard.dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/verifyModels.fxml"));
                Parent root = loginController.dashboard.dashboardViewLoader.load();
                loginController.dashboard.verifyModelsView = loginController.dashboard.dashboardViewLoader.getController();

                // Clear verifyModels UI and add a UI card for each model in the list
                for (File model : loginController.dashboard.browseModelsList) {
                    loginController.dashboard.verifyModelsView.addVerifyModelsPreviewCard(model);
                }

                // Copy the browseModelsList to verifyModelsList
                loginController.dashboard.verifyModelsList.clear();
                loginController.dashboard.verifyModelsList.addAll(loginController.dashboard.browseModelsList);

                // Show the verifyModels view
                loginController.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
            } catch (Exception e){
                // Handle errors
                e.printStackTrace();
            }
        }
    }
}
