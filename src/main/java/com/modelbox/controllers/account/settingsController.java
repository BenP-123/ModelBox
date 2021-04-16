package com.modelbox.controllers.account;

import com.modelbox.app;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

import java.util.prefs.Preferences;

public class settingsController {

    @FXML public AnchorPane settingsAnchorPane;
    @FXML public Text settingsTextHeading;
    @FXML public HBox settingsHbox;
    @FXML public Button accountSettingsBtn;
    @FXML public Button accountSecurityBtn;
    @FXML public Button changeEmailBtn;
    @FXML public TextField displayNameTextField;
    @FXML public AnchorPane accountSettingsAnchorPane;
    @FXML public AnchorPane accountSecurityAnchorPane;
    @FXML public AnchorPane changeEmailAnchorPane;
    @FXML public Circle settingsPictureImage;
    @FXML public TextField changeAccountEmailField;
    @FXML public PasswordField changeEmailPasswordField;
    @FXML public TextField changeEmailErrorField;
    @FXML private Button changeEmailButton;
    @FXML private Button deleteAccountBtn;
    @FXML public TextField deleteAccountEmailField;
    @FXML public TextField deleteAccountErrorField;
    @FXML public AnchorPane settingsContentAnchorPane;
    @FXML public AnchorPane loadingAnchorPane;
    @FXML public AnchorPane deleteConfirmationPopUp;
    @FXML public Text personalAccountTextLabel;
    @FXML public Text deleteAccountTxt;
    @FXML public Text confirmDeleteTxt;
    @FXML public Text typenameAcctTxt;
    @FXML public Text actionUndoneTxt;
    @FXML public Text accountDataTxt;
    @FXML public Text collaboratorsTxt;
    @FXML public Text changePasswordTxt;
    @FXML public Text passwordNoticeOne;
    @FXML public Text passwordNoticeTwo;
    @FXML public Text changeEmailHeader;
    @FXML public Text changeEmailNoticeOne;
    @FXML public Text changeEmailNoticeTwo;
    @FXML public Text changeEmailNewEmailTxt;
    @FXML public Text changeEmailNewPassword;
    @FXML public Text changeEmailNewPasswordTwo;

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
        deleteAccountEmailField.setText("");

        app.settingsView.accountSettingsBtn.setStyle("-fx-background-color: #D3D3D3; -fx-border-color: #868686; -fx-border-radius: 5 5 0 0; -fx-alignment: center-left;");
        app.settingsView.accountSecurityBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #868686; -fx-border-radius: 0 0 0 0; -fx-alignment: center-left;");
        app.settingsView.changeEmailBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #868686; -fx-border-radius: 0 0 5 5; -fx-alignment: center-left;");
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

        app.settingsView.accountSecurityBtn.setStyle("-fx-background-color: #D3D3D3; -fx-border-color: #868686; -fx-border-radius: 0 0 0 0; -fx-alignment: center-left;");
        app.settingsView.accountSettingsBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #868686; -fx-border-radius: 5 5 0 0; -fx-alignment: center-left;");
        app.settingsView.changeEmailBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #868686; -fx-border-radius:  0 0 5 5; -fx-alignment: center-left;");
    }

    @FXML
    private void changeEmailBtnClicked(Event event) {
        accountSecurityAnchorPane.setVisible(false);
        accountSettingsAnchorPane.setVisible(false);
        changeEmailAnchorPane.setVisible(true);
        changeEmailErrorField.setVisible(false);
        deleteAccountErrorField.setVisible(false);
        changeAccountEmailField.setText("");
        changeEmailPasswordField.setText("");

        app.settingsView.changeEmailBtn.setStyle("-fx-background-color: #D3D3D3; -fx-border-color: #868686; -fx-border-radius: 0 0 5 5; -fx-alignment: center-left;");
        app.settingsView.accountSettingsBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #868686; -fx-border-radius: 5 5 0 0; -fx-alignment: center-left;");
        app.settingsView.accountSecurityBtn.setStyle("-fx-background-color: transparent; -fx-border-color: #868686; -fx-border-radius: 0 0 0 0; -fx-alignment: center-left;");
    }

    @FXML
    private void changeEmailButtonClicked(Event event) {
        changeCurrentUserEmail();
    }

    @FXML
    private void changeEmailEnterKeyPressed(KeyEvent event) {
        if(event.getCode().equals((KeyCode.ENTER))) {
            changeCurrentUserEmail();
        }
    }

    private void changeCurrentUserEmail(){
        try {
            changeEmailErrorField.setVisible(false);

            // Save the current user's owner_id for future manipulation
            Preferences prefs = Preferences.userRoot().node("/com/modelbox");
            prefs.put("owner_id", app.users.ownerId);

            String functionCall = String.format("ModelBox.Auth.changeCurrentUserEmail('%s', '%s');", changeAccountEmailField.getText(), changeEmailPasswordField.getText());
            app.mongoApp.eval(functionCall);
        } catch(Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    @FXML
    private void changePasswordBtnClicked(){
        try {
            String functionCall = "ModelBox.Auth.changeCurrentUserPassword();";
            app.mongoApp.eval(functionCall);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    @FXML
    private void deleteAccountBtnClicked(Event event) {
        deleteAccountErrorField.setVisible(false);
        deleteConfirmationPopUp.setVisible(true);
    }

    @FXML
    private void deleteAccountEnterKeyPressed(KeyEvent event) {
        if(event.getCode().equals((KeyCode.ENTER))) {
            deleteAccountErrorField.setVisible(false);
            deleteConfirmationPopUp.setVisible(true);
        }
    }

    @FXML
    private void deleteConfirmationBtnClicked(Event event) {
        deleteCurrentUser();
    }

    @FXML
    private void closeConfirmationBtnClicked(Event event) {
        deleteAccountErrorField.setVisible(false);
        deleteConfirmationPopUp.setVisible(false);
        deleteAccountEmailField.setText("");
    }

    private void deleteCurrentUser() {
        try {
            String functionCall = String.format("ModelBox.Auth.deleteCurrentUser('%s');", deleteAccountEmailField.getText());
            app.mongoApp.eval(functionCall);
        } catch(Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }
}
