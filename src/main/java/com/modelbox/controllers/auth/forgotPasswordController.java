package com.modelbox.controllers.auth;

import com.modelbox.app;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class forgotPasswordController {

    @FXML public TextField emailField;
    @FXML public AnchorPane forgotPassPane;
    @FXML public Text emailAddressLabel;
    @FXML public Text asterixOne;
    @FXML public Button sendEmailLoginBtn;
    @FXML private Button loginBtn;
    @FXML public AnchorPane sendResetEmailPrompt;
    @FXML public AnchorPane passwordResetInstructions;
    @FXML public TextField forgotPasswordErrorField;
    @FXML public Text forgotPasswordHeading;
    @FXML public Text forgotPasswordSubHeading;
    @FXML public Text logInPromptText;

    private String whiteText = "-fx-fill: white";
    private String blackText = "-fx-fill: black";
    private String textFields = "-fx-background-color: transparent; -fx-border-color: white; -fx-border-radius: 4px; -fx-padding: 8px; -fx-text-fill: white";
    private String textFieldsDefault = "-fx-background-color: #ffffff; -fx-border-color: #C4C4C4; -fx-border-radius: 4px; -fx-padding: 8px; -fx-text-fill: black; -fx-faint-focus-color: transparent";



    /**
     * Handles resetting the user's password when the reset password button is clicked
     *
     * @param  event a JavaFX Event
     */
    @FXML
    private void resetPassBtnClicked(Event event) {
        resetUserPassword();
    }

    /**
     * Handles resetting the user's password when the enter key is pressed on the last field
     *
     * @param  event  a JavaFX KeyEvent
     */
    @FXML
    private void resetPassEnterKeyPressed(KeyEvent event) {
        if(event.getCode().equals((KeyCode.ENTER))) {
            resetUserPassword();
        }
    }

    /**
     * Attempts to reset the user's password using the information provided in the forgotPassword view
     * and modifies the view accordingly to handle errors
     *
     */
    private void resetUserPassword() {
        try {
            String functionCall = String.format("ModelBox.Auth.sendPasswordResetEmail('%s');", emailField.getText());
            app.mongoApp.eval(functionCall);
        } catch(Exception exception){
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
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    public void forgotPasswordDarkMode(){
        if(app.viewMode){
            app.forgotPasswordView.emailField.setStyle(textFields);
            app.forgotPasswordView.forgotPasswordHeading.setStyle(whiteText);
            app.forgotPasswordView.forgotPasswordSubHeading.setStyle(whiteText);
            app.forgotPasswordView.asterixOne.setStyle(whiteText);
            app.forgotPasswordView.emailAddressLabel.setStyle(whiteText);
            app.forgotPasswordView.sendResetEmailPrompt.setStyle("-fx-background-color: #17181a");
        }else{
            app.forgotPasswordView.forgotPasswordHeading.setStyle(blackText);
            app.forgotPasswordView.forgotPasswordSubHeading.setStyle(blackText);
            app.forgotPasswordView.asterixOne.setStyle(blackText);
            app.forgotPasswordView.emailAddressLabel.setStyle(blackText);
            app.forgotPasswordView.sendResetEmailPrompt.setStyle("-fx-background-color: none");
        }
    }


}
