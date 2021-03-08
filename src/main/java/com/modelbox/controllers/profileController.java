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
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.bson.BsonBinary;

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
    private FileChooser profilePictureFileChooser;

    /**
     *   Constructs the profileController object
     *
     */
    public profileController() {
        profilePictureFileChooser = new FileChooser();
        profilePictureFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image","*.png", "*.jpg"));
        profilePictureFileChooser.setTitle("Select Profile Picture");
    }

    private void displayProfilePicture() {

        ByteArrayInputStream pictureData = new ByteArrayInputStream(usersIO.getProfilePicture());
        Image profilePicture = new Image(pictureData);
        profilePictureImage.setImage(profilePicture);

    }

    /**
     *   Sets the profile picture of a user
     *
     *   @param  event  a JavaFX Event
     *	 @return void
     */
    @FXML
    private void addProfilePictureBtnClicked(Event event){
        try {
            // Get the new picture and upload to the database
            File newPictureFile = profilePictureFileChooser.showOpenDialog(editProfileBtn.getScene().getWindow());
            usersIO.setProfilePicture(Files.readAllBytes(newPictureFile.toPath()));

            // Show it in the profile view
            displayProfilePicture();

            // Change button preview while still in edit mode
            ImageView newButtonIcon = new ImageView(new Image("/images/save-picture-btn.png"));
            newButtonIcon.setFitWidth(200);
            newButtonIcon.setFitHeight(200);
            addProfilePictureBtn.setGraphic(newButtonIcon);
            addProfilePictureBtn.disableProperty().set(true);

        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }

    }

    /**
     *	Allows all the fields in the profile page to be edited
     *
     *  @param  event a JavaFX Event
     *	@return void
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
