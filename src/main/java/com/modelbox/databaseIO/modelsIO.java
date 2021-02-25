package com.modelbox.databaseIO;

import java.io.File;
import java.nio.file.Files;
import org.bson.BsonBinary;
import org.bson.Document;
import com.modelbox.auth.logIn;
import org.bson.types.ObjectId;

public class modelsIO {

    /**
     * Using the MongoDB driver, add a document that contains a 3D model in binary data
     *
     * @return       0 on success, -1 on error
     */
    public static int storeModel(File model){

        Document modelDoc = new Document("_id", new ObjectId());
        try {
            // Add 'owner_id' to the document to identify the model owner
            modelDoc.append("owner_id", usersIO.getOwnerID());

            // Add 3D model file to document
            byte[] data = Files.readAllBytes(model.toPath());
            modelDoc.append("modelFile", new BsonBinary(data));

            // Add document to 'models' collection
            logIn.database.getCollection("models").insertOne(modelDoc);
            return 0;
        } catch (Exception e) {
            // Handle error if file cannot be read to a byte array and stored
            return -1;
        }
    }
}
