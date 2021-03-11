package com.modelbox.controllers;

import com.modelbox.databaseIO.usersIO;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.io.ByteArrayInputStream;

public class settingsController {

    @FXML public TextField displayNameTextField;
    @FXML private AnchorPane accountSettingsAnchorPane;
    @FXML private AnchorPane accountSecurityAnchorPane;
    @FXML private ImageView settingsPictureImage;

    /**
     *   Sets the account settings pane as visible
     *
     *   @param  event a JavaFX Event
     *	 @return void
     */
    @FXML
    private void accountSettingsBtnClicked(Event event) {
        accountSecurityAnchorPane.setVisible(false);
        accountSettingsAnchorPane.setVisible(true);
    }

    /**
     *   Sets the account security pane as visible.
     *
     *   @param  event  a JavaFX Event
     *	 @return void
     */
    @FXML
    private void accountSecurityBtnClicked(Event event) {
        accountSettingsAnchorPane.setVisible(false);
        accountSecurityAnchorPane.setVisible(true);
    }

    /**
     *   Gets the profile image from the database and displays it in the settings view
     *
     *
     *	 @return void
     */
    public void displaySettingsPicture() {
        //Get users image from the database and show it in the settings view
        ByteArrayInputStream pictureData = new ByteArrayInputStream(usersIO.getProfilePicture());
        Image profilePicture = new Image(pictureData);
        settingsPictureImage.setImage(profilePicture);
        //settingsPictureImage.setX();

    }
}
