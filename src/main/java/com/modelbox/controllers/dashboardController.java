package com.modelbox.controllers;

import com.modelbox.app;
import com.sun.javafx.css.StyleManager;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;
import javafx.scene.layout.VBox;
import org.bson.BsonArray;
import org.bson.BsonValue;

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
    @FXML public Button lightModeBtn;
    @FXML public Button darkModeBtn;
    @FXML public AnchorPane menuBarAnchorPane;


    /**
     * Constructs a dashboardController object
     *
     */
    public dashboardController() {
        stlImporter = new StlMeshImporter();
        browseModelsList = new ArrayList<>();
        verifyModelsList = new BsonArray();
        dbModelsList = new BsonArray();
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
        notificationsAnchorPane.setVisible(false);
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
                notificationsAnchorPane.setVisible(false);
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

        if (notificationsAnchorPane.visibleProperty().get()) {
            notificationsAnchorPane.setVisible(false);
        } else {
            // Load data
            String functionCall = String.format("ModelBox.Users.getCurrentUserNotifications();");
            app.mongoApp.eval(functionCall);
        }
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
            app.dashboard.myModelsDarkMode();

            // Asynchronously populate the my models view and show appropriate nodes when ready
            String functionCall = String.format("ModelBox.Models.getCurrentUserModels();");
            app.mongoApp.eval(functionCall);
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

            uploadModelsDarkMode();

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
            app.dashboard.profileDarkMode();
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
            app.dashboard.settingsDarkMode();

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
     *  @param  list      a BsonArray of Document(s) that contain 3D models
     *  @param  modelID   a string that represents the id of the model
     *	@return the index value of the model in the List
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

    public void darkModeBtnClicked(ActionEvent actionEvent) {
        app.userPrefs.putBoolean("viewMode", true);
        app.viewMode = app.userPrefs.getBoolean("viewMode", app.defaultView);
        app.dashboard.darkModeBtn.setVisible(false);
        app.dashboard.lightModeBtn.setVisible(true);
        toggleDarkMode();
    }

    public void lightModeBtnClicked(ActionEvent actionEvent) {
        app.userPrefs.putBoolean("viewMode", false);
        app.viewMode = app.userPrefs.getBoolean("viewMode", app.defaultView);
        app.dashboard.lightModeBtn.setVisible(false);
        app.dashboard.darkModeBtn.setVisible(true);
        app.viewMode = app.userPrefs.getBoolean("viewMode", app.defaultView);
        toggleDarkMode();
    }

    public void toggleDarkMode(){
        dashBoardDarkMode();

        if(app.myModelsView !=null) {
            myModelsDarkMode();
        }
        if(app.settingsView != null){
            settingsDarkMode();
        }
        if(app.myModelsView != null){
            myModelsDarkMode();
        }
        if(app.uploadModelsView != null){
            uploadModelsDarkMode();
        }
        if(app.verifyModelsView != null){
            uploadVerifyDarkMode();
        }
        if(app.profileView != null){
            profileDarkMode();
        }

    }

    private void dashBoardDarkMode() {
        if (app.viewMode) {
            app.dashboard.dashViewsAnchorPane.setStyle("-fx-background-color: #17181a");
            app.dashboard.dashboardAnchorPane.setStyle("-fx-background-color:  #17181a");
        } else {
            app.dashboard.dashViewsAnchorPane.setStyle("-fx-background-color: white");
            app.dashboard.dashboardAnchorPane.setStyle("-fx-background-color:   #ffffff");
        }
    }

    public void myModelsDarkMode(){
        if (app.viewMode) {
            app.myModelsView.myModelsAnchorPane.setStyle("-fx-background-color:  #17181a");
            app.myModelsView.myModelsTextHeading.setStyle("-fx-fill: #ffffff");
            app.myModelsView.myModelsHBox.setStyle("-fx-background-color:  #17181a");
            app.myModelsView.loadingBar.setStyle("-fx-fill: #ffffff");
            app.myModelsView.myModelsScrollPane.setStyle("-fx-background-color: #17181a");
            app.myModelsView.myModelsFlowPane.setStyle("-fx-background-color: #17181a");
            app.myModelsView.loadingAnchorPane.setStyle("-fx-background-color: #17181a; -fx-border-color:  #c4c4c4; -fx-border-style: dashed; -fx-border-width: 2; -fx-border-radius: 15 15 15 15");
            app.myModelsView.filterModelsChoiceBox.setStyle("-fx-border-color: white; -fx-border-width: 1px");
        }else{
            app.myModelsView.myModelsAnchorPane.setStyle("-fx-background-color:   #ffffff");
            app.myModelsView.myModelsTextHeading.setStyle("-fx-fill: black");
            app.myModelsView.myModelsHBox.setStyle("-fx-background-color:  #ffffff");
            app.myModelsView.loadingBar.setStyle("-fx-fill: black");
            app.myModelsView.myModelsFlowPane.setStyle("-fx-background-color:  #ffffff");
            app.myModelsView.myModelsScrollPane.setStyle("-fx-background-color: #ffffff");
            app.myModelsView.loadingAnchorPane.setStyle("-fx-background-color: #ffffff;  -fx-border-color:  #c4c4c4; -fx-border-style: dashed; -fx-border-width: 2; -fx-border-radius: 15 15 15 15");
            app.myModelsView.filterModelsChoiceBox.setStyle("-fx-border-color: none; -fx-border-width: 0");
        }

    }

    public void uploadModelsDarkMode(){
        if(app.viewMode){
            app.uploadModelsView.uploadModelsTextHeading.setStyle("-fx-fill: white");
            app.uploadModelsView.uploadModelsAnchorPane.setStyle("-fx-background-color: #17181a");
            app.uploadModelsView.browseModelsBtnIcon.setStyle("-fx-background-color: #17181a; -fx-border-radius: 15 15 15 15; -fx-border-style: dashed; -fx-border-width: 2; -fx-border-color: #c4c4c4");
        }else{
            app.uploadModelsView.uploadModelsTextHeading.setStyle("-fx-fill: black");
            app.uploadModelsView.uploadModelsAnchorPane.setStyle("-fx-background-color: #ffffff");
            app.uploadModelsView.browseModelsBtnIcon.setStyle("-fx-background-color: none; -fx-border-radius: 15 15 15 15; -fx-border-style: dashed; -fx-border-width: 2; -fx-border-color: #c4c4c4");
        }

    }

    public void uploadVerifyDarkMode(){
        if(app.viewMode){
            app.verifyModelsView.verifyModelsTextHeading.setStyle("-fx-fill: white");
            app.verifyModelsView.verifyModelsAnchorPane.setStyle("-fx-background-color: #17181a");
            app.verifyModelsView.verifyModelsScrollPane.setStyle("-fx-background-color: #17181a");
            app.verifyModelsView.verifyModelsFlowPane.setStyle("-fx-background-color: #17181a");
        }else{
            app.verifyModelsView.verifyModelsTextHeading.setStyle("-fx-fill: black");
            app.verifyModelsView.verifyModelsAnchorPane.setStyle("-fx-background-color: none");
            app.verifyModelsView.verifyModelsScrollPane.setStyle("-fx-background-color: #ffffff");
            app.verifyModelsView.verifyModelsFlowPane.setStyle("-fx-background-color:  #ffffff");
        }
    }

    public void settingsDarkMode(){
        if(app.viewMode){
            //Delete Account
            app.settingsView.settingsAnchorPane.setStyle("-fx-background-color:  #17181a");
            app.settingsView.settingsHbox.setStyle("-fx-background-color: #17181a");
            app.settingsView.settingsTextHeading.setStyle("-fx-fill: white");
            app.settingsView.accountSettingsAnchorPane.setStyle("-fx-background-color: #17181a");
            app.settingsView.accountSecurityAnchorPane.setStyle("-fx-background-color: #17181a");
            app.settingsView.changeEmailAnchorPane.setStyle("-fx-background-color: #17181a");
            app.settingsView.displayNameTextField.setStyle("-fx-text-fill: white; -fx-background-color: #17181a");
            app.settingsView.personalAccountTextLabel.setStyle("-fx-fill: white");
            app.settingsView.deleteAccountTxt.setStyle("-fx-fill: white");
            app.settingsView.confirmDeleteTxt.setStyle("-fx-fill: white");
            app.settingsView.typenameAcctTxt.setStyle("-fx-fill: white");
            app.settingsView.accountDataTxt.setStyle("-fx-fill: white");
            app.settingsView.collaboratorsTxt.setStyle("-fx-fill: white");
            app.settingsView.actionUndoneTxt.setStyle("-fx-fill: white");

            //Change Account Password
            app.settingsView.accountSettingsBtn.setStyle("-fx-background-color:none; -fx-border-color: #868686; -fx-text-fill: white; -fx-border-radius: 5 5 0 0; -fx-alignment: center-left");
            app.settingsView.accountSecurityBtn.setStyle("-fx-background-color:none; -fx-border-color: #868686; -fx-text-fill: white; -fx-border-radius: 5 5 0 0; -fx-alignment: center-left");
            app.settingsView.changeEmailBtn.setStyle("-fx-background-color:none; -fx-border-color:  #868686; -fx-text-fill: white; -fx-border-radius: 0 0 5 5; -fx-alignment: center-left");
            app.settingsView.changePasswordTxt.setStyle("-fx-fill: white");
            app.settingsView.passwordNoticeOne.setStyle("-fx-fill: white");
            app.settingsView.passwordNoticeTwo.setStyle("-fx-fill: white");

            //Change Account Email
            app.settingsView.changeEmailHeader.setStyle("-fx-fill: white");
            app.settingsView.changeEmailNoticeOne.setStyle("-fx-fill: white");
            app.settingsView.changeEmailNoticeTwo.setStyle("-fx-fill: white");
            app.settingsView.changeEmailNewEmailTxt.setStyle("-fx-fill: white");
            app.settingsView.changeEmailNewPassword.setStyle("-fx-fill: white");
            app.settingsView.changeEmailNewPasswordTwo.setStyle("-fx-fill: white");


        }else{
            //Default View -- Delete Account
            app.settingsView.settingsAnchorPane.setStyle("-fx-background-color:  white");
            app.settingsView.settingsHbox.setStyle("-fx-background-color: white");
            app.settingsView.settingsTextHeading.setStyle("-fx-fill: black");
            app.settingsView.accountSettingsAnchorPane.setStyle("-fx-background-color: white");
            app.settingsView.accountSecurityAnchorPane.setStyle("-fx-background-color: white");
            app.settingsView.changeEmailAnchorPane.setStyle("-fx-background-color: white");
            app.settingsView.displayNameTextField.setStyle("-fx-text-fill: black; -fx-background-color: white");
            app.settingsView.personalAccountTextLabel.setStyle("-fx-fill: black");
            app.settingsView.deleteAccountTxt.setStyle("-fx-fill: black");
            app.settingsView.confirmDeleteTxt.setStyle("-fx-fill: black");
            app.settingsView.typenameAcctTxt.setStyle("-fx-fill: black");
            app.settingsView.accountDataTxt.setStyle("-fx-fill: black");
            app.settingsView.collaboratorsTxt.setStyle("-fx-fill: black");
            app.settingsView.actionUndoneTxt.setStyle("-fx-fill: black");

            //Change Account Password
            app.settingsView.accountSettingsBtn.setStyle("-fx-background-color:none; -fx-border-color: #868686; -fx-text-fill: black; -fx-border-radius: 5 5 0 0; -fx-alignment: center-left");
            app.settingsView.accountSecurityBtn.setStyle("-fx-background-color:none; -fx-border-color: #868686; -fx-text-fill: black; -fx-border-radius: 5 5 0 0; -fx-alignment: center-left");
            app.settingsView.changeEmailBtn.setStyle("-fx-background-color:none; -fx-border-color:  #868686; -fx-text-fill: black; -fx-border-radius: 0 0 5 5; -fx-alignment: center-left");
            app.settingsView.changePasswordTxt.setStyle("-fx-fill: black");
            app.settingsView.passwordNoticeOne.setStyle("-fx-fill: black");
            app.settingsView.passwordNoticeTwo.setStyle("-fx-fill: black");

            //Change Account Email
            app.settingsView.changeEmailHeader.setStyle("-fx-fill: black");
            app.settingsView.changeEmailNoticeOne.setStyle("-fx-fill: black");
            app.settingsView.changeEmailNoticeTwo.setStyle("-fx-fill: black");
            app.settingsView.changeEmailNewEmailTxt.setStyle("-fx-fill: black");
            app.settingsView.changeEmailNewPassword.setStyle("-fx-fill: black");
            app.settingsView.changeEmailNewPasswordTwo.setStyle("-fx-fill: black");
        }
    }

    public void profileDarkMode(){
        if(app.viewMode) {
            app.profileView.profileHeader.setStyle("-fx-fill: white");
            app.profileView.displayNameTextLabel.setStyle("-fx-fill: white");
            app.profileView.firstNameTextLabel.setStyle("-fx-fill: white");
            app.profileView.lastNameTextLabel.setStyle("-fx-fill: white");
            app.profileView.bioTextLabel.setStyle("-fx-fill: white");
        }else{
            app.profileView.profileHeader.setStyle("-fx-fill: black");
            app.profileView.displayNameTextLabel.setStyle("-fx-fill: black");
            app.profileView.firstNameTextLabel.setStyle("-fx-fill: black");
            app.profileView.lastNameTextLabel.setStyle("-fx-fill: black");
            app.profileView.bioTextLabel.setStyle("-fx-fill: black");
            }
    }


}
