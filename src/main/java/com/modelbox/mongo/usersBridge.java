package com.modelbox.mongo;

import com.modelbox.app;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static com.mongodb.client.model.Filters.eq;

public class usersBridge {

    public String ownerId;
    public MongoCollection<Document> usersCollection;

    public void handleGetCurrentUserAccountMenu(String displayName, String emailAddress) {
        try {
            app.dashboard.displayNameTextField.setText(displayName);
            app.dashboard.emailAddressTextField.setText(emailAddress);
            app.dashboard.accountMenuPane.setVisible(!app.dashboard.accountMenuPane.visibleProperty().get());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void handleGetCurrentUserProfile(String displayName, String firstName, String lastName, String profileBio, String profilePicture) {
        try {
            app.profileView.editProfileBtn.setText("Edit profile");
            app.profileView.editProfileBtn.setStyle("-fx-background-color: #007be8;");
            app.profileView.displayNameTextField.setEditable(false);
            app.profileView.firstNameTextField.setEditable(false);
            app.profileView.lastNameTextField.setEditable(false);
            app.profileView.bioTextArea.setEditable(false);
            app.profileView.profilePictureImage.setVisible(true);
            app.profileView.addProfilePictureBtn.setVisible(false);
            app.profileView.cancelProfileUploadBtn.setVisible(false);

            app.profileView.displayNameTextField.setText(displayName);
            app.profileView.firstNameTextField.setText(firstName);
            app.profileView.lastNameTextField.setText(lastName);
            app.profileView.bioTextArea.setText(profileBio);
            if (profilePicture.equals("")){
                app.profileView.profilePictureImage.setFill(new ImagePattern(new Image(String.valueOf(getClass().getResource("/images/empty-profile-pic.png")))));
            } else {
                byte[] decodedProfilePicture = Base64.getDecoder().decode(profilePicture.getBytes("UTF-8"));
                app.profileView.profilePictureImage.setFill(new ImagePattern(new Image(new ByteArrayInputStream(decodedProfilePicture))));
            }
            app.profileView.loadingAnchorPane.setVisible(false);
            app.profileView.profileContentAnchorPane.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void handleGetCurrentUserSettings(String displayName, String profilePicture) {
        try {
            app.settingsView.displayNameTextField.setText(displayName);
            if (profilePicture.equals("")){
                app.settingsView.settingsPictureImage.setFill(new ImagePattern(new Image(String.valueOf(getClass().getResource("/images/empty-profile-pic.png")))));
            } else {
                byte[] decodedProfilePicture = Base64.getDecoder().decode(profilePicture.getBytes("UTF-8"));
                app.settingsView.settingsPictureImage.setFill(new ImagePattern(new Image(new ByteArrayInputStream(decodedProfilePicture))));
            }
            app.settingsView.loadingAnchorPane.setVisible(false);
            app.settingsView.settingsContentAnchorPane.setVisible(true);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Using the MongoDB driver, sets the profile info of the currently logged in user
     *
     *
     * @param displayName a String containing the display name for the current user
     * @param firstName a String containing the first name for the current user
     * @param lastName a String containing the last name for the current user
     * @param profileBio a String containing the profile bio for the current user
     * @param profilePicture a byte[] containing the new file contents of the picture to use for the current user
     */
    public void setCurrentUserProfileInfo(String displayName, String firstName, String lastName, String profileBio, byte[] profilePicture) {
        Bson filter = eq("owner_id", app.users.ownerId);
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        app.users.usersCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                Document newValues = new Document("displayName", displayName).append("firstName", firstName)
                        .append("lastName", lastName).append("profileBio", profileBio);

                if (profilePicture != null) {
                    newValues.append("profilePicture", new BsonBinary(profilePicture));
                }
                Bson updatedValue = newValues;
                Bson updatedOperation = new Document("$set", updatedValue);
                subscribers.OperationSubscriber<UpdateResult> updateOneSubscriber = new subscribers.OperationSubscriber<>();
                usersCollection.updateOne(found, updatedOperation).subscribe(updateOneSubscriber);
                updateOneSubscriber.await();
            }

            String functionCall = String.format("ModelBox.UserIO.getCurrentUserProfile();");
            app.mongoApp.eval(functionCall);

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

/* Edited out, working on getting user info for collaboration
    public ArrayList getUserInfo(ArrayList collaborators) {
        Bson filter = Filters.in("owner_id", collaborators);

        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        app.users.usersCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                return (ArrayList)found.get("displayName");
            }


        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return new ArrayList<>();
    }

 */
}
