package com.modelbox.mongo;

import com.modelbox.app;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import java.io.ByteArrayInputStream;

/**
 * Provides a connection mechanism to handle user-related callbacks from Javascript calls made in a JavaFX WebEngine
 * @see com.modelbox.app#mongoApp
 */
public class usersBridge {

    public String ownerId;

    /**
     * Prepares and populates the appropriate fields on the account menu and shows the app's account menu
     * @param currentUserAccountMenu a serialized BSON document containing database information to be shown
     */
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

    /**
     * Prepares and populates the appropriate fields on the notifications pane and subsequently shows the pane
     * @param currentUserNotifications a serialized BSON array containing database information to be shown
     */
    public void handleGetCurrentUserNotifications(String currentUserNotifications) {
        try {
            BsonArray userNotifications = BsonArray.parse(currentUserNotifications);
            app.dashboard.notificationsVBox.getChildren().clear();
            if (userNotifications.isEmpty()) {
                Text noNotifications = new Text("No notifications yet!");
                Line separator = addNotificationToPane(noNotifications);
                app.dashboard.notificationsVBox.getChildren().add(1, separator);
            } else {
                for (int i = userNotifications.size() - 1; i >= 0; i--) {
                    Text notificationMessage = new Text(userNotifications.get(i).asString().getValue());
                    Line separator = addNotificationToPane(notificationMessage);
                    app.dashboard.notificationsVBox.getChildren().add(separator);
                }
            }

            app.dashboard.notificationsAnchorPane.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Creates and appends a notification message on the notifications pane
     * @param notificationMessage the desired Text that the notification should contain
     * @return the JavaFX Line that will act as a divider after the notification text
     */
    private Line addNotificationToPane(Text notificationMessage) {
        notificationMessage.setWrappingWidth(250);
        notificationMessage.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

        Line separator = new Line();
        separator.setStartX(0);
        separator.setEndX(275);
        separator.setStroke(Color.WHITE);
        separator.setStrokeWidth(1.25);
        app.dashboard.notificationsVBox.getChildren().add(notificationMessage);

        return separator;
    }

    /**
     * Prepares and populates the appropriate fields on the 'Profile' view and shows the app's 'Profile' view
     * @param currentUserProfile a serialized BSON document containing database information to be shown
     */
    public void handleGetCurrentUserProfile(String currentUserProfile) {
        try {
            BsonDocument currentUserDocument = BsonDocument.parse(currentUserProfile);
            app.profileView.editProfileBtn.setText("Edit profile");
            if (!app.isDarkModeActive) {
                app.profileView.editProfileBtn.setStyle("-fx-background-color: #007be8;");
            }
            app.profileView.displayNameTextField.setEditable(false);
            app.profileView.firstNameTextField.setEditable(false);
            app.profileView.lastNameTextField.setEditable(false);
            app.profileView.bioTextArea.setEditable(false);
            app.profileView.profilePictureCircle.setVisible(true);
            app.profileView.profilePictureHBox.setVisible(true);
            app.profileView.profilePictureHBox.setClip(new Circle(app.profileView.profilePictureHBox.getWidth()/2, 100, 100));
            app.profileView.profilePictureImageView.setVisible(true);
            app.profileView.addProfilePictureBtn.setVisible(false);
            app.profileView.cancelProfileUploadBtn.setVisible(false);

            app.profileView.displayNameTextField.setText(currentUserDocument.get("displayName").asString().getValue());
            app.profileView.firstNameTextField.setText(currentUserDocument.get("firstName").asString().getValue());
            app.profileView.lastNameTextField.setText(currentUserDocument.get("lastName").asString().getValue());
            String profileBioText = currentUserDocument.get("profileBio") == null ? "" : currentUserDocument.get("profileBio").asString().getValue();
            app.profileView.bioTextArea.setText(profileBioText);
            if (currentUserDocument.get("profilePicture") == null){
                app.profileView.profilePictureImageView.setImage(new Image(String.valueOf(getClass().getResource("/images/empty-profile-pic.png"))));
            } else {
                byte[] decodedProfilePicture = currentUserDocument.get("profilePicture").asBinary().getData();
                app.profileView.profilePictureImageView.setImage(new Image(new ByteArrayInputStream(decodedProfilePicture)));
            }
            app.profileView.loadingAnchorPane.setVisible(false);
            app.profileView.profileContentAnchorPane.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Prepares and populates the appropriate fields on the 'Settings' view and shows the app's 'Settings' view
     * @param currentUserSettings a serialized BSON document containing database information to be shown
     */
    public void handleGetCurrentUserSettings(String currentUserSettings) {
        try {
            BsonDocument currentUserDocument = BsonDocument.parse(currentUserSettings);

            app.settingsView.displayNameTextField.setText(currentUserDocument.get("displayName").asString().getValue());
            if (currentUserDocument.get("profilePicture") == null){
                app.settingsView.settingsPictureHBox.setClip(new Circle(app.settingsView.settingsPictureHBox.getWidth()/2, 25, 25));
                app.settingsView.settingsPictureImageView.setImage(new Image(String.valueOf(getClass().getResource("/images/empty-profile-pic.png"))));
            } else {
                byte[] decodedProfilePicture = currentUserDocument.get("profilePicture").asBinary().getData();
                app.settingsView.settingsPictureHBox.setClip(new Circle(app.settingsView.settingsPictureHBox.getWidth()/2, 25, 25));
                app.settingsView.settingsPictureImageView.setImage(new Image(new ByteArrayInputStream(decodedProfilePicture)));
            }
            app.settingsView.changeEmailTabBtn.setStyle("-fx-background-color: #eeeeee; -fx-border-color: #868686; -fx-background-radius: 5 5 0 0; -fx-border-radius: 5 5 0 0; -fx-alignment: center-left;");
            app.settingsView.loadingAnchorPane.setVisible(false);
            app.settingsView.settingsContentAnchorPane.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
