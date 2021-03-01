package com.modelbox.controllers;

import com.modelbox.auth.logIn;
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

import java.io.File;

public class loginController {

    public static dashboardController dashboard;
    public static logIn activeLogin = new logIn();
    private FXMLLoader dashboardLoader;
    @FXML private TextField emailField;
    @FXML private PasswordField passField;
    @FXML private Button loginBtn;
    @FXML private Button forgotPassBtn;
    @FXML private Button createAccountBtn;
    @FXML private AnchorPane loginAnchorPane;

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
            activeLogin.setPassword(passField.getText());
            activeLogin.setEmailAddress(emailField.getText());

            int handleLogin = activeLogin.logUserIn();

            if (handleLogin == 0) {
                dashboardLoader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
                Parent root = (Parent) dashboardLoader.load();
                dashboard = dashboardLoader.getController();
                loginBtn.getScene().setRoot(root);

                dashboard.dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/myModels.fxml"));
                Parent myModelsRoot = (Parent) dashboard.dashboardViewLoader.load();
                dashboard.myModelsView = dashboard.dashboardViewLoader.getController();
                dashboard.dashViewsAnchorPane.getChildren().setAll(myModelsRoot);

                // Modify UI accordingly
                if (dashboard.myModelsList.isEmpty()) {
                    dashboard.myModelsView.myModelsScrollPane.setVisible(false);
                    dashboard.myModelsView.noModelsBtn.setVisible(true);
                } else {
                    dashboard.myModelsView.myModelsFlowPane.getChildren().clear();

                    for (File model : dashboard.myModelsList) {
                        dashboard.myModelsView.addMyModelsPreviewCard(model);
                    }

                    dashboard.myModelsView.noModelsBtn.setVisible(false);
                    dashboard.myModelsView.myModelsScrollPane.setVisible(true);
                }
            }
            else {
                loginController signInController = new loginController();
                FXMLLoader loginLoader = new FXMLLoader();
                loginLoader.setController(signInController);
                Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
                loginBtn.getScene().setRoot(root);
                loginFailed();
            }
            } catch(Exception loadException){
                // Handle exception if
            // fxml document fails to load and show properly
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
                    activeLogin.setPassword(passField.getText());
                    activeLogin.setEmailAddress(emailField.getText());
                    int handleLogin = activeLogin.logUserIn();

                    if (handleLogin == 0) {
                        dashboardLoader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
                        Parent root = (Parent) dashboardLoader.load();
                        dashboard = dashboardLoader.getController();
                        loginBtn.getScene().setRoot(root);

                        dashboard.dashboardViewLoader = new FXMLLoader(getClass().getResource("/views/myModels.fxml"));
                        Parent myModelsRoot = (Parent) dashboard.dashboardViewLoader.load();
                        dashboard.myModelsView = dashboard.dashboardViewLoader.getController();
                        dashboard.dashViewsAnchorPane.getChildren().setAll(myModelsRoot);

                        // Modify UI accordingly
                        if (dashboard.myModelsList.isEmpty()) {
                            dashboard.myModelsView.myModelsScrollPane.setVisible(false);
                            dashboard.myModelsView.noModelsBtn.setVisible(true);
                        } else {
                            dashboard.myModelsView.myModelsFlowPane.getChildren().clear();

                            for (File model : dashboard.myModelsList) {
                                dashboard.myModelsView.addMyModelsPreviewCard(model);
                            }

                            dashboard.myModelsView.noModelsBtn.setVisible(false);
                            dashboard.myModelsView.myModelsScrollPane.setVisible(true);
                        }
                    }
                 else {
                     loginController signInController = new loginController();
                     FXMLLoader loginLoader = new FXMLLoader();
                     loginLoader.setController(signInController);
                     Parent root = FXMLLoader.load(getClass().getResource("/views/login.fxml"));
                     loginBtn.getScene().setRoot(root);
                     loginFailed();

                }
            } catch (Exception loadException) {
                // Handle exception if fxml document fails to load and show properly
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

    private void loginFailed(){
        System.out.println("Login Failed");
    }
}
