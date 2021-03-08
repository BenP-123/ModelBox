package com.modelbox.controllers;

import com.modelbox.databaseIO.modelsIO;
import com.modelbox.databaseIO.usersIO;
import com.modelbox.auth.logOut;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXML;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;


public class dashboardController {
    public List<byte[]> dbModelsList;
    public List<File> browseModelsList;
    public List<File> verifyModelsList;
    public StlMeshImporter stlImporter;
    public myModelsController myModelsView;
    public previewPopUpController previewPopUpView;
    public uploadModelsController uploadModelsView;
    public verifyModelsController verifyModelsView;
    public profileController profileView;
    public settingsController settingsView;
    public FXMLLoader dashboardViewLoader;
    private loginController logInView;
    private FXMLLoader logInViewLoader;


    @FXML public AnchorPane dashboardAnchorPane;
    @FXML public AnchorPane dashViewsAnchorPane;
    @FXML private Pane navigationMenuPane;
    @FXML private Pane accountMenuPane;
    @FXML private TextField displayNameTextField;
    @FXML private TextField emailAddressTextField;
    @FXML private Button logOutBtn;


    /**
     *   Constructs a dashboardController object
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
     *	Toggles the navigation menu
     *
     *  @param  event a JavaFX Event
     *	@return void
     */
    @FXML
    private void navigationMenuBtnClicked(Event event){
        navigationMenuPane.setVisible(!navigationMenuPane.visibleProperty().get());
    }

    /**
     *	Toggles the user account menu
     *
     *  @param  event a JavaFX Event
     *	@return void
     */
    @FXML
    private void accountMenuBtnClicked(Event event){
        if(loginController.activeLogin.getMongoDatabase() != null) {
            displayNameTextField.setText(usersIO.getDisplayName());
            emailAddressTextField.setText(usersIO.getEmailAddress());
        }
        accountMenuPane.setVisible(!accountMenuPane.visibleProperty().get());
    }

    /************************************************* UI REDIRECT METHODS ********************************************/

    /**
     * Handles the UI redirect to the my models view
     *
     * @param  event a JavaFX Event
     * @return void
     */
    @FXML
    private void myModelsBtnClicked(Event event){
        navigationMenuPane.setVisible(false);

        try {
            dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/myModels.fxml"));
            Parent root = dashboardViewLoader.load();
            myModelsView = dashboardViewLoader.getController();

            // Modify UI accordingly
            if(dbModelsList.isEmpty()){
                myModelsView.myModelsScrollPane.setVisible(false);
                myModelsView.noModelsBtn.setVisible(true);
            }
            else {
                myModelsView.myModelsFlowPane.getChildren().clear();

                for (byte[] model : dbModelsList) {
                    myModelsView.addMyModelsPreviewCard(model);
                }

                myModelsView.noModelsBtn.setVisible(false);
                myModelsView.myModelsScrollPane.setVisible(true);
            }

            dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the upload models view
     *
     * @param  event a JavaFX Event
     * @return void
     */
    @FXML
    private void uploadModelsBtnClicked(Event event){
        navigationMenuPane.setVisible(false);

        try {
            dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/uploadModels.fxml"));
            Parent root = dashboardViewLoader.load();
            uploadModelsView = dashboardViewLoader.getController();
            dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the profile view
     *
     * @param  event a JavaFX Event
     * @return void
     */
    @FXML
    private void accountProfileBtnClicked(Event event){
        accountMenuPane.setVisible(false);

        // Show profile view
        try {
            dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/profile.fxml"));
            Parent root = dashboardViewLoader.load();
            profileView = dashboardViewLoader.getController();

            // Modify UI accordingly
            if(loginController.activeLogin.getMongoDatabase() != null) {
                profileView.displayNameTextField.setText(usersIO.getDisplayName());
                profileView.firstNameTextField.setText(usersIO.getFirstName());
                profileView.lastNameTextField.setText(usersIO.getLastName());
                profileView.emailAddressTextField.setText(usersIO.getEmailAddress());
                profileView.bioTextArea.setText(usersIO.getProfileBio());
                profileView.profilePictureImage.setImage(new Image(new ByteArrayInputStream(usersIO.getProfilePicture())));
            }

            dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the settings view
     *
     * @param  event a JavaFX Event
     * @return void
     */
    @FXML
    private void accountSettingsBtnClicked(Event event){
        accountMenuPane.setVisible(false);

        // Show settings view
        try {
            dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/settings.fxml"));
            Parent root = dashboardViewLoader.load();
            settingsView = dashboardViewLoader.getController();

            // Modify UI accordingly
            if(loginController.activeLogin.getMongoDatabase() != null) {
                settingsView.displayNameTextField.setText(usersIO.getDisplayName());
            }

            dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     * Logs the user out and redirects to the login view
     *
     * @return void
     */
    @FXML
    private void logOutBtnClicked(Event e){
        try {
            logOut activeLogOut = new logOut();
            activeLogOut.logUserOut();
            logInViewLoader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = logInViewLoader.load();
            logInView = logInViewLoader.getController();
            logOutBtn.getScene().setRoot(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /*************************************************** UTILITY METHODS **********************************************/

    /**
     *	Returns the index of a specific 3D model in a list of models
     *
     *  @param  modelList     a List of File(s) that contains models
     *  @param  modelFileName a string that represents the filename of the model
     *	@return               the index value of the model in the List
     */
    public int getModelIndexByName(List<File> modelList, String modelFileName) {
        int index = 0;
        for (int i = 0; i < modelList.size(); i++) {
            if (modelFileName.equals((modelList.get(i)).getName())) {
                index = i;
            }
        }
        return index;
    }

    /**
     *	Returns the index of a specific 3D model in a list of models
     *
     *  @param  modelList     a List of byte[]'s that contains models
     *  @param  modelFileName a string that represents the filename of the model
     *	@return               the index value of the model in the List
     */
    public int getModelByteIndex(List<byte[]> modelList, String modelFileName) {
        int index = 0;
        for (int i = 0; i < modelList.size(); i++) {
            if (modelFileName.equals(modelsIO.getModelName(modelList.get(i)))) {
                index = i;
            }
        }
        return index;
    }
}
