package com.modelbox.controllers;

import com.modelbox.auth.logIn;
import com.modelbox.databaseIO.modelsIO;
import com.modelbox.databaseIO.usersIO;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

public class loginController {

    public static dashboardController dashboard;
    public static logIn activeLogin;
    private FXMLLoader dashboardLoader;
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private Button loginBtn;
    @FXML private Button forgotPassBtn;
    @FXML private Button createAccountBtn;
    @FXML private AnchorPane loginAnchorPane;
    @FXML private Pane loginErrorPopout;

    /**
     * Verifies a ModelBox user using the facilities provided from the auth package and redirects the UI
     * to the dashboard. The dashboard.fxml document is loaded and set as the root node of the current scene.
     *
     * @param  e    a JavaFX event with the properties and methods of the element that triggered the event
     * @return      nothing of value is returned
     */
    @FXML
    private void loginBtnClicked(Event e) {
        try {
            activeLogin = new logIn();
            activeLogin.setEmailAddress(emailField == null ? "" : emailField.getText());
            activeLogin.setPassword(passField == null ? "" : passField.getText());

            // Run verification checks (do them as logical ands in the condition once the methods are implemented)
            if (activeLogin.areRequiredFieldsMet()){

                loginErrorPopout.setVisible(false);

                // Attempt to log the user in
                if (activeLogin.logUserIn() == 0) {
                    dashboardLoader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
                    Parent root = (Parent) dashboardLoader.load();
                    dashboard = dashboardLoader.getController();
                    loginBtn.getScene().setRoot(root);

                    // Prep the view
                    dashboard.dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/myModels.fxml"));
                    Parent myModelsRoot = (Parent) dashboard.dashboardViewLoader.load();
                    dashboard.myModelsView = dashboard.dashboardViewLoader.getController();
                    dashboard.dashViewsAnchorPane.getChildren().setAll(myModelsRoot);

                    // Clear all the UI cards from the myModelsFlowPane on the myModelsView
                    loginController.dashboard.myModelsView.myModelsFlowPane.getChildren().clear();

                    // Clear the byte[] list with the old models stored
                    loginController.dashboard.dbModelsList.clear();

                    // Get the updated list of byte[]'s from the database
                    modelsIO.getMyModels(usersIO.getOwnerID());

                    // Perform a check to make sure the list isn't empty
                    if (loginController.dashboard.dbModelsList.isEmpty()){
                        loginController.dashboard.myModelsView.myModelsScrollPane.setVisible(false);
                        loginController.dashboard.myModelsView.noModelsBtn.setVisible(true);
                    } else {

                        // Add the UI cards for the myModels view
                        for (byte[] model : loginController.dashboard.dbModelsList) {
                            loginController.dashboard.myModelsView.addMyModelsPreviewCard(model);
                        }

                        loginController.dashboard.myModelsView.noModelsBtn.setVisible(false);
                        loginController.dashboard.myModelsView.myModelsScrollPane.setVisible(true);
                    }
                } else {
                    // Clear the login fields
                    emailField.setText("");
                    passField.setText("");
                }
            } else {
                loginErrorPopout.setVisible(true);
            }

        } catch(Exception exception){
            // Handle errors
            System.err.println(exception);
        }
    }

    /**
     * Verifies a ModelBox user using the facilities provided from the auth package and redirects the UI
     * to the dashboard. The dashboard.fxml document is loaded and set as the root node of the current scene.
     *
     * @param  event  a JavaFX event with the properties and methods of the element that triggered the event
     * @return        nothing of value is returned
     */
    @FXML
    private void loginEnterKeyPressed(KeyEvent event) {
        if(event.getCode().equals((KeyCode.ENTER))) {
            try {
                activeLogin = new logIn();
                activeLogin.setEmailAddress(emailField == null ? "" : emailField.getText());
                activeLogin.setPassword(passField == null ? "" : passField.getText());

                // Run verification checks (do them as logical ands in the condition once the methods are implemented)
                if (activeLogin.areRequiredFieldsMet()){

                    loginErrorPopout.setVisible(false);

                    // Attempt to log the user in
                    if (activeLogin.logUserIn() == 0) {
                        dashboardLoader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
                        Parent root = (Parent) dashboardLoader.load();
                        dashboard = dashboardLoader.getController();
                        loginBtn.getScene().setRoot(root);

                        dashboard.dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/myModels.fxml"));
                        Parent myModelsRoot = (Parent) dashboard.dashboardViewLoader.load();
                        dashboard.myModelsView = dashboard.dashboardViewLoader.getController();
                        dashboard.dashViewsAnchorPane.getChildren().setAll(myModelsRoot);

                        modelsIO.getMyModels(usersIO.getOwnerID());

                        // Modify UI accordingly
                        if (dashboard.dbModelsList.isEmpty()) {
                            dashboard.myModelsView.myModelsScrollPane.setVisible(false);
                            dashboard.myModelsView.noModelsBtn.setVisible(true);
                        } else {
                            dashboard.myModelsView.myModelsFlowPane.getChildren().clear();

                            for (byte[] model : dashboard.dbModelsList) {
                                dashboard.myModelsView.addMyModelsPreviewCard(model);
                            }

                            dashboard.myModelsView.noModelsBtn.setVisible(false);
                            dashboard.myModelsView.myModelsScrollPane.setVisible(true);
                        }
                    } else {
                        // Clear the login fields
                        emailField.setText("");
                        passField.setText("");
                    }

                } else {
                    loginErrorPopout.setVisible(true);
                }

            } catch(Exception exception){
                // Handle errors
                System.err.println(exception);
            }
        }
    }

    /**
     * The application UI is redirected to the login view by loading and setting the login.fxml document as
     * the root node of the current scene.
     *
     * @param  e    a JavaFX event with the properties and methods of the element that triggered the event
     * @return      nothing of value is returned
     */
    @FXML
    private void forgotPassBtnClicked(Event e) {
        // Load and show the login fxml document if the user already has an account
        forgotPasswordController forgotPassController = new forgotPasswordController();
        FXMLLoader forgotPassLoader = new FXMLLoader();
        forgotPassLoader.setController(forgotPassController);

        try {
            Parent root = forgotPassLoader.load(getClass().getResource("/views/forgotPassword.fxml"));
            forgotPassBtn.getScene().setRoot(root);
        } catch (Exception loadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }

    /**
     * The application UI is redirected to the create account view by loading and setting the createAccount.fxml
     * document as the root node of the current scene.
     *
     * @param  e    a JavaFX event with the properties and methods of the element that triggered the event
     * @return      nothing of value is returned
     */
    @FXML
    private void createAccountBtnClicked(Event e) {
        // Load and show the create account fxml document
        createAccountController createController = new createAccountController();
        FXMLLoader createAccountLoader = new FXMLLoader();
        createAccountLoader.setController(createController);

        try {
            Parent root = createAccountLoader.load(getClass().getResource("/views/createAccount.fxml"));
            createAccountBtn.getScene().setRoot(root);
        } catch (Exception loadException){
            // Handle exception if fxml document fails to load and show properly
        }
    }
}
