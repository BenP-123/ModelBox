package com.modelbox.controllers;

import com.modelbox.databaseIO.usersIO;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.io.File;
import java.nio.file.Files;

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
                loginController.dashboard.verifyModelsList.clear();

                for (File model : loginController.dashboard.browseModelsList) {
                    Document modelDocument = new Document("_id", new ObjectId());
                    try {
                        modelDocument.append("owner_id", usersIO.getOwnerID());
                        modelDocument.append("modelName", model.getName());
                        byte[] data = Files.readAllBytes(model.toPath());
                        modelDocument.append("modelFile", new BsonBinary(data));
                    } catch (Exception exception) {
                        // Handle errors
                        exception.printStackTrace();
                    }
                    loginController.dashboard.verifyModelsList.add(modelDocument);
                    loginController.dashboard.verifyModelsView.addVerifyModelsPreviewCard(modelDocument);
                }

                // Show the verifyModels view
                loginController.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
            } catch (Exception e){
                // Handle errors
                e.printStackTrace();
            }
        }
    }
}
