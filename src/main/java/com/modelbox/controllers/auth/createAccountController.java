package com.modelbox.controllers.auth;

import com.modelbox.app;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.util.prefs.Preferences;

public class createAccountController {

    @FXML
    public TextField firstNameField;
    @FXML
    public TextField lastNameField;
    @FXML
    public TextField emailField;
    @FXML
    public PasswordField passField;
    @FXML
    public PasswordField confirmPassField;
    @FXML
    public AnchorPane createAccountForm;
    @FXML
    public AnchorPane createAccountInstructions;
    @FXML
    public TextField createAccountErrorField;
    @FXML
    public Text asterixOne;
    @FXML
    public Text welcomeTxtBox;
    @FXML
    public Text welcomeMsgTxtBox;
    @FXML
    public Text fnameTxtBox;
    @FXML
    public Text lastnameTxtBox;
    @FXML
    public Text emailAddressTxtBox;
    @FXML
    public Text passwordTxt;
    @FXML
    public Text confirmPwordText;
    @FXML
    public Text asterixTwo;
    @FXML
    public Text asterixThree;
    @FXML
    public Text asterixFour;
    @FXML
    public Text asterixFive;
    @FXML
    private Button loginBtn;
    @FXML
    public Text confirmSubHeading1;
    @FXML
    public Text confirmSubHeading2;

    /**
     * Handles the creation of a user account when the create account button is clicked
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void createAccountBtnClicked(Event event) {
        createNewUserAccount();
    }

    /**
     * Handles the creation of a user account when the enter key is pressed on the last field
     *
     * @param event a JavaFX KeyEvent
     */
    @FXML
    private void createAccountEnterKeyPressed(KeyEvent event) {
        if (event.getCode().equals((KeyCode.ENTER))) {
            createNewUserAccount();
        }
    }

    /**
     * Attempts to create a new user account using the information provided in the createAccount view
     * and modifies the view accordingly to handle errors
     */
    private void createNewUserAccount() {
        try {
            // Save the other create account fields so they can be added to the users custom_data object
            Preferences prefs = Preferences.userRoot().node("/com/modelbox");
            prefs.put("displayName", firstNameField.getText() + " " + lastNameField.getText());
            prefs.put("firstName", firstNameField.getText());
            prefs.put("lastName", lastNameField.getText());

            String functionCall = String.format("ModelBox.Auth.registerNewUser('%s', '%s', '%s', '%s', '%s');",
                    firstNameField.getText(), lastNameField.getText(), emailField.getText(), passField.getText(), confirmPassField.getText());
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the log in view
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void loginBtnClicked(Event event) {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/login.fxml"));
            Parent root = app.viewLoader.load();
            app.loginView = app.viewLoader.getController();
            app.loginView.loginDarkMode();
            loginBtn.getScene().setRoot(root);
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }
    }

    public void createAccountDarkMode() {
        if (app.viewMode) {
            app.createAccountView.welcomeTxtBox.setStyle("-fx-fill: white");
            app.createAccountView.welcomeMsgTxtBox.setStyle("-fx-fill: white");
            app.createAccountView.fnameTxtBox.setStyle("-fx-fill: white");
            app.createAccountView.asterixOne.setStyle("-fx-fill: white");
            app.createAccountView.asterixTwo.setStyle("-fx-fill: white");
            app.createAccountView.lastnameTxtBox.setStyle("-fx-fill: white");
            app.createAccountView.emailAddressTxtBox.setStyle("-fx-fill: white");
            app.createAccountView.asterixThree.setStyle("-fx-fill: white");
            app.createAccountView.passwordTxt.setStyle("-fx-fill: white");
            app.createAccountView.asterixFour.setStyle("-fx-fill: white");
            app.createAccountView.confirmPwordText.setStyle("-fx-fill: white");
            app.createAccountView.asterixFive.setStyle("-fx-fill: white");
            app.createAccountView.createAccountForm.setStyle("-fx-background-color: #17181a");
            app.createAccountView.createAccountErrorField.setStyle("-fx-background-color: #17181a; -fx-border-color: red;-fx-border-radius: 5px; -fx-padding: 8px; -fx-text-fill: white");
        } else {
            app.createAccountView.welcomeTxtBox.setStyle("-fx-fill: black");
            app.createAccountView.welcomeMsgTxtBox.setStyle("-fx-fill: black");
            app.createAccountView.fnameTxtBox.setStyle("-fx-fill: black");
            app.createAccountView.asterixOne.setStyle("-fx-fill: black");
            app.createAccountView.asterixTwo.setStyle("-fx-fill: black");
            app.createAccountView.lastnameTxtBox.setStyle("-fx-fill: black");
            app.createAccountView.emailAddressTxtBox.setStyle("-fx-fill: black");
            app.createAccountView.asterixThree.setStyle("-fx-fill: black");
            app.createAccountView.passwordTxt.setStyle("-fx-fill: black");
            app.createAccountView.asterixFour.setStyle("-fx-fill: black");
            app.createAccountView.confirmPwordText.setStyle("-fx-fill: black");
            app.createAccountView.asterixFive.setStyle("-fx-fill: black");
            app.createAccountView.createAccountErrorField.setStyle("-fx-background-color: #ffffff; -fx-border-color: red;-fx-border-radius: 5px; -fx-padding: 8px; -fx-text-fill: black");
        }
    }
}
