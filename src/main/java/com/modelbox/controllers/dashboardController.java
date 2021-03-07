package com.modelbox.controllers;

import com.modelbox.databaseIO.modelsIO;
import com.modelbox.databaseIO.usersIO;
import com.modelbox.auth.logOut;
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
     *   Constructs the dashboardController object, with all the corresponding properties
     *   and methods.
     *
     */
    public dashboardController() {
        stlImporter = new StlMeshImporter();
        browseModelsList = new ArrayList<>();
        verifyModelsList = new ArrayList<>();
        dbModelsList = new ArrayList<>();
    }

    /**
     *	Displays the navigation menu.
     *
     *  @param  e      a JavaFX event with the properties and methods of the element that triggered the event
     *	@return void   no value returned
     */
    @FXML
    private void navigationMenuBtnClicked(Event e){
        navigationMenuPane.setVisible(!navigationMenuPane.visibleProperty().get());
    }

    /**
     *	Displays the user account menu.
     *
     *  @param e       a JavaFX event with the properties and methods of the element that triggered the event
     *	@return void   no value returned
     */
    @FXML
    private void accountMenuBtnClicked(Event e){
        if(loginController.activeLogin.getMongoDatabase() != null) {
            displayNameTextField.setText(usersIO.getDisplayName());
            emailAddressTextField.setText(usersIO.getEmailAddress());
        }
        accountMenuPane.setVisible(!accountMenuPane.visibleProperty().get());
    }

    /**
     *	Retrieves all of a users models from the database and displays them.
     *
     *  @param  e       a JavaFX event with the properties and methods of the element that triggered the event
     *	@return void    no value returned
     */
    @FXML
    private void myModelsBtnClicked(Event e){
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
        } catch (Exception loadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }
    
    /**
     *	Displays the uploadModels view.
     *
     *  @param  e       a JavaFX event with the properties and methods of the element that triggered the event
     *	@return void    no value returned
     */
    @FXML
    private void uploadModelsBtnClicked(Event e){
        navigationMenuPane.setVisible(false);

        try {
            dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/uploadModels.fxml"));
            Parent root = dashboardViewLoader.load();
            uploadModelsView = dashboardViewLoader.getController();
            dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception loadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }

    /**
     *	Displays the profile view.
     *
     *  @param  e       a JavaFX event with the properties and methods of the element that triggered the event
     *	@return void    no value returned
     */
    @FXML
    private void accountProfileBtnClicked(Event e){
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
            }

            dashViewsAnchorPane.getChildren().setAll(root);
        } catch (Exception loadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }
    
    /**
     *	Displays the settings view.
     *
     *  @param  e       a JavaFX event with the properties and methods of the element that triggered the event
     *	@return void    no value returned
     */
    @FXML
    private void accountSettingsBtnClicked(Event e){
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
        } catch (Exception loadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }

    /**
     *   Logs a user out of the application. After logging out, the login pane is displayed if the user
     *   wishes to login again.
     *
     *   @param  e      a JavaFX event with the properties and methods of the element that triggered the event
     *	 @return void   no value returned
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
        } catch (Exception loadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }

    /**
     *	Returns the index of a 3D model in a list of models, allowing for each model to be removed individually from the
     *	list by clicking a cancel button in the UI.
     *
     *  @param  modelList       a ListArray that contains all the models.
     *  @param  modelFileName   string that represents the model file name.
     *	@return index           the index value of the model in the List array is returned.
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
