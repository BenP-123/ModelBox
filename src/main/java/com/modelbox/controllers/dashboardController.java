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
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import org.bson.BsonArray;
import org.bson.BsonValue;

/**
 * Provides a JavaFX controller implementation for the dashboard.fxml view
 */
public class dashboardController {
    public BsonArray dbModelsList;
    public List<File> browseModelsList;
    public BsonArray verifyModelsList;
    public StlMeshImporter stlImporter;

    @FXML public VBox notificationsVBox;
    @FXML public AnchorPane dashboardAnchorPane;
    @FXML public AnchorPane dashViewsAnchorPane;
    @FXML public Pane navigationMenuPane;
    @FXML public Pane accountMenuPane;
    @FXML public AnchorPane notificationsAnchorPane;
    @FXML public TextField displayNameTextField;
    @FXML public TextField emailAddressTextField;
    @FXML public Button logOutBtn;
    @FXML public AnchorPane menuBarAnchorPane;
    @FXML public ToggleSwitch darkModeToggleSwitch;
    @FXML public Line dashboardLine;

    /**
     * Constructs and initializes a dashboardController object
     */
    public dashboardController() {
        stlImporter = new StlMeshImporter();
        browseModelsList = new ArrayList<>();
        verifyModelsList = new BsonArray();
        dbModelsList = new BsonArray();
    }

    /**
     * Toggles the navigation menu visibility
     * @param event a JavaFX Event
     */
    @FXML
    private void navigationMenuBtnClicked(Event event){
        accountMenuPane.setVisible(false);
        notificationsAnchorPane.setVisible(false);
        navigationMenuPane.setVisible(!navigationMenuPane.visibleProperty().get());
    }

    /**
     * Toggles the account menu visibility
     * @param event a JavaFX Event
     */
    @FXML
    private void accountMenuBtnClicked(Event event){
        try {
            if (accountMenuPane.visibleProperty().get()) {
                accountMenuPane.setVisible(false);
            } else {
                accountMenuPane.setVisible(false);
                notificationsAnchorPane.setVisible(false);
                String functionCall = "ModelBox.Users.getCurrentUserAccountMenu();";
                app.mongoApp.eval(functionCall);
            }
        } catch(Exception exception){
            exception.printStackTrace();
        }
    }

    /**
     * Toggles the notifications dropdown visibility
     * @param event a JavaFX Event
     */
    @FXML
    private void notificationsBtnClicked(Event event){
        navigationMenuPane.setVisible(false);
        accountMenuPane.setVisible(false);

        if (notificationsAnchorPane.visibleProperty().get()) {
            notificationsAnchorPane.setVisible(false);
        } else {
            String functionCall = "ModelBox.Users.getCurrentUserNotifications();";
            app.mongoApp.eval(functionCall);
        }
    }

    /**
     * Prepares and shows the 'My Models' view
     * @param event a JavaFX Event
     */
    @FXML
    private void myModelsBtnClicked(Event event){
        try {
            navigationMenuPane.setVisible(false);

            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/myModels.fxml"));
            Parent root = app.viewLoader.load();
            app.myModelsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);

            String functionCall = "ModelBox.Models.getCurrentUserModels();";
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Prepares and shows the 'Upload Models' view
     * @param event a JavaFX Event
     */
    @FXML
    private void uploadModelsBtnClicked(Event event){
        try {
            navigationMenuPane.setVisible(false);

            app.viewLoader = new FXMLLoader(getClass().getResource("/views/uploadModels/uploadModels.fxml"));
            Parent root = app.viewLoader.load();
            app.uploadModelsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Prepares and shows the 'Profile' view
     * @param event a JavaFX Event
     */
    @FXML
    private void accountProfileBtnClicked(Event event){
        try {
            accountMenuPane.setVisible(false);

            app.viewLoader = new FXMLLoader(getClass().getResource("/views/account/profile.fxml"));
            Parent root = app.viewLoader.load();
            app.profileView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);

            String functionCall = "ModelBox.Users.getCurrentUserProfile();";
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Prepares and shows the 'Settings' view
     * @param event a JavaFX Event
     */
    @FXML
    private void accountSettingsBtnClicked(Event event){
        try {
            accountMenuPane.setVisible(false);

            app.viewLoader = new FXMLLoader(getClass().getResource("/views/account/settings.fxml"));
            Parent root = app.viewLoader.load();
            app.settingsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);

            String functionCall = "ModelBox.Users.getCurrentUserSettings();";
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Logs the user out of their account
     * @param event a JavaFX Event
     */
    @FXML
    private void logOutBtnClicked(Event event){
        try {
            String functionCall = "ModelBox.Auth.logOutCurrentUser();";
            app.mongoApp.eval(functionCall);
        } catch (Exception exception){
            exception.printStackTrace();
        }
    }

    /**
     * Gets the index of a specific 3D model in a BSON array of models
     * @param list a BSON array of document(s) that contain 3D models
     * @param modelID a String that represents the id of the model
     * @return the index value of the model in the BSON array
     */
    public int getDocumentIndexByModelID(BsonArray list, String modelID) {
        int index = 0;
        for (int i = 0; i < list.size(); i++) {
            if (modelID.equals(list.get(i).asDocument().get("_id").asObjectId().getValue().toHexString())) {
                index = i;
            }
        }
        return index;
    }

    /**
     * Gets the role of a specific collaborator for a specific model
     * @param list a BSON array of document(s) that contain 3D models
     * @param modelID a String that represents the id of the model
     * @param collaboratorID a String that represents the id of a specific collaborator
     * @return the permissions/role value of a collaborator for a specific model
     */
    public String getCollaboratorRoleByModelID(BsonArray list, String modelID, String collaboratorID) {
        String role = "";
        for (int i = 0; i < list.size(); i++) {
            if (modelID.equals(list.get(i).asDocument().get("_id").asObjectId().getValue().toHexString())) {
                for (BsonValue collaborator : list.get(i).asDocument().get("collaborators").asArray()) {
                    if (collaborator.asDocument().get("id").asString().getValue().equals(collaboratorID)) {
                        role = collaborator.asDocument().get("permissions").asString().getValue();
                    }
                }
            }
        }
        return role;
    }
}
