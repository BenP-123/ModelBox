package com.modelbox.controllers;

import com.modelbox.app;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import org.bson.Document;
import org.bson.types.ObjectId;


public class dashboardController {
    public List<Document> dbModelsList;
    public List<File> browseModelsList;
    public List<Document> verifyModelsList;
    public StlMeshImporter stlImporter;

    @FXML public AnchorPane dashboardAnchorPane;
    @FXML public AnchorPane dashViewsAnchorPane;
    @FXML private Pane navigationMenuPane;
    @FXML public Pane accountMenuPane;
    @FXML private Pane notificationsPane;
    @FXML public TextField displayNameTextField;
    @FXML public TextField emailAddressTextField;
    @FXML public Button logOutBtn;


    /**
     * Constructs a dashboardController object
     *
     */
    public dashboardController() {
        stlImporter = new StlMeshImporter();
        browseModelsList = new ArrayList<>();
        verifyModelsList = new ArrayList<>();
        dbModelsList = new ArrayList<>();
    }

    /************************************************* MENU TOGGLE METHODS ********************************************/

    /**
     *	Toggles the navigation menu visibility
     *
     *  @param  event a JavaFX Event
     */
    @FXML
    private void navigationMenuBtnClicked(Event event){
        accountMenuPane.setVisible(false);
        notificationsPane.setVisible(false);
        navigationMenuPane.setVisible(!navigationMenuPane.visibleProperty().get());
    }

    /**
     *	Toggles the user account menu visibility
     *
     *  @param  event a JavaFX Event
     */
    @FXML
    private void accountMenuBtnClicked(Event event){
        try {
            if (accountMenuPane.visibleProperty().get()) {
                accountMenuPane.setVisible(false);
            } else {
                accountMenuPane.setVisible(false);
                notificationsPane.setVisible(false);
                String functionCall = String.format("ModelBox.Users.getCurrentUserAccountMenu();");
                app.mongoApp.eval(functionCall);
            }
        } catch(Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     *	Toggles the notifications visibility
     *
     *  @param  event a JavaFX Event
     */
    @FXML
    private void notificationsBtnClicked(Event event){
        navigationMenuPane.setVisible(false);
        accountMenuPane.setVisible(false);
        notificationsPane.setVisible(!notificationsPane.visibleProperty().get());
    }

    /************************************************* UI REDIRECT METHODS ********************************************/

    /**
     * Handles the UI redirect to the my models view
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void myModelsBtnClicked(Event event){
        try {
            navigationMenuPane.setVisible(false);

            // Show my models view
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/myModels.fxml"));
            Parent root = app.viewLoader.load();
            app.myModelsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);

            // Asynchronously populate the my models view and show appropriate nodes when ready
            app.models.getAllModelsFromCurrentUser();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Handles the UI redirect to the upload models view
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void uploadModelsBtnClicked(Event event){
        try {
            navigationMenuPane.setVisible(false);

            // Show upload models view
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/uploadModels/uploadModels.fxml"));
            Parent root = app.viewLoader.load();
            app.uploadModelsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the profile view
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void accountProfileBtnClicked(Event event){
        try {
            accountMenuPane.setVisible(false);

            // Show profile view
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/account/profile.fxml"));
            Parent root = app.viewLoader.load();
            app.profileView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);

            // Load data
            String functionCall = String.format("ModelBox.Users.getCurrentUserProfile();");
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Handles the UI redirect to the settings view
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void accountSettingsBtnClicked(Event event){
        try {
            accountMenuPane.setVisible(false);

            // Show settings view
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/account/settings.fxml"));
            Parent root = app.viewLoader.load();
            app.settingsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);

            // Load data
            String functionCall = String.format("ModelBox.Users.getCurrentUserSettings();");
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Logs the user out and redirects to the login view
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void logOutBtnClicked(Event event){
        try {
            String functionCall = String.format("ModelBox.Auth.logOutCurrentUser();");
            app.mongoApp.eval(functionCall);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /*************************************************** UTILITY METHODS **********************************************/

    /**
     *	Returns the index of a specific 3D model in a list of models
     *
     *  @param list      a List of File(s) that contain 3D models
     *  @param modelName a string that represents the name of the model
     *	@return the index value of the model in the List
     */
    public int getFileIndexByModelName(List<File> list, String modelName) {
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (modelName.equals((list.get(i)).getName())) {
                index = i;
            }
        }
        return index;
    }

    /**
     *	Returns the index of a specific 3D model in a list of models
     *
     *  @param  list      a List of Document(s) that contain 3D models
     *  @param  modelID   a string that represents the id of the model
     *	@return the index value of the model in the List
     */
    public int getDocumentIndexByModelID(List<Document> list, String modelID) {
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (modelID.equals(((ObjectId) list.get(i).get("_id")).toString())) {
                index = i;
            }
        }
        return index;
    }
}
