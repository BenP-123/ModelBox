package com.modelbox.mongo;

import com.modelbox.app;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Text;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

import java.io.ByteArrayInputStream;

public class usersBridge {

    public String ownerId;

    public void handleGetCurrentUserAccountMenu(String currentUserAccountMenu) {
        try {
            BsonDocument currentUserDocument = BsonDocument.parse(currentUserAccountMenu);
            app.dashboard.displayNameTextField.setText(currentUserDocument.get("displayName").asString().getValue());
            app.dashboard.emailAddressTextField.setText(currentUserDocument.get("emailAddress").asString().getValue());
            app.dashboard.accountMenuPane.setVisible(!app.dashboard.accountMenuPane.visibleProperty().get());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void handleGetCurrentUserNotifications(String currentUserNotifications) {
        try {
            BsonArray userNotifications = BsonArray.parse(currentUserNotifications);

            app.dashboard.notificationsVBox.getChildren().clear();

            if (userNotifications.isEmpty()) {
                Text noNotifications = new Text("No notifications yet!");
                noNotifications.setStyle("-fx-fill: #ffffff; -fx-font-size: 18px; -fx-font-family: Arial;");
                app.dashboard.notificationsVBox.getChildren().add(noNotifications);
            } else {
                for (BsonValue notification : userNotifications) {

                }
            }

            app.dashboard.notificationsAnchorPane.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void handleGetCurrentUserProfile(String currentUserProfile) {
        try {
            BsonDocument currentUserDocument = BsonDocument.parse(currentUserProfile);

            app.profileView.editProfileBtn.setText("Edit profile");
            app.profileView.editProfileBtn.setStyle("-fx-background-color: #007be8;");
            app.profileView.displayNameTextField.setEditable(false);
            app.profileView.firstNameTextField.setEditable(false);
            app.profileView.lastNameTextField.setEditable(false);
            app.profileView.bioTextArea.setEditable(false);
            app.profileView.profilePictureImage.setVisible(true);
            app.profileView.addProfilePictureBtn.setVisible(false);
            app.profileView.cancelProfileUploadBtn.setVisible(false);

            app.profileView.displayNameTextField.setText(currentUserDocument.get("displayName").asString().getValue());
            app.profileView.firstNameTextField.setText(currentUserDocument.get("firstName").asString().getValue());
            app.profileView.lastNameTextField.setText(currentUserDocument.get("lastName").asString().getValue());
            String profileBioText = currentUserDocument.get("profileBio") == null ? "" : currentUserDocument.get("profileBio").asString().getValue();
            app.profileView.bioTextArea.setText(profileBioText);
            if (currentUserDocument.get("profilePicture") == null){
                app.profileView.profilePictureImage.setFill(new ImagePattern(new Image(String.valueOf(getClass().getResource("/images/empty-profile-pic.png")))));
            } else {
                app.profileView.profilePictureImage.setFill(new ImagePattern(new Image(new ByteArrayInputStream(currentUserDocument.get("profilePicture").asBinary().getData()))));
            }
            app.profileView.loadingAnchorPane.setVisible(false);
            app.profileView.profileContentAnchorPane.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void handleGetCurrentUserSettings(String currentUserSettings) {
        try {
            BsonDocument currentUserDocument = BsonDocument.parse(currentUserSettings);

            app.settingsView.displayNameTextField.setText(currentUserDocument.get("displayName").asString().getValue());
            if (currentUserDocument.get("profilePicture") == null){
                app.settingsView.settingsPictureImage.setFill(new ImagePattern(new Image(String.valueOf(getClass().getResource("/images/empty-profile-pic.png")))));
            } else {
                app.settingsView.settingsPictureImage.setFill(new ImagePattern(new Image(new ByteArrayInputStream(currentUserDocument.get("profilePicture").asBinary().getData()))));
            }
            app.settingsView.loadingAnchorPane.setVisible(false);
            app.settingsView.settingsContentAnchorPane.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
