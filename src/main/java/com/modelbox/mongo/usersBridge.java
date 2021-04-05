package com.modelbox.mongo;

import com.modelbox.app;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;

import java.io.ByteArrayInputStream;
import java.util.Base64;

public class usersBridge {

    public String ownerId;

    public void handleGetCurrentUserAccountMenu(String currentUserAccountMenu) {
        try {
            BsonDocument currentUserDocument = BsonDocument.parse(currentUserAccountMenu);
            app.dashboard.displayNameTextField.setText(currentUserDocument.get("displayName").asString().getValue());
            app.dashboard.emailAddressTextField.setText(currentUserDocument.get("emailAddress").asString().getValue());
            app.dashboard.navigationMenuPane.setVisible(false);
            app.dashboard.notificationsAnchorPane.setVisible(false);
            app.dashboard.accountMenuPane.setVisible(true);
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
                noNotifications.setWrappingWidth(250);
                noNotifications.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                Line separator = new Line();
                separator.setStartX(0);
                separator.setEndX(275);
                separator.setStroke(Color.WHITE);
                separator.setStrokeWidth(1.25);
                app.dashboard.notificationsVBox.getChildren().add(noNotifications);
                app.dashboard.notificationsVBox.getChildren().add(1, separator);
            } else {
                for (int i = userNotifications.size() - 1; i >= 0; i--) {
                    Text notificationMessage = new Text(userNotifications.get(i).asString().getValue());
                    notificationMessage.setWrappingWidth(250);
                    notificationMessage.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                    Line separator = new Line();
                    separator.setStartX(0);
                    separator.setEndX(275);
                    separator.setStroke(Color.WHITE);
                    separator.setStrokeWidth(1.25);
                    app.dashboard.notificationsVBox.getChildren().add(notificationMessage);
                    app.dashboard.notificationsVBox.getChildren().add(separator);
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
                byte[] decodedProfilePicture = Base64.getDecoder().decode(currentUserDocument.get("profilePicture").asBinary().getData());
                app.profileView.profilePictureImage.setFill(new ImagePattern(new Image(new ByteArrayInputStream(decodedProfilePicture))));
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
                byte[] decodedProfilePicture = Base64.getDecoder().decode(currentUserDocument.get("profilePicture").asBinary().getData());
                app.settingsView.settingsPictureImage.setFill(new ImagePattern(new Image(new ByteArrayInputStream(decodedProfilePicture))));
            }
            app.settingsView.loadingAnchorPane.setVisible(false);
            app.settingsView.settingsContentAnchorPane.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
