package com.modelbox.databaseIO;

import com.modelbox.controllers.loginController;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.eq;

public class usersIO {

    public static MongoCollection<Document> usersCollection = loginController.activeLogin.getMongoDatabase().getCollection("users");

    //************************************************** GETTER METHODS **********************************************//

    /**
     * Using the MongoDB driver, retrieve all the users profile info from the database
     *
     */
    public static void getAllInfoFromCurrentUser(){
        Bson filter = eq("owner_id", usersIO.getOwnerID());

        usersCollection.find(filter).first().subscribe(new Subscriber<Document>() {

            private Document userDocument;

            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);
            }

            @Override
            public void onNext(Document document) {
                userDocument = document;
            }

            @Override
            public void onError(Throwable t) {
                // Handle errors
                t.printStackTrace();
                onComplete();
            }

            @Override
            public void onComplete() {
                try {
                    Platform.runLater(
                            () -> {
                                // Modify UI accordingly on the profile view
                                loginController.dashboard.profileView.displayNameTextField.setText((String) userDocument.get("displayName"));
                                loginController.dashboard.profileView.firstNameTextField.setText((String) userDocument.get("firstName"));
                                loginController.dashboard.profileView.lastNameTextField.setText((String) userDocument.get("lastName"));
                                loginController.dashboard.profileView.emailAddressTextField.setText((String) userDocument.get("emailAddress"));
                                loginController.dashboard.profileView.bioTextArea.setText((String) userDocument.get("profileBio"));
                                loginController.dashboard.profileView.profilePictureImage.setFill(new ImagePattern(new Image(new ByteArrayInputStream(((Binary) userDocument.get("profilePicture")).getData()))));
                                loginController.dashboard.profileView.loadingAnchorPane.setVisible(false);
                                loginController.dashboard.profileView.profileContentAnchorPane.setVisible(true);
                            }
                    );
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

    }

    /**
     * Using the MongoDB driver, retrieve the owner_id of the current logged in user
     *
     * @return a string containing the value of the current user's 'owner_id'
     */
    public static String getOwnerID() {
        Bson filter = eq("emailAddress", loginController.activeLogin.getEmailAddress());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);
        String ownerID = "";

        try {
            ownerID = (String) findSubscriber.await().getReceived().get(0).get("owner_id");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return ownerID;
    }

    /**
     * Retrieve the emailAddress of the current logged in user
     *
     * @return a string containing the value of the current user's 'emailAddress'
     */
    public static String getEmailAddress() {
        return loginController.activeLogin.getEmailAddress();
    }

    /**
     * Using the MongoDB driver, retrieve the displayName of the current logged in user
     *
     * @return a string containing the value of the current user's 'displayName'
     */
    public static String getDisplayName() {
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);
        String displayName = "";

        try {
            displayName = (String) findSubscriber.await().getReceived().get(0).get("displayName");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return displayName;
    }

    /**
     * Using the MongoDB driver, retrieve the firstName of the current logged in user
     *
     * @return a string containing the value of the current user's 'firstName'
     */
    public static String getFirstName() {
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);
        String firstName = "";

        try {
            firstName = (String) findSubscriber.await().getReceived().get(0).get("firstName");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return firstName;
    }

    /**
     * Using the MongoDB driver, retrieve the lastName of the current logged in user
     *
     * @return a string containing the value of the current user's 'lastName'
     */
    public static String getLastName() {
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);
        String lastName = "";

        try {
            lastName = (String) findSubscriber.await().getReceived().get(0).get("lastName");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return lastName;
    }

    /**
     * Using the MongoDB driver, retrieve the bio of the current logged in user
     *
     * @return a string containing the value of the current user's 'profileBio' statement
     */
    public static String getProfileBio() {
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);
        String profileBio = "";

        try {
            profileBio = (String) findSubscriber.await().getReceived().get(0).get("profileBio");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return profileBio;
    }

    /**
     * Using the MongoDB driver, retrieve the profile picture of the currently logged in user
     *
     * @return a byte[] containing the contents of the current user's picture file
     */
    public static byte[] getProfilePicture() {
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);
        Binary profilePicture;
        byte[] pictureData = null;

        try {
            profilePicture = findSubscriber.await().getReceived().get(0).get("profilePicture", Binary.class);
            pictureData = profilePicture.getData();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        return pictureData;
    }

    //************************************************** SETTER METHODS **********************************************//

    /**
     * Using the MongoDB driver, sets the emailAddress of the currently logged in user
     *
     * @param email a String containing the new email address to use for the current user
     */
    public static void setEmailAddress(String email){
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                Bson updatedValue = new Document("emailAddress", email);
                Bson updatedOperation = new Document("$set", updatedValue);
                usersCollection.updateOne(found, updatedOperation);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }

        // Need to confirm the new email address
    }

    /**
     * Using the MongoDB driver, sets the displayName of the currently logged in user
     *
     * @param displayName a String containing the new display name to use for the current user
     */
    public static void setDisplayName(String displayName){
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                Bson updatedValue = new Document("displayName", displayName);
                Bson updatedOperation = new Document("$set", updatedValue);
                usersCollection.updateOne(found, updatedOperation);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Using the MongoDB driver, sets the firstName of the currently logged in user
     *
     * @param firstName a String containing the new first name to use for the current user
     */
    public static void setFirstName(String firstName){
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                Bson updatedValue = new Document("firstName", firstName);
                Bson updatedOperation = new Document("$set", updatedValue);
                usersCollection.updateOne(found, updatedOperation);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Using the MongoDB driver, sets the lastName of the currently logged in user
     *
     * @param lastName a String containing the new last name to use for the current user
     */
    public static void setLastName(String lastName){
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                Bson updatedValue = new Document("lastName", lastName);
                Bson updatedOperation = new Document("$set", updatedValue);
                usersCollection.updateOne(found, updatedOperation);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Using the MongoDB driver, sets the profileBio of the currently logged in user
     *
     * @param bioStatement a String containing the new bio statement to use for the current user
     */
    public static void setProfileBio(String bioStatement){
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                Bson updatedValue = new Document("profileBio", bioStatement);
                Bson updatedOperation = new Document("$set", updatedValue);
                usersCollection.updateOne(found, updatedOperation);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Using the MongoDB driver, sets the profilePicture of the currently logged in user
     *
     * @param profilePic a byte[] containing the new file contents of the picture to use for the current user
     */
    public static void setProfilePicture(byte[] profilePic) {
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                Bson updatedValue = new Document("profilePicture", new BsonBinary(profilePic));
                Bson updatedOperation = new Document("$set", updatedValue);
                usersCollection.updateOne(found, updatedOperation);
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    //*************************************************** UTILITY METHODS ********************************************//

    /**
     * Using the MongoDB driver, sets the initial custom_data object of the currently logged in user
     *
     * @param displayName a String containing the display name to use for the current user
     * @param firstName   a String containing the first name to use for the current user
     * @param lastName    a String containing the last name to use for the current user
     */
    public static void setInitialCustomData(String displayName, String firstName, String lastName) {
        Bson filter = eq("owner_id", usersIO.getOwnerID());
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        usersCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                Bson updatedValue = new Document("displayName", displayName).append("firstName", firstName).append("lastName", lastName);
                Bson updatedOperation = new Document("$set", updatedValue);
                subscribers.OperationSubscriber<UpdateResult> updateSubscriber = new subscribers.OperationSubscriber<>();
                usersCollection.updateOne(found, updatedOperation).subscribe(updateSubscriber);
                updateSubscriber.await();
            }

        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
