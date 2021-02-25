package com.modelbox.databaseIO;

import com.modelbox.controllers.loginController;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import com.modelbox.auth.logIn;
import org.bson.conversions.Bson;

import static com.mongodb.client.model.Filters.eq;

public class usersIO {

    /**
     * Using the MongoDB driver, retrieve the owner_id of the current logged in user
     *
     * @return       a string containing the value of the current user's 'owner_id'
     */
    public static String getOwnerID() {
        MongoCollection<Document> collection = logIn.database.getCollection("users");
        Bson filter = eq("contact.email", loginController.activeLogin.getEmailAddress());
        System.out.println((String) (collection.find(filter).first()).get("owner_id"));
        return (String) (collection.find(filter).first()).get("owner_id");
    }
}
