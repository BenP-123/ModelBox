package com.modelbox.controllers.uploadModels;

import com.modelbox.app;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import org.bson.BsonBoolean;
import org.bson.Document;
import org.bson.BsonArray;
import org.bson.types.Binary;
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
        try {
            // Clear old list to make room for the new selection
            app.dashboard.browseModelsList.clear();

            // Add the selected files to the list
            app.dashboard.browseModelsList.addAll(
                    modelFileChooser.showOpenMultipleDialog(
                            browseModelsBtn.getScene().getWindow()
                    )
            );

            if (!app.dashboard.browseModelsList.isEmpty()) {
                app.viewLoader = new FXMLLoader(getClass().getResource("/views/uploadModels/verifyModels.fxml"));
                Parent root = app.viewLoader.load();
                app.verifyModelsView = app.viewLoader.getController();

                app.dashboard.verifyModelsList.clear();
                // Add the models to the verifyModelsList
                for (File model : app.dashboard.browseModelsList) {
                    Document modelDocument = new Document("_id", new ObjectId());
                    try {
                        modelDocument.append("owner_id", app.users.ownerId);
                        modelDocument.append("shared_id", new BsonArray());
                        modelDocument.append("modelName", model.getName());
                        byte[] data = Files.readAllBytes(model.toPath());
                        modelDocument.append("modelFile", new Binary(data));
                        modelDocument.append("isShared", new BsonBoolean(false));
                    } catch (Exception exception) {
                        // Handle errors
                        exception.printStackTrace();
                    }
                    app.dashboard.verifyModelsList.add(modelDocument);
                    app.verifyModelsView.addVerifyModelsPreviewCard(modelDocument);
                }
                app.verifyModelsView.verifyModelsFlowPane.minHeightProperty().bind(app.verifyModelsView.verifyModelsScrollPane.heightProperty());

                // Show the verifyModels view
                app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
            }
        } catch (NullPointerException nullException){
            // No models were selected for upload
        } catch (Exception exception){
            // Show other errors
            exception.printStackTrace();
        }
    }
}
