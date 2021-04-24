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
import java.util.ArrayList;

/**
 * Provides a JavaFX controller implementation for the uploadModels.fxml view
 */
public class uploadModelsController {

    @FXML public Text uploadModelsTextHeading;
    @FXML public AnchorPane uploadModelsAnchorPane;
    @FXML public ImageView browseModelsBtnIcon;
    @FXML private Button browseModelsBtn;
    private final FileChooser modelFileChooser;

    /**
     * Constructs and initializes an uploadModelsController object
     */
    public uploadModelsController() {
        modelFileChooser = new FileChooser();
        modelFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("STL Files","*.stl"));
        modelFileChooser.setTitle("Open 3D Model");
    }

    /**
     * Opens a file browser to select local 3D models for upload and modifies the UI accordingly
     * @param event a JavaFX Event
     */
    @FXML
    private void browseModelsBtnClicked(Event event){
        try {
            // Clear old list to make room for the new selection
            app.dashboard.browseModelsList.clear();

            // Add the selected files to the list
            app.dashboard.browseModelsList.addAll(modelFileChooser.showOpenMultipleDialog(browseModelsBtn.getScene().getWindow()));

            if (!app.dashboard.browseModelsList.isEmpty()) {
                app.viewLoader = new FXMLLoader(getClass().getResource("/views/uploadModels/verifyModels.fxml"));
                Parent root = app.viewLoader.load();
                app.verifyModelsView = app.viewLoader.getController();
                app.dashboard.verifyModelsList.clear();

                // Add the models to the verifyModelsList
                ArrayList<String> invalidModels = new ArrayList<>();
                for (File model : app.dashboard.browseModelsList) {
                    byte[] data = Files.readAllBytes(model.toPath());
                    if (data.length/1024 > 10000) {
                        // Adding list of filenames that are invalid to the pop-up text
                        invalidModels.add(model.getName() + " is " + app.models.getModelSize(data.length) + ", ");
                    } else {
                        BsonDocument modelDocument = new BsonDocument("_id", new BsonObjectId());
                        modelDocument.append("owner_id", new BsonString(app.users.ownerId));
                        modelDocument.append("collaborators", new BsonArray());
                        modelDocument.append("modelName", new BsonString(model.getName()));
                        modelDocument.append("modelFile", new BsonBinary(data));
                        modelDocument.append("isShared", new BsonBoolean(false));

                        app.dashboard.verifyModelsList.add(modelDocument);
                        app.verifyModelsView.addVerifyModelsPreviewCard(modelDocument);
                    }
                }

                if (!invalidModels.isEmpty()) {
                    StringBuilder invalidPopUpText = new StringBuilder();
                    for (String invalidModel : invalidModels) {
                        invalidPopUpText.append(invalidModel);
                    }
                    invalidPopUpText.append("file size limit of 10MB was exceeded.");
                    app.verifyModelsView.removedModelsText.setText(invalidPopUpText.toString());
                    app.verifyModelsView.removedModelsPopup.setVisible(true);
                }

                if (app.dashboard.verifyModelsList.isEmpty()) {
                    browseModelsBtn.setText("The selected models exceed the 10MB file size limit. Please try again!");
                } else {
                    app.verifyModelsView.verifyModelsFlowPane.minHeightProperty().bind(app.verifyModelsView.verifyModelsScrollPane.heightProperty());

                    // Show the verifyModels view
                    app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
                }
            }
        } catch (Exception exception){
            // No models selected for upload
        }
    }
}
