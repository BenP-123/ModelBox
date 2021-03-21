package com.modelbox.controllers;

import com.modelbox.auth.changeEmail;
import com.modelbox.auth.deleteAccount;
import com.modelbox.databaseIO.usersIO;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;

import java.io.ByteArrayInputStream;

public class settingsController {

    public static changeEmail activeChangeEmail;
    public static deleteAccount activeDeleteAccount;
    @FXML public TextField displayNameTextField;
    @FXML private AnchorPane accountSettingsAnchorPane;
    @FXML private AnchorPane accountSecurityAnchorPane;
    @FXML private AnchorPane changeEmailAnchorPane;
    @FXML public Circle settingsPictureImage;
    @FXML private TextField newEmailField;
    @FXML private TextField changeEmailErrorField;
    @FXML private Button changeEmailButton;
    @FXML private Button deleteAccountBtn;
    @FXML private TextField deletePassField;
    @FXML private TextField deleteConfirmPassField;
    @FXML private TextField deleteAccountErrorField;

    /**
     *   Sets the account settings pane as visible
     *
     *   @param event a JavaFX Event
     */
    @FXML
    private void accountSettingsBtnClicked(Event event) {
        accountSecurityAnchorPane.setVisible(false);
        changeEmailAnchorPane.setVisible(false);
        accountSettingsAnchorPane.setVisible(true);
        changeEmailErrorField.setVisible(false);
        deleteAccountErrorField.setVisible(false);
        deletePassField.setText("");
        deleteConfirmPassField.setText("");
    }

    /**
     *   Sets the account security pane as visible.
     *
     *   @param event a JavaFX Event
     */
    @FXML
    private void accountSecurityBtnClicked(Event event) {
        accountSettingsAnchorPane.setVisible(false);
        changeEmailAnchorPane.setVisible(false);
        accountSecurityAnchorPane.setVisible(true);
        changeEmailErrorField.setVisible(false);
        deleteAccountErrorField.setVisible(false);
    }

    @FXML
    private void changeEmailBtnClicked(Event event) {
        accountSecurityAnchorPane.setVisible(false);
        accountSettingsAnchorPane.setVisible(false);
        changeEmailAnchorPane.setVisible(true);
        changeEmailErrorField.setVisible(false);
        deleteAccountErrorField.setVisible(false);
        newEmailField.setText("");
    }

    @FXML
    private void changeEmailButtonClicked(Event event) {
        changeUsersEmail();
    }

    @FXML
    private void changeEmailEnterKeyPressed(KeyEvent event) {
        if(event.getCode().equals((KeyCode.ENTER))) {
            changeUsersEmail();
        }
    }

    private void changeUsersEmail(){
        try {
            activeChangeEmail = new changeEmail();
            activeChangeEmail.setEmailAddress(newEmailField == null ? "" : newEmailField.getText());

            if (activeChangeEmail.didVerificationsPass()) {
                changeEmailErrorField.setVisible(false);

                // Attempt to reset the user's password
                if (activeChangeEmail.sendChangeEmail() == 0) {

                    // Redirect to the login
                    FXMLLoader loginLoader = new FXMLLoader();
                    Parent root = loginLoader.load(getClass().getResource("/views/login.fxml"));
                    changeEmailButton.getParent().getScene().setRoot(root);

                } else {
                    changeEmailErrorField.setText("Attempt to change email was unsuccessful. Try again!");
                    changeEmailErrorField.setVisible(true);

                    // Clear the form and let the user try again
                    newEmailField.setText("");
                }
            } else {
                changeEmailErrorField.setText(activeChangeEmail.getChangeEmailErrorMessage());
                changeEmailErrorField.setVisible(true);
            }
        } catch(Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     *   Gets the profile image from the database and displays it in the settings view
     *
     */
    public void displaySettingsPicture() {
        //Get users image from the database and show it in the settings view
        ByteArrayInputStream pictureData = new ByteArrayInputStream(usersIO.getProfilePicture());
        Image profilePicture = new Image(pictureData);
        settingsPictureImage.setFill(new ImagePattern(profilePicture));
    }

    @FXML
    private void deleteAccountBtnClicked(Event event) {
        deleteUsersAccount();
    }

    @FXML
    private void deleteConfirmPassField(KeyEvent event) {
        if(event.getCode().equals((KeyCode.ENTER))) {
            deleteUsersAccount();
        }
    }

    private void deleteUsersAccount() {
        try {
            activeDeleteAccount = new deleteAccount();
            activeDeleteAccount.setDeletePassword(deletePassField == null ? "" : deletePassField.getText());
            activeDeleteAccount.setConfirmDeletePassword(deleteConfirmPassField == null ? "" : deleteConfirmPassField.getText());

            if (activeDeleteAccount.didVerificationsPass()) {
                deleteAccountErrorField.setVisible(false);

                // Attempt to reset the user's password
                if (activeDeleteAccount.deleteUsersAccount() == 0) {

                    // Redirect to the login
                    FXMLLoader loginLoader = new FXMLLoader();
                    Parent root = loginLoader.load(getClass().getResource("/views/login.fxml"));
                    deleteAccountBtn.getParent().getScene().setRoot(root);

                } else {
                    deleteAccountErrorField.setText("Attempt to delete account was unsuccessful. Try again!");
                    deleteAccountErrorField.setVisible(true);

                    // Clear the form and let the user try again
                    deletePassField.setText("");
                    deleteConfirmPassField.setText("");
                }
            } else {
                deleteAccountErrorField.setText(activeDeleteAccount.getDeleteAccountError());
                deleteAccountErrorField.setVisible(true);
            }
        } catch(Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }
}
