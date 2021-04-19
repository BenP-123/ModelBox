package com.modelbox.controllers.uploadModels;

import com.modelbox.app;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.bson.*;
import java.io.File;
import java.nio.file.Files;

public class uploadModelsController {

    @FXML public Text uploadModelsTextHeading;
    @FXML public AnchorPane uploadModelsAnchorPane;
    @FXML public ImageView browseModelsBtnIcon;
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
                    BsonDocument modelDocument = new BsonDocument("_id", new BsonObjectId());
                    try {
                        modelDocument.append("owner_id", new BsonString(app.users.ownerId));
                        modelDocument.append("collaborators", new BsonArray());
                        modelDocument.append("modelName", new BsonString(model.getName()));
                        byte[] data = Files.readAllBytes(model.toPath());
                        modelDocument.append("modelFile", new BsonBinary(data));
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
