package com.modelbox.controllers;

import com.modelbox.databaseIO.usersIO;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;

public class profileController {

    @FXML private Button editProfileBtn;
    @FXML public TextField displayNameTextField;
    @FXML public TextField firstNameTextField;
    @FXML public TextField lastNameTextField;
    @FXML public TextField emailAddressTextField;
    @FXML public TextArea bioTextArea;
    @FXML public ImageView profilePictureImage;
    @FXML private Button addProfilePictureBtn;
    @FXML private Button cancelProfileUploadBtn;
    @FXML public AnchorPane profileContentAnchorPane;
    @FXML public AnchorPane loadingAnchorPane;
    private FileChooser profilePictureFileChooser;
    private File newPictureFile;
    private int checkIfCancelBtnClicked;

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
     *   Checks to see if the image should be uploaded to the database or not
     *   and Gets the image from the database to be updated in the profile view
     *
     */
    private void displayProfilePicture() {
        //If checkIfCancelBtnClicked equals one the user decided to not change the image and
        //wants to keep the previous image uploaded to the database before editing their profile
        if(checkIfCancelBtnClicked != 1) {
            try {
                //Upload users image to the database
                usersIO.setProfilePicture(Files.readAllBytes(this.newPictureFile.toPath()));
            } catch (Exception exception) {
                // Handle errors
                exception.printStackTrace();
            }
        }

        //Get the users image from the database and display it
        ByteArrayInputStream pictureData = new ByteArrayInputStream(usersIO.getProfilePicture());
        Image profilePicture = new Image(pictureData);
        profilePictureImage.setImage(profilePicture);
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
        profilePictureImage.setImage(profilePicture);
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
            byte[] profilePic = Files.readAllBytes(newPictureFile.toPath());
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

            //The user has currently not cancelled their chosen image and is previewing their chosen image
            checkIfCancelBtnClicked = 0;
    }

    /**
     *   Cancels the users profile Upload and sets the upload button to true
     *
     *   @param  event  a JavaFX Event
     */
    @FXML
    private void cancelProfileUploadBtn(Event event){
        profilePictureImage.setVisible(false);
        addProfilePictureBtn.setVisible(true);
        cancelProfileUploadBtn.setVisible(false);

        //User has cancelled their selected image and if they click
        //upload the previous image stored in the database will be displayed
        //AKA Pre edit profile image
        checkIfCancelBtnClicked = 1;
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
            emailAddressTextField.setEditable(true);
            bioTextArea.setEditable(true);
            profilePictureImage.setVisible(false);
            addProfilePictureBtn.setVisible(true);

        } else {
            editProfileBtn.setText("Edit profile");
            editProfileBtn.setStyle("-fx-background-color: #007be8;");
            displayNameTextField.setEditable(false);
            firstNameTextField.setEditable(false);
            lastNameTextField.setEditable(false);
            emailAddressTextField.setEditable(false);
            bioTextArea.setEditable(false);
            profilePictureImage.setVisible(true);
            addProfilePictureBtn.setVisible(false);
            cancelProfileUploadBtn.setVisible(false);
            // Show users image in the profile view
            displayProfilePicture();

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

            if(!(emailAddressTextField.getText().equals(usersIO.getEmailAddress()))){
                usersIO.setEmailAddress(emailAddressTextField.getText());
            }

            if(!(bioTextArea.getText().equals(usersIO.getProfileBio()))) {
                usersIO.setProfileBio(bioTextArea.getText());
            }
        }
    }
}
