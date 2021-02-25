package com.modelbox.controllers;

import com.modelbox.databaseIO.usersIO;
import com.modelbox.auth.logOut;
import com.modelbox.databaseIO.modelsIO;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.stage.FileChooser;
import javafx.fxml.FXML;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import com.interactivemesh.jfx.importer.stl.StlMeshImporter;




public class dashboardController {
    public static List<File> myModelsList = new ArrayList<>();
    public static List<File> uploadModelsList = new ArrayList<>();
    private StlMeshImporter stlImporter = new StlMeshImporter();

    @FXML private Pane navMenuPanel;
    @FXML private Pane accountMenuPanel;
    @FXML private AnchorPane myModelsView;
    @FXML private AnchorPane uploadModelView;
    @FXML private AnchorPane verifyUploadsView;
    @FXML private FlowPane myModelsFlowPanel;
    @FXML private FlowPane verifyUploadsFlowPanel;
    @FXML private AnchorPane dashboardRootNode;
    @FXML private AnchorPane settingsView;
    @FXML private AnchorPane profileView;
    @FXML private ScrollPane myModelsScrollPane;
    @FXML private Button noModelsBtn;
    @FXML private Button editProfileBtn;
    @FXML private TextField displayNameField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField emailField;
    @FXML private TextField usersSettingsDisplayName;
    @FXML private TextField accountUserName;
    @FXML private TextField accountUserEmail;
    @FXML private TextArea bioField;
    @FXML private Button logOutBtn;
    @FXML private Button accountSettingsBtn;
    @FXML private Button accountSecurityBtn;
    @FXML private AnchorPane accountSettingsAnchorPane;
    @FXML private AnchorPane accountSecurityAnchorPane;

    /**
     *	Sets all Panes to not be visible while setting one pane to visible. The one pane is passed
     *	via parameter. All panes are defined in dashboard.fxml.
     *
     *	@param ap JavaFX AnchorPane
     *	@return no value returned
     */
    private void setVisible(AnchorPane ap){
        navMenuPanel.setVisible(false);
        verifyUploadsView.setVisible(false);
        myModelsView.setVisible(false);
        uploadModelView.setVisible(false);
        accountMenuPanel.setVisible(false);
        profileView.setVisible(false);
        settingsView.setVisible(false);

        ap.setVisible(true);
    }

    /**
     *	Displays the Navigation Menu. All items are defined in dashboard.fxml
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	@return no value returned
     */
    @FXML
    private void navMenuBtnClicked(Event e){
        navMenuPanel.setVisible(!navMenuPanel.visibleProperty().get());
    }

    /**
     *	Displays the users Account. All items are defined in dashboard.fxml
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	@return no value returned
     */
    @FXML
    private void accountMenuBtnClicked(Event e){
        if(com.modelbox.auth.logIn.database != null) {
            accountUserName.setText(usersIO.getDisplayName());
            //accountUserEmail.setText(usersIO.getOwnerID());
        }
        accountMenuPanel.setVisible(!accountMenuPanel.visibleProperty().get());
    }

    
    /**
     *	If the user does not have any models uploaded and clicks the "my models" on navigation. They are immediately
     *   forwarded to the Upload Models page. All items are defined in dashboard.fxml.
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	@return no value returned
     */
    @FXML
    private void noModelsBtnClicked(Event e){ setVisible(uploadModelView); }

    
    /**
     *	Displays the users uploaded models pane. All items are defined in dashboard.fxml
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	@return no value returned
     */
    @FXML
    private void uploadModelViewBtnClicked(Event e){
        setVisible(uploadModelView);
    }

    
    /**
     *	Displays the users Settings. All items are defined in dashboard.fxml
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	@return no value returned
     */
    @FXML
    private void settingsBtnClicked(Event e){
        if(com.modelbox.auth.logIn.database != null) {
            usersSettingsDisplayName.setText(usersIO.getDisplayName());
        }
        setVisible(settingsView);
    }

    
    /**
     *	Displays the usersProfile. All items are defined in dashboard.fxml
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	@return no value returned
     */
    @FXML
    private void profileBtnClicked(Event e){
        if(com.modelbox.auth.logIn.database != null) {
            displayNameField.setText(usersIO.getDisplayName());
            firstNameField.setText(usersIO.getFirstName());
            lastNameField.setText(usersIO.getLastName());
            //emailField.setText(usersIO.getOwnerID());
            //bioField.setText(usersIO.getProfileBio());
        }
        setVisible(profileView);
    }

    /**
     *	Sets all fields to be editable in the view profile pane. Upon editing the information the new data is saved
     *  to the database. All items are defined in dashboard.fxml.
     *
     *  @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	@return no value returned
     */
    @FXML
    private void editProfileBtnClicked(Event e) {
        if (editProfileBtn.getText().equals("Edit profile")) {
            editProfileBtn.setText("Update profile");
            editProfileBtn.setStyle("-fx-background-color: green;");
            displayNameField.setEditable(true);
            firstNameField.setEditable(true);
            lastNameField.setEditable(true);
            emailField.setEditable(true);
            bioField.setEditable(true);
        } else {
            // FIXME: Implement the logic with the database so that changes are actually saved
            editProfileBtn.setText("Edit profile");
            editProfileBtn.setStyle("-fx-background-color: #007be8;");
            displayNameField.setEditable(false);
            firstNameField.setEditable(false);
            lastNameField.setEditable(false);
            emailField.setEditable(false);
            bioField.setEditable(false);
        }
    }

    /**
     *	Retrieves all of a users models from the database and displays them. All items are defined in
     *   dashboard.fxml.
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	@return no value returned
     */
    @FXML
    private void myModelsViewBtnClicked(Event e){
        if(myModelsList.isEmpty()){
            myModelsScrollPane.setVisible(false);
            noModelsBtn.setVisible(true);
        }
        else {
            myModelsFlowPanel.getChildren().clear();

            for (File model : myModelsList) {
                addMyModelsPreviewCard(model);
            }

            noModelsBtn.setVisible(false);
            myModelsScrollPane.setVisible(true);
        }

        setVisible(myModelsView);
        navMenuPanel.setVisible(false);
    }


    /**
     *   A user selects one or multiple files for upload. The files are then converted into strings of bytes and uploaded to MongoDB
     *   "models" collection. All models are tied to the user by the "owner_id" JSON field. Items are defined in dashboard.fxml.
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	@return no value returned
     */
    @FXML
    private void uploadBtnClicked(Event e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("STL Files","*.stl"));
        fileChooser.setTitle("Open 3D Model");

        // Clear old list to make room for new
        if (uploadModelsList != null) {
            for (int i = 0; i < uploadModelsList.size(); i++) {
                uploadModelsList.remove(i);
            }
        }

        uploadModelsList.addAll(fileChooser.showOpenMultipleDialog(dashboardRootNode.getScene().getWindow()));

        if (uploadModelsList != null) {
            uploadModelView.setVisible(false);
            verifyUploadsFlowPanel.getChildren().clear();
            for (File model : uploadModelsList) {
                addVerifyModelCard(model);
            }
            verifyUploadsView.setVisible(true);
        }
    }

    
    /**
     *	Upon adding the models to the upload page a user can verify the models they wish to upload.  The models are all displayed
     *   in a card view. Each model can be removed individually from the list by clicking a cancel button. 
     *
     *   @param  modelFile the 3D Model File selected by the user.
     *	@return no value returned
     */
    private void addVerifyModelCard(File modelFile){
        try {
            URL modelUrl = new URL("file:///" + modelFile.getAbsolutePath());
            stlImporter.read(modelUrl);
        }
        catch (Exception loadException) {
            // Handle exceptions
        }

        // Create the models so that it can go in each model card that's generated
        TriangleMesh modelMesh = stlImporter.getImport();
        MeshView modelMeshView = new MeshView(modelMesh);

        // Create a cancel btn for each model card generated
        ImageView cancelUploadIcon = new ImageView("/images/plus-circle.png");
        cancelUploadIcon.setRotate(45);
        cancelUploadIcon.setFitHeight(25);
        cancelUploadIcon.setFitWidth(25);
        Button cancelUploadBtn = new Button();
        cancelUploadBtn.setGraphic(cancelUploadIcon);
        cancelUploadBtn.setStyle("-fx-background-color: none;");
        cancelUploadBtn.setOnAction(e -> {
            StackPane currentModel = (StackPane) ((Button) e.getSource()).getParent();
            verifyUploadsFlowPanel.getChildren().remove(currentModel);
            uploadModelsList.remove(uploadModelsList.get(getModelIndexByName(uploadModelsList, currentModel.getId())));
        });

        // Manipulate the features of the model card and the arrangement of its internals
        StackPane modelMeshPane = new StackPane(modelMeshView, cancelUploadBtn);
        modelMeshPane.setId(modelFile.getName());
        modelMeshPane.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 8 8 8 8");
        modelMeshPane.setMinWidth(150);
        modelMeshPane.setMinHeight(250);
        modelMeshPane.setMaxWidth(150);
        modelMeshPane.setMaxHeight(250);
        StackPane.setAlignment(modelMeshView, Pos.CENTER);
        StackPane.setAlignment(cancelUploadBtn, Pos.TOP_RIGHT);

        //Add the model card to the view
        verifyUploadsFlowPanel.getChildren().add(modelMeshPane);
    }

    /**
     *	Upon adding the models to the upload page a user can verify the models they wish to upload.  The models are all displayed
     *   in a card view. Each model can be removed individually from the list by clicking a cancel button. 
     *
     *   @param  modelFileName String that represents the model file name.
     *   @param  modeList a ListArray that contains all the models. 
     *	 @return index, the index value of the model in a List array is returned.
     */
    private int getModelIndexByName(List<File> modelList, String modelFileName) {
        int index = 0;
        for (int i = 0; i < modelList.size(); i++) {
            if (modelFileName.equals((modelList.get(i)).getName())) {
                index = i;
            }
        }
        return index;
    }

    /**
     *	Upon clicking the upload models button the models are uploaded to MongoDB. The models are converted
     *   to a String of bytes and stored into the "models" collection in the database.
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	 @return no value returned
     */
    @FXML
    private void uploadModelsBtnClicked(Event e){
        verifyUploadsView.setVisible(false);
        // FIXME: Execute database upload operations asynchronously
        // FIXME: For now files are just being loaded via a List containing all absolute paths
        myModelsList.addAll(uploadModelsList);

        if(myModelsList.isEmpty()){
            myModelsScrollPane.setVisible(false);
            noModelsBtn.setVisible(true);
        }
        else {
            myModelsFlowPanel.getChildren().clear();

            for (File model : myModelsList) {
                addMyModelsPreviewCard(model);
                modelsIO.setModelFile(model);
            }

            noModelsBtn.setVisible(false);
            myModelsScrollPane.setVisible(true);
        }

        setVisible(myModelsView);
    }

    /**
     *   Populates the UI with a users models. The Models are displayed and are downloadable by clicking the download button. 
     *   All items are defined in dashboard.fxml
     *
     *   @param  modelFile a 3D model file
     *	 @return no value returned
     */
    private void addMyModelsPreviewCard(File modelFile){
        try {
            URL modelUrl = new URL("file:///" + modelFile.getAbsolutePath());
            stlImporter.read(modelUrl);
        }
        catch (Exception loadException) {
            // Handle exceptions
        }

        // Create the models so that it can go in each model card that's generated
        TriangleMesh modelMesh = stlImporter.getImport();
        MeshView modelMeshView = new MeshView(modelMesh);

        // Create a download btn for each model card generated
        ImageView downloadModelIcon = new ImageView("/images/download-btn.png");
        downloadModelIcon.setFitHeight(25);
        downloadModelIcon.setFitWidth(25);
        Button downloadModelBtn = new Button();
        downloadModelBtn.setGraphic(downloadModelIcon);
        downloadModelBtn.setStyle("-fx-background-color: none;");
        downloadModelBtn.setOnAction(e -> {
            StackPane currentModel = (StackPane) ((Button) e.getSource()).getParent();
            FileChooser fileSaver = new FileChooser();
            fileSaver.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("STL File","*.stl"));
            fileSaver.setTitle("Save 3D Model");

            // Just copy the file to its new destination for now. We'll download from MongoDB later
            try {
                Files.copy(myModelsList.get(getModelIndexByName(myModelsList, currentModel.getId())).toPath(), (fileSaver.showSaveDialog(dashboardRootNode.getScene().getWindow())).toPath());
            } catch (Exception saveException) {
                // Handle exceptions
            }
        });

        // Manipulate the features of the model card and the arrangement of its internals
        StackPane modelMeshPane = new StackPane(modelMeshView, downloadModelBtn);
        modelMeshPane.setId(modelFile.getName());
        modelMeshPane.setStyle("-fx-background-color: #eeeeee; -fx-background-radius: 8 8 8 8");
        modelMeshPane.setMinWidth(150);
        modelMeshPane.setMinHeight(250);
        modelMeshPane.setMaxWidth(150);
        modelMeshPane.setMaxHeight(250);
        StackPane.setAlignment(modelMeshView, Pos.CENTER);
        StackPane.setAlignment(downloadModelBtn, Pos.BOTTOM_RIGHT);
        myModelsFlowPanel.getChildren().add(modelMeshPane);
    }

    /**
     *   Sets the profile picture of a user in the settings page. A user selects an image and it is converted
     *   into a String of bytes and stored into MongoDB. All items are defined in dashboard.fxml. 
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	 @return no value returned
     */
    @FXML
    private void addProfilePictureBtn(Event e){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image","*.png"));
        fileChooser.setTitle("Select Profile Picture");

       // profilePicture.setImage(fileChooser.showOpenDialog(dashboardRootNode.getScene().getWindow()));
    }

    /**
     *   Logs a user out of the program. Upon logging out the login pane is displayed if the user
     *   wisher to login again. All items are defined in dashboard.fxml
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	 @return no value returned
     */
    @FXML
    private void logOutBtnClicked(Event e){
        loginController signInController = new loginController();
        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setController(signInController);

        try {
            logOut.logUserOut();
            Parent root = loginLoader.load(getClass().getResource("/views/login.fxml"));
            logOutBtn.getParent().getScene().setRoot(root);
        } catch (Exception fxmlLoadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }
    
    /**
     *   Sets the account settings pane as visible. All items are defined in dashboard.fxml
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	 @return no value returned
     */
    @FXML
    private void accountSettingsBtnClicked(Event e) {
        accountSecurityAnchorPane.setVisible(false);
        accountSettingsAnchorPane.setVisible(true);
    }

    /**
     *   Sets the account security pane as visible. All items are defined in dashboard.fxml
     *
     *   @param  e a JavaFX event with the properties and methods of the element that triggered the event
     *	 @return no value returned
     */
    @FXML
    private void accountSecurityBtnClicked(Event e) {
        accountSettingsAnchorPane.setVisible(false);
        accountSecurityAnchorPane.setVisible(true);
    }

}
