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
    private String whiteText = "-fx-fill: white";
    private String blackText = "-fx-fill: black";
    private String textFields = "-fx-background-color: transparent; -fx-border-color: white; -fx-border-radius: 4px; -fx-padding: 8px; -fx-text-fill: white";
    private String textFieldsDefault = "-fx-background-color: #ffffff; -fx-border-color: #C4C4C4; -fx-border-radius: 4px; -fx-padding: 8px; -fx-text-fill: black; -fx-faint-focus-color: transparent";


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
            app.createAccountView.firstNameField.setStyle(textFields);
            app.createAccountView.lastNameField.setStyle(textFields);
            app.createAccountView.emailField.setStyle(textFields);
            app.createAccountView.passField.setStyle(textFields);
            app.createAccountView.confirmPassField.setStyle(textFields);

            app.createAccountView.welcomeTxtBox.setStyle(whiteText);
            app.createAccountView.welcomeMsgTxtBox.setStyle(whiteText);
            app.createAccountView.fnameTxtBox.setStyle(whiteText);
            app.createAccountView.asterixOne.setStyle(whiteText);
            app.createAccountView.asterixTwo.setStyle(whiteText);
            app.createAccountView.lastnameTxtBox.setStyle(whiteText);
            app.createAccountView.emailAddressTxtBox.setStyle(whiteText);
            app.createAccountView.asterixThree.setStyle(whiteText);
            app.createAccountView.passwordTxt.setStyle(whiteText);
            app.createAccountView.asterixFour.setStyle(whiteText);
            app.createAccountView.confirmPwordText.setStyle(whiteText);
            app.createAccountView.asterixFive.setStyle(whiteText);
            app.createAccountView.createAccountForm.setStyle("-fx-background-color: #17181a");
            app.createAccountView.createAccountErrorField.setStyle("-fx-background-color: #17181a; -fx-border-color: red;-fx-border-radius: 5px; -fx-padding: 8px; -fx-text-fill: white");
        } else {
            app.createAccountView.firstNameField.setStyle(textFieldsDefault);
            app.createAccountView.lastNameField.setStyle(textFieldsDefault);
            app.createAccountView.emailField.setStyle(textFieldsDefault);
            app.createAccountView.passField.setStyle(textFieldsDefault);
            app.createAccountView.confirmPassField.setStyle(textFieldsDefault);

            app.createAccountView.welcomeTxtBox.setStyle(blackText);
            app.createAccountView.welcomeMsgTxtBox.setStyle(blackText);
            app.createAccountView.fnameTxtBox.setStyle(blackText);
            app.createAccountView.asterixOne.setStyle(blackText);
            app.createAccountView.asterixTwo.setStyle(blackText);
            app.createAccountView.lastnameTxtBox.setStyle(blackText);
            app.createAccountView.emailAddressTxtBox.setStyle(blackText);
            app.createAccountView.asterixThree.setStyle(blackText);
            app.createAccountView.passwordTxt.setStyle(blackText);
            app.createAccountView.asterixFour.setStyle(blackText);
            app.createAccountView.confirmPwordText.setStyle(blackText);
            app.createAccountView.asterixFive.setStyle(blackText);
            app.createAccountView.createAccountErrorField.setStyle("-fx-background-color: #ffffff; -fx-border-color: red;-fx-border-radius: 5px; -fx-padding: 8px; -fx-text-fill: black");
        }
    }
}
