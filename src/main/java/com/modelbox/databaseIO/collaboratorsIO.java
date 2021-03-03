package com.modelbox.databaseIO;

import com.modelbox.auth.logIn;
import com.modelbox.controllers.loginController;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class collaboratorsIO {
    public static MongoCollection<Document> collabsCollection = loginController.activeLogin.getMongoDatabase().getCollection("collaborators");

}
