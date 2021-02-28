package com.modelbox.databaseIO;

import com.modelbox.controllers.loginController;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.modelbox.auth.logIn;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

public class usersIO {

    public static MongoCollection<Document> usersCollection = logIn.database.getCollection("users");

    /**
     * Using the MongoDB driver, retrieve the owner_id of the current logged in user
     *
     * @return       a string containing the value of the current user's 'owner_id'
     */
    public static String getOwnerID() {
        Bson filter = eq("emailAddress", loginController.activeLogin.getEmailAddress());
        return (String) (usersCollection.find(filter).first()).get("owner_id");
    }

    public static String getEmailAddress() {
        Bson filter = eq("emailAddress", loginController.activeLogin.getEmailAddress());
        return (String) (usersCollection.find(filter).first()).get("emailAddress");
    }

    /**
     * Using the MongoDB driver, retrieve the displayName of the current logged in user
     *
     * @return       a string containing the value of the current user's 'displayName'
     */
    public static String getDisplayName() {
        Bson filter = eq("emailAddress", loginController.activeLogin.getEmailAddress());
        return (String) (usersCollection.find(filter).first()).get("displayName");
    }

    /**
     * Using the MongoDB driver, retrieve the firstName of the current logged in user
     *
     * @return       a string containing the value of the current user's 'firstName'
     */
    public static String getFirstName() {
        Bson filter = eq("emailAddress", loginController.activeLogin.getEmailAddress());
        return (String) (usersCollection.find(filter).first()).get("firstName");
    }

    /**
     * Using the MongoDB driver, retrieve the lastName of the current logged in user
     *
     * @return       a string containing the value of the current user's 'lastName'
     */
    public static String getLastName() {
        Bson filter = eq("emailAddress", loginController.activeLogin.getEmailAddress());
        return (String) (usersCollection.find(filter).first()).get("lastName");
    }

    /**
     * Using the MongoDB driver, retrieve the bio of the current logged in user
     *
     * @return       a string containing the value of the current user's 'profileBio' statement
     */
    public static String getProfileBio() {
        Bson filter = eq("emailAddress", loginController.activeLogin.getEmailAddress());
        return (String) (usersCollection.find(filter).first()).get("profileBio");
    }

    /**
     * Using the MongoDB driver, retrieve the profile picture of the current logged in user
     *
     * @return       should be a File containing the current user's profile picture
     */
    public static String getProfilePicture() {
        // Need to implement

        return "FIXME";
    }

    public static void setDisplayName(String displayNameField){
        Document found = usersCollection.find(new Document("displayName", getDisplayName())).first();

        if(found != null){
            Bson updatedValue = new Document("displayName", displayNameField);
            Bson updatedOperation = new Document("$set", updatedValue);
            usersCollection.updateOne(found, updatedOperation);

            System.out.println("User's display name updated");
        }
    }

    public static void setFirstName(String firstNameField){
        Document found = usersCollection.find(new Document("firstName", getFirstName())).first();

        if(found != null){
            Bson updatedValue = new Document("firstName", firstNameField);
            Bson updatedOperation = new Document("$set", updatedValue);
            usersCollection.updateOne(found, updatedOperation);

            System.out.println("User's first name updated");
        }
    }

    public static void setLastName(String lastNameField){
        Document found = usersCollection.find(new Document("lastName", getLastName())).first();

        if(found != null){
            Bson updatedValue = new Document("lastName", lastNameField);
            Bson updatedOperation = new Document("$set", updatedValue);
            usersCollection.updateOne(found, updatedOperation);

            System.out.println("User's last name updated");
        }
    }

    public static void setEmailAddress(String emailField){
        Document found = usersCollection.find(new Document("emailAddress", getEmailAddress())).first();

        if(found != null){
            Bson updatedValue = new Document("emailAddress", emailField);
            Bson updatedOperation = new Document("$set", updatedValue);
            usersCollection.updateOne(found, updatedOperation);

            System.out.println("User's email address updated");
        }
    }

    public static void setProfileBio(String bioField){
        Document found = usersCollection.find(new Document("profileBio", getProfileBio())).first();

        if(found != null){
            Bson updatedValue = new Document("profileBio", bioField);
            Bson updatedOperation = new Document("$set", updatedValue);
            usersCollection.updateOne(found, updatedOperation);

            System.out.println("User's bio updated");
        }
    }

    public static void setProfilePicture(String profilePic) {
        // Need to implement
    }
}
