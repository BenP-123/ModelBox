package com.modelbox.databaseIO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import com.modelbox.controllers.loginController;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class modelsIO {

    public static MongoCollection<Document> modelsCollection = loginController.activeLogin.getMongoDatabase().getCollection("models");

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
            modelDoc.append("modelName", model.getName());
            byte[] data = Files.readAllBytes(model.toPath());
            modelDoc.append("modelFile", new BsonBinary(data));

            // Add document to 'models' collection
            modelsCollection.insertOne(modelDoc);
            return 0;
        } catch (Exception e) {
            // Handle error if file cannot be read to a byte array and stored
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Using the MongoDB driver, get the 3D model with the specified name and save the file locally to the path specified
     *
     * @return       no value returned
     */
    public static void downloadModelFile(String modelName, Path savePath) {

        // Query gets a model from only my stored models, given a specified modelName
        Bson filter = and(eq("modelName", modelName), eq("owner_id", usersIO.getOwnerID()));
        Binary modelData = (modelsCollection.find(filter).first()).get("modelFile", org.bson.types.Binary.class);

        try {
            Files.write(savePath, modelData.getData());
        } catch (Exception e) {
            // Handle error
        }

    }

    /**
     * Using the MongoDB driver, delete the 3D model with the specified name and all its properties from the database
     *
     * @return       no value returned
     */
    public static void deleteModelDocument(String modelName) {
        Bson filter = and(eq("modelName", modelName), eq("owner_id", usersIO.getOwnerID()));
        modelsCollection.deleteOne(filter);
    }

    public static void getMyModels(String ownerID){

        Bson filter = eq("owner_id", usersIO.getOwnerID());

        try (MongoCursor<Document> cur = modelsCollection.find(filter).iterator()) {
            while (cur.hasNext()) {
                Binary data = (Binary)cur.next().get("modelFile");

                loginController.dashboard.dbModelsList.add(data.getData());
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static String getModelName(byte[] modelData){
        Bson filter = and(eq("modelFile", new BsonBinary(modelData)), eq("owner_id", usersIO.getOwnerID()));
        String modelName = (String) (modelsCollection.find(filter).first()).get("modelName");

        return modelName;
    }
}
