package com.modelbox.mongo;

import com.modelbox.app;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.util.prefs.Preferences;

/**
 * Provides a connection mechanism to handle authentication-related callbacks from Javascript calls made in a JavaFX WebEngine
 * @see com.modelbox.app#mongoApp
 */
public class authBridge {

    private String logInStatus;
    private String forgotPasswordStatus;
    private String createAccountStatus;
    private String deleteAccountStatus;
    private String changeEmailStatus;

    /**
     * Gets the status of logging into the app
     * @return the String containing the status of logging into the app
     */
    public String getLogInStatus() {
        return logInStatus;
    }

    /**
     * Gets the status of issuing a forgot password email
     * @return the String containing the status of issuing a forgot password email
     */
    public String getForgotPasswordStatus() {
        return forgotPasswordStatus;
    }

    /**
     * Gets the status of creating a new account
     * @return the String containing the status of creating a new account
     */
    public String getCreateAccountStatus() {
        return createAccountStatus;
    }

    /**
     * Gets the status of deleting an existing account
     * @return the String containing the status of deleting an existing account
     */
    public String getDeleteAccountStatus() {
        return deleteAccountStatus;
    }

    /**
     * Gets the status of changing the current user's email
     * @return the String containing the status of changing the current user's email
     */
    public String getChangeEmailStatus() {
        return changeEmailStatus;
    }

    /**
     * Sets the status of logging into the app
     * @param status the String containing the current status of logging into the app
     */
    public void setLogInStatus(String status) {
        logInStatus = status;
    }

    /**
     * Sets the status of issuing a forgot password email
     * @param status the String containing the current status of issuing a forgot password email
     */
    public void setForgotPasswordStatus(String status) {
        forgotPasswordStatus = status;
    }

    /**
     * Sets the status of creating a new account
     * @param status the String containing the current status of creating a new account
     */
    public void setCreateAccountStatus(String status) {
        createAccountStatus = status;
    }

    /**
     * Sets the status of deleting an existing account
     * @param status the String containing the current status of deleting an existing account
     */
    public void setDeleteAccountStatus(String status) {
        deleteAccountStatus = status;
    }

    /**
     * Sets the status of changing the current user's email
     * @param status the String containing the current status of changing the current user's email
     */
    public void setChangeEmailStatus(String status) {
        changeEmailStatus = status;
    }

    /**
     * Checks the status of the current user and shows the dashboard if appropriate
     * @param ownerID the specific identifier for the current user
     * @param email the email address provided by the user at log in
     * @param password the password provided by the user at log in
     */
    public void handleLogInCurrentUser(String ownerID, String email, String password) {
        try {
            if (logInStatus.equals("success")) {
                app.users.ownerId = ownerID;

                Preferences prefs = Preferences.userRoot().node("/com/modelbox");
                String displayName = prefs.get("displayName", "null");
                String firstName = prefs.get("firstName", "null");
                String lastName = prefs.get("lastName", "null");
                String oldOwnerId = prefs.get("owner_id", "null");
                if (!displayName.equals("null") && !firstName.equals("null") && !lastName.equals("null")) {
                    String functionCall = String.format("ModelBox.Auth.initializeNewUser('%s', '%s', '%s');", displayName, firstName, lastName);
                    app.mongoApp.eval(functionCall);
                } else if (!oldOwnerId.equals("null")) {
                    String functionCall = String.format("ModelBox.Auth.initializeNewEmail('%s', '%s');", oldOwnerId, email);
                    app.mongoApp.eval(functionCall);
                } else {
                    setUpMyModelsView();
                }
            } else {
                app.loginView.loginErrorField.setText(logInStatus);
                app.loginView.loginErrorField.setVisible(true);

                // Clear the form and let the user try again
                app.loginView.emailField.setText("");
                app.loginView.passField.setText("");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Prepares and populates the dashboard for a new user
     */
    public void handleInitializeNewUser(){
        try {
            Preferences prefs = Preferences.userRoot().node("/com/modelbox");
            prefs.remove("displayName");
            prefs.remove("firstName");
            prefs.remove("lastName");

            setUpMyModelsView();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Prepares and populates the dashboard for a user who changed their email address
     */
    public void handleInitializeNewEmail(){
        try {
            Preferences prefs = Preferences.userRoot().node("/com/modelbox");
            prefs.remove("owner_id");
            setUpMyModelsView();
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Prepares the dashboard and 'My Models' view and shows the app's 'My Models' view
     */
    private void setUpMyModelsView() {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Parent root = app.viewLoader.load();
            app.dashboard = app.viewLoader.getController();
            app.dashboard.dashboardLine.endXProperty().bind(app.dashboard.dashboardAnchorPane.widthProperty());

            if (app.isDarkModeActive) {
                app.dashboard.dashboardAnchorPane.getStylesheets().add(getClass().getResource("/css/dark-mode.css").toString());
                app.dashboard.darkModeToggleSwitch.setSwitchedOnProperty(true);
            } else {
                app.dashboard.dashboardAnchorPane.getStylesheets().add(getClass().getResource("/css/light-mode.css").toString());
                app.dashboard.darkModeToggleSwitch.setSwitchedOnProperty(false);
            }
            app.loginView.emailField.getScene().setRoot(root);

            // Show the my models view
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/myModels.fxml"));
            Parent myModelsRoot = app.viewLoader.load();
            app.myModelsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(myModelsRoot);

            String functionCall = "ModelBox.Models.getCurrentUserModels();";
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Redirects the user to the 'Log In' view after logging out of their account
     */
    public void handleLogOutCurrentUser() {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/login.fxml"));
            Parent root = app.viewLoader.load();
            app.loginView = app.viewLoader.getController();
            app.dashboard.logOutBtn.getScene().setRoot(root);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Redirects the user to a confirmation view after issuing a change email address confirmation
     */
    public void handleChangeCurrentUserEmail() {
        try {
            if (changeEmailStatus.equals("success")) {
                app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/createAccount.fxml"));
                Parent root = app.viewLoader.load();
                app.createAccountView = app.viewLoader.getController();
                app.createAccountView.confirmSubHeading1.setText("Your email address has been successfully changed. You must confirm your email address before using ModelBox.");
                app.createAccountView.confirmSubHeading2.setText("Please check your email for a confirmation link. Once confirmed, log in on this device to complete your email change.");
                app.createAccountView.createAccountForm.setVisible(false);
                app.createAccountView.createAccountInstructions.setVisible(true);
                app.dashboard.logOutBtn.getScene().setRoot(root);
            } else {
                Preferences prefs = Preferences.userRoot().node("/com/modelbox");
                prefs.remove("owner_id");

                app.settingsView.changeEmailErrorField.setText(changeEmailStatus);
                app.settingsView.changeEmailErrorField.setVisible(true);

                // Clear the form and let the user try again
                app.settingsView.changeAccountEmailField.setText("");
                app.settingsView.changeEmailPasswordField.setText("");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Redirects the user to the 'Forgot Password' view so that they can change their password
     */
    public void handleChangeCurrentUserPassword() {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/forgotPassword.fxml"));
            Parent root = app.viewLoader.load();
            app.forgotPasswordView = app.viewLoader.getController();
            app.forgotPasswordView.forgotPasswordHeading.setText("Change your password");
            app.forgotPasswordView.forgotPasswordSubHeading.setText("Let us help you update your account password. Please enter your information below.");
            app.forgotPasswordView.logInPromptText.setText("Already like your password?");
            app.dashboard.logOutBtn.getScene().setRoot(root);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Modifies the 'Forgot Password' view accordingly once an email has been provided
     */
    public void handleSendPasswordResetEmail() {
        try {
            if (forgotPasswordStatus.equals("success")) {
                app.forgotPasswordView.sendResetEmailPrompt.setVisible(false);
                app.forgotPasswordView.passwordResetInstructions.setVisible(true);
            } else {
                app.forgotPasswordView.forgotPasswordErrorField.setText(forgotPasswordStatus);
                app.forgotPasswordView.forgotPasswordErrorField.setVisible(true);

                // Clear the form and let the user try again
                app.forgotPasswordView.emailField.setText("");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Modifies the 'Create Account' view accordingly once the appropriate information has been provided
     */
    public void handleRegisterNewUser() {
        try {
            if (createAccountStatus.equals("success")) {
                app.createAccountView.createAccountForm.setVisible(false);
                app.createAccountView.createAccountInstructions.setVisible(true);
            } else {
                app.createAccountView.createAccountErrorField.setText(createAccountStatus);
                app.createAccountView.createAccountErrorField.setVisible(true);

                // Clear the form and let the user try again
                app.createAccountView.firstNameField.setText("");
                app.createAccountView.lastNameField.setText("");
                app.createAccountView.emailField.setText("");
                app.createAccountView.passField.setText("");
                app.createAccountView.confirmPassField.setText("");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Redirects the user to an account deletion confirmation view if their account was successfully deleted. Otherwise,
     * the user can attempt to delete their account again.
     */
    public void handleDeleteCurrentUser() {
        try {
            if (deleteAccountStatus.equals("success")) {
                app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/accountDeleted.fxml"));
                Parent root = app.viewLoader.load();
                app.accountDeletedView = app.viewLoader.getController();
                app.dashboard.logOutBtn.getScene().setRoot(root);
            } else {
                app.settingsView.deleteConfirmationPopUp.setVisible(false);
                app.settingsView.deleteAccountErrorField.setText(deleteAccountStatus);
                app.settingsView.deleteAccountErrorField.setVisible(true);

                // Clear the email field and let the user try again
                app.settingsView.deleteAccountEmailField.setText("");
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
