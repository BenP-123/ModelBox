package com.modelbox.controllers;

import com.modelbox.databaseIO.usersIO;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;

public class profileController {

    @FXML private Button editProfileBtn;
    @FXML public TextField displayNameTextField;
    @FXML public TextField firstNameTextField;
    @FXML public TextField lastNameTextField;
    @FXML public TextField emailAddressTextField;
    @FXML public TextArea bioTextArea;
    @FXML private Circle profilePictureCircle;
    @FXML private ImageView profilePictureImage;
    @FXML private AnchorPane profileAnchorPane;
    private FileChooser profilePictureFileChooser;

    /**
     *   Constructs the profileController object, with all the corresponding properties
     *   and methods.
     *
     */
    public profileController() {
        profilePictureFileChooser = new FileChooser();
        profilePictureFileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image","*.png", "*.jpg"));
        profilePictureFileChooser.setTitle("Select Profile Picture");
    }

    /**
     *   Sets the profile picture of a user in the settings page. A user selects an image and it is converted
     *   into a String of bytes and stored into MongoDB.
     *
     *   @param  e      a JavaFX event with the properties and methods of the element that triggered the event
     *	 @return void   no value returned
     */
    @FXML
    private void addProfilePictureBtn(Event e){
        //  profilePictureImage.setImage(new Image(profilePictureFileChooser.showOpenDialog(editProfileBtn.getScene().getWindow())));
    }

    /**
     *	Sets all fields to be editable in the profile view. Upon editing the information the new data is saved
     *  to the database.
     *
     *  @param  e       a JavaFX event with the properties and methods of the element that triggered the event
     *	@return void    no value returned
     */
    @FXML
    private void editProfileBtnClicked(Event e) {
        if (editProfileBtn.getText().equals("Edit profile")) {
            editProfileBtn.setText("Update profile");
            editProfileBtn.setStyle("-fx-background-color: green;");
            displayNameTextField.setEditable(true);
            firstNameTextField.setEditable(true);
            lastNameTextField.setEditable(true);
            emailAddressTextField.setEditable(true);
            bioTextArea.setEditable(true);
        } else {

            editProfileBtn.setText("Edit profile");
            editProfileBtn.setStyle("-fx-background-color: #007be8;");
            displayNameTextField.setEditable(false);
            firstNameTextField.setEditable(false);
            lastNameTextField.setEditable(false);
            emailAddressTextField.setEditable(false);
            bioTextArea.setEditable(false);

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
