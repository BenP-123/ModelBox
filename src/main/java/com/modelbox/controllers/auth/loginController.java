package com.modelbox.controllers.auth;

import com.modelbox.app;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class loginController {

    @FXML public TextField loginErrorField;
    @FXML public TextField emailField;
    @FXML public PasswordField passField;
    @FXML public AnchorPane loginAnchorPane;
    @FXML public AnchorPane loginPane;
    @FXML  public Text welcomeHeader;
    @FXML public Text welcomeHeaderTwo;
    @FXML public Text emailAddressLabel;
    @FXML public Text passwordLabel;
    @FXML  public Button loginBtn;
    @FXML public Text requiredLabel;
    @FXML public Text asterixTwo;
    @FXML public Text asterixOne;
    @FXML private Button forgotPassBtn;
    @FXML private Button createAccountBtn;
    @FXML private CheckBox checkBox;
    @FXML private TextField passwordPlainTxt;

    private String whiteText = "-fx-fill: white";
    private String blackText = "-fx-fill: black";
    private String textFields = "-fx-background-color: transparent; -fx-border-color: white; -fx-border-radius: 4px; -fx-padding: 8px; -fx-text-fill: white";
    private String textFieldsDefault = "-fx-background-color: #ffffff; -fx-border-color: #C4C4C4; -fx-border-radius: 4px; -fx-padding: 8px; -fx-text-fill: black; -fx-faint-focus-color: transparent";


    /**
     * Handles the user login when the login button is clicked
     *
     * @param  event    a JavaFX Event
     */
    @FXML
    private void loginBtnClicked(Event event) {
        loginCurrentUser();
    }

    /**
     * Handles the user login when the enter key is pressed on the last field
     *
     * @param  event  a JavaFX KeyEvent
     */
    @FXML
    private void loginEnterKeyPressed(KeyEvent event) {
        if(event.getCode().equals((KeyCode.ENTER))) {
            loginCurrentUser();
        }
    }

    /**
     * Logs the user in and redirects the user to the dashboard
     *
     */
    public void loginCurrentUser() {
        try {
            String functionCall = String.format("ModelBox.Auth.logInCurrentUser('%s', '%s');", emailField.getText(), passField.getText());
            app.mongoApp.eval(functionCall);
        } catch(Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the forgot password view
     *
     * @param  event    a JavaFX Event
     */
    @FXML
    private void forgotPassBtnClicked(Event event) {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/forgotPassword.fxml"));
            Parent root = app.viewLoader.load();
            app.forgotPasswordView = app.viewLoader.getController();
            app.forgotPasswordView.forgotPasswordDarkMode();
            forgotPassBtn.getScene().setRoot(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }

    /**
     * Handles the UI redirect to the create account view
     *
     * @param  event    a JavaFX Event
     */
    @FXML
    private void createAccountBtnClicked(Event event) {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/createAccount.fxml"));
            Parent root = app.viewLoader.load();
            app.createAccountView = app.viewLoader.getController();
            app.createAccountView.createAccountDarkMode();
            createAccountBtn.getScene().setRoot(root);
        } catch (Exception exception){
            // Handle errors
            exception.printStackTrace();
        }
    }


    /**
     * Toggles the visibility of the entered password from masked to plain text.
     *
     * @param  actionEvent   a JavaFX Event
     */
    public void checkBoxSelected(ActionEvent actionEvent) {
            if (checkBox.isSelected()) {
                passwordPlainTxt.setText(passField.getText());
                passwordPlainTxt.setVisible(true);
                passField.setVisible(false);
                return;
            }
            passField.setText(passwordPlainTxt.getText());
            passField.setVisible(true);
            passwordPlainTxt.setVisible(false);
    }

    public void loginDarkMode(){
        if(app.viewMode){
            app.loginView.emailField.setStyle(textFields);
            app.loginView.passField.setStyle(textFields);
            app.loginView.passwordPlainTxt.setStyle(textFields);
            app.loginView.welcomeHeader.setStyle(whiteText);
            app.loginView.welcomeHeaderTwo.setStyle(whiteText);
            app.loginView.asterixOne.setStyle(whiteText);
            app.loginView.emailAddressLabel.setStyle(whiteText);
            app.loginView.asterixTwo.setStyle(whiteText);
            app.loginView.passwordLabel.setStyle(whiteText);
            app.loginView.loginPane.setStyle("-fx-background-color: #17181a");
            app.loginView.loginErrorField.setStyle("-fx-background-color: #17181a; -fx-border-color: red;-fx-border-radius: 5px; -fx-padding: 8px; -fx-text-fill: #ffffff");
        }else{
            app.loginView.passwordPlainTxt.setStyle(textFieldsDefault);
            app.loginView.emailField.setStyle(textFieldsDefault);
            app.loginView.passField.setStyle(textFieldsDefault);
            app.loginView.welcomeHeader.setStyle(blackText);
            app.loginView.welcomeHeaderTwo.setStyle(blackText);
            app.loginView.asterixOne.setStyle(blackText);
            app.loginView.emailAddressLabel.setStyle(blackText);
            app.loginView.asterixTwo.setStyle(blackText);
            app.loginView.passwordLabel.setStyle(blackText);
            app.loginView.loginPane.setStyle("-fx-background-color: none");
            app.loginView.loginErrorField.setStyle("-fx-background-color: #ffffff; -fx-border-color: red;-fx-border-radius: 5px; -fx-padding: 8px; -fx-text-fill: black");
        }
    }
}
