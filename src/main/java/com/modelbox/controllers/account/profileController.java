package com.modelbox.controllers.account;

import com.modelbox.app;
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
import java.util.Base64;

public class profileController {

    @FXML public Button editProfileBtn;
    @FXML public TextField displayNameTextField;
    @FXML public TextField firstNameTextField;
    @FXML public TextField lastNameTextField;
    @FXML public TextArea bioTextArea;
    @FXML public Circle profilePictureImage;
    @FXML public Button addProfilePictureBtn;
    @FXML public Button cancelProfileUploadBtn;
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
        // Display the users image without uploading it to the database
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
            // Get the users image and call the display function w/o uploading it to the database
            newPictureFile = profilePictureFileChooser.showOpenDialog(editProfileBtn.getScene().getWindow());
            profilePic = Files.readAllBytes(newPictureFile.toPath());
            previewUsersImage(profilePic);
        }catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }

        // Show users image before uploading
        profilePictureImage.setVisible(true);
        addProfilePictureBtn.setVisible(false);

        // Show the cancel upload image button
        cancelProfileUploadBtn.setVisible(true);
    }

    /**
     *   Cancels the users profile Upload and sets the upload button to true
     *
     *   @param  event  a JavaFX Event
     */
    @FXML
    private void cancelProfileUploadBtn(Event event){
        profilePic = null;
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
        try {
            if (editProfileBtn.getText().equals("Edit profile")) {
                editProfileBtn.setText("Update profile");
                editProfileBtn.setStyle("-fx-background-color: #55ffa7;");
                displayNameTextField.setEditable(true);
                firstNameTextField.setEditable(true);
                lastNameTextField.setEditable(true);
                bioTextArea.setEditable(true);
                profilePictureImage.setVisible(false);
                addProfilePictureBtn.setVisible(true);

            } else {
                profileContentAnchorPane.setVisible(false);
                loadingAnchorPane.setVisible(true);

                // Set data
                String profilePictureData = "";
                if (profilePic != null) {
                    byte[] encodedProfilePic = Base64.getEncoder().encode(profilePic);
                    profilePictureData = new String(encodedProfilePic);
                }
                String functionCall = String.format("ModelBox.Users.setCurrentUserProfile('%s', '%s', '%s', '%s', '%s');", displayNameTextField.getText(), firstNameTextField.getText(), lastNameTextField.getText(), bioTextArea.getText(), profilePictureData);
                app.mongoApp.eval(functionCall);
            }
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }

    }
}
