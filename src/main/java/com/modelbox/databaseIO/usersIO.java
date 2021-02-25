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
        Bson filter = eq("contact.email", loginController.activeLogin.getEmailAddress());
        return (String) (usersCollection.find(filter).first()).get("owner_id");
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
}
