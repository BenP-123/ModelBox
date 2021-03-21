package com.modelbox.controllers;

import com.modelbox.databaseIO.usersIO;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

public class profileController {

    @FXML private Button editProfileBtn;
    @FXML public TextField displayNameTextField;
    @FXML public TextField firstNameTextField;
    @FXML public TextField lastNameTextField;
    @FXML public TextArea bioTextArea;
    @FXML public Circle profilePictureImage;
    @FXML private Button addProfilePictureBtn;
    @FXML private Button cancelProfileUploadBtn;
    @FXML public AnchorPane profileContentAnchorPane;
    @FXML public AnchorPane loadingAnchorPane;
    private FileChooser profilePictureFileChooser;
    private File newPictureFile;
    private byte[] profilePic;

    /**
     *   Constructs the profileController object
     *
     */
    public profileController() {
        profilePictureFileChooser = new FileChooser();
        profilePictureFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image","*.png", "*.jpg"));
        profilePictureFileChooser.setTitle("Select Profile Picture");
    }

    /**
     *   User selects an image to be previewed
     *
     *   @param  profilePic  a byte[] containing the file contents of the user's picture
     */
    private void previewUsersImage(byte[] profilePic) {
        //Display the users image without uploading it to the database
        ByteArrayInputStream pictureData = new ByteArrayInputStream(profilePic);
        Image profilePicture = new Image(pictureData);
        profilePictureImage.setFill(new ImagePattern(profilePicture));
    }

    /**
     *   User selects an image to be previewed
     *
     *   @param  event  a JavaFX Event
     */
    @FXML
    private void addProfilePictureBtnClicked(Event event){
        try {
            //Get the users image and call the display function w/o uploading it to the database
            newPictureFile = profilePictureFileChooser.showOpenDialog(editProfileBtn.getScene().getWindow());
            profilePic = Files.readAllBytes(newPictureFile.toPath());
            previewUsersImage(profilePic);
        }catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }
            //Show users image before uploading
            profilePictureImage.setVisible(true);
            addProfilePictureBtn.setVisible(false);

            //Show the cancel upload image button
            cancelProfileUploadBtn.setVisible(true);
    }

    /**
     *   Cancels the users profile Upload and sets the upload button to true
     *
     *   @param  event  a JavaFX Event
     */
    @FXML
    private void cancelProfileUploadBtn(Event event){
        profilePictureImage.setFill(new ImagePattern(new Image(new ByteArrayInputStream(usersIO.getProfilePicture()))));
        profilePictureImage.setVisible(false);
        cancelProfileUploadBtn.setVisible(false);
        addProfilePictureBtn.setVisible(true);
    }

    /**
     *	Allows all the fields in the profile page to be edited
     *
     *  @param  event a JavaFX Event
     */
    @FXML
    private void editProfileBtnClicked(Event event) {
        if (editProfileBtn.getText().equals("Edit profile")) {
            editProfileBtn.setText("Update profile");
            editProfileBtn.setStyle("-fx-background-color: green;");
            displayNameTextField.setEditable(true);
            firstNameTextField.setEditable(true);
            lastNameTextField.setEditable(true);
            bioTextArea.setEditable(true);
            profilePictureImage.setVisible(false);
            addProfilePictureBtn.setVisible(true);

        } else {
            editProfileBtn.setText("Edit profile");
            editProfileBtn.setStyle("-fx-background-color: #007be8;");
            displayNameTextField.setEditable(false);
            firstNameTextField.setEditable(false);
            lastNameTextField.setEditable(false);
            bioTextArea.setEditable(false);
            profilePictureImage.setVisible(true);
            addProfilePictureBtn.setVisible(false);
            cancelProfileUploadBtn.setVisible(false);

            // Save profile edits
            if(!(displayNameTextField.getText().equals(usersIO.getDisplayName()))){
                usersIO.setDisplayName(displayNameTextField.getText());
            }

            if(!(firstNameTextField.getText().equals(usersIO.getFirstName()))){
                usersIO.setFirstName(firstNameTextField.getText());
            }

            if(!(lastNameTextField.getText().equals(usersIO.getLastName()))){
                usersIO.setLastName(lastNameTextField.getText());
            }

            if(!(bioTextArea.getText().equals(usersIO.getProfileBio()))) {
                usersIO.setProfileBio(bioTextArea.getText());
            }

            if(!(usersIO.getProfilePicture().equals(profilePic))) {
                usersIO.setProfilePicture(profilePic);
            }
        }
    }
}
