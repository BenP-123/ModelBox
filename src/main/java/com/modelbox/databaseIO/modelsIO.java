package com.modelbox.databaseIO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.modelbox.controllers.loginController;
import com.mongodb.client.MongoCollection;
import org.bson.BsonBinary;
import org.bson.Document;
import com.modelbox.auth.logIn;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class modelsIO {

    public static MongoCollection<Document> modelsCollection = logIn.database.getCollection("models");

    /**
     * Using the MongoDB driver, add a document that contains a 3D model in binary data
     *
     * @return       0 on success, -1 on error
     */
    public static int setModelFile(File model){
        Document modelDoc = new Document("_id", new ObjectId());
        try {
            // Add 'owner_id' to the document to identify the model owner
            modelDoc.append("owner_id", usersIO.getOwnerID());

            // Add 3D model file to document
            byte[] data = Files.readAllBytes(model.toPath());
            modelDoc.append("modelFile", new BsonBinary(data));

            // Add document to 'models' collection
            modelsCollection.insertOne(modelDoc);
            return 0;
        } catch (Exception e) {
            // Handle error if file cannot be read to a byte array and stored
            return -1;
        }
    }

    /**
     * Using the MongoDB driver, get the 3D model with the specified name
     *
     * @return       returns the 3D model stored in the database as a string for future import
     */
    public static void downloadModelFile(String modelName) {

        // Query gets a model from only my stored models, given a specified modelName
        Bson filter = and(eq("modelName", modelName), eq("owner_id", usersIO.getOwnerID()));
        BsonBinary modelData = (BsonBinary) (modelsCollection.find(filter).first()).get("modelFile");

        Path path = Paths.get("/path/file");
        try {
            Files.write(path, modelData.getData());
        } catch (Exception e) {
            // Handle error
        }

    }

}
