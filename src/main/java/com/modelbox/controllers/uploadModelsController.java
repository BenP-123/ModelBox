package com.modelbox.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import java.io.File;

public class uploadModelsController {

    private FileChooser modelFileChooser;
    @FXML private AnchorPane uploadModelsAnchorPane;
    @FXML private Text uploadModelsTextHeading;
    @FXML private Button browseModelsBtn;
    @FXML private ImageView browserModelsBtnIcon;

    /**
     *   Constructs the uploadModelsController object, with all the corresponding properties
     *   and methods. The modeFileChooser is also configured with some basic settings.
     *
     */
    public uploadModelsController() {
        modelFileChooser = new FileChooser();
        modelFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("STL Files","*.stl"));
        modelFileChooser.setTitle("Open 3D Model");
    }

    /**
     *   A user selects one or multiple files for upload. The files are then added to a list and then UI preview cards
     *   are generated on a verification page prior to actually uploading the files to the database.
     *
     *
     *  @param  e       a JavaFX event with the properties and methods of the element that triggered the event
     *	@return void    no value returned
     */
    @FXML
    private void browseModelsBtnClicked(Event e){
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
            } catch (Exception loadException){
                // Handle exception if fxml document fails to load and show properly
            }
        }
    }
}
