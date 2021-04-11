package com.modelbox.mongo;

import com.modelbox.app;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import java.util.prefs.Preferences;

public class authBridge {

    private String logInStatus;
    private String forgotPasswordStatus;
    private String createAccountStatus;
    private String deleteAccountStatus;
    private String changeEmailStatus;

    public String getLogInStatus() {
        return logInStatus;
    }

    public String getForgotPasswordStatus() {
        return forgotPasswordStatus;
    }

    public String getCreateAccountStatus() {
        return createAccountStatus;
    }

    public String getDeleteAccountStatus() {
        return deleteAccountStatus;
    }

    public String getChangeEmailStatus() {
        return changeEmailStatus;
    }

    public void setLogInStatus(String status) {
        logInStatus = status;
    }

    public void setForgotPasswordStatus(String status) {
        forgotPasswordStatus = status;
    }

    public void setCreateAccountStatus(String status) {
        createAccountStatus = status;
    }

    public void setDeleteAccountStatus(String status) {
        deleteAccountStatus = status;
    }

    public void setChangeEmailStatus(String status) {
        changeEmailStatus = status;
    }

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
            // Handle errors
            exception.printStackTrace();
        }
    }

    public void handleInitializeNewUser(){
        try {
            Preferences prefs = Preferences.userRoot().node("/com/modelbox");
            prefs.remove("displayName");
            prefs.remove("firstName");
            prefs.remove("lastName");

            setUpMyModelsView();
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }

    }

    public void handleInitializeNewEmail(){
        try {
            Preferences prefs = Preferences.userRoot().node("/com/modelbox");
            prefs.remove("owner_id");

            setUpMyModelsView();
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }

    }

    private void setUpMyModelsView() {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Parent root = (Parent) app.viewLoader.load();
            app.dashboard = app.viewLoader.getController();
            app.loginView.emailField.getScene().setRoot(root);

            // Show the my models view
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/myModels.fxml"));
            Parent myModelsRoot = (Parent) app.viewLoader.load();
            app.myModelsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(myModelsRoot);

            // Asynchronously populate the my models view and show appropriate nodes when ready
            String functionCall = String.format("ModelBox.Models.getCurrentUserModels();");
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }

    }

    public void handleLogOutCurrentUser() {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/auth/login.fxml"));
            Parent root = app.viewLoader.load();
            app.loginView = app.viewLoader.getController();
            app.dashboard.logOutBtn.getScene().setRoot(root);
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }
    }

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
            // Handle errors
            exception.printStackTrace();
        }
    }

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
            // Handle errors
            exception.printStackTrace();
        }
    }

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
            // Handle errors
            exception.printStackTrace();
        }
    }

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
            // Handle errors
            exception.printStackTrace();
        }
    }
}
