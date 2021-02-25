package com.modelbox.databaseIO;

import com.modelbox.auth.logIn;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

public class collaboratorsIO {
    public static MongoCollection<Document> collabsCollection = logIn.database.getCollection("collaborators");

}
