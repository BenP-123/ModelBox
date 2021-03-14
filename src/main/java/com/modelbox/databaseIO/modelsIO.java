package com.modelbox.databaseIO;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import com.modelbox.controllers.loginController;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import javafx.application.Platform;
import org.bson.BsonBinary;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class modelsIO {

    public static MongoCollection<Document> modelsCollection = loginController.activeLogin.getMongoDatabase().getCollection("models");

    //************************************************** GETTER METHODS **********************************************//

    /**
     * Using the MongoDB driver, retrieve all the users model documents from the database and store them in a local list
     *
     */
    public static void getAllModelsFromCurrentUser(){
        Bson filter = eq("owner_id", usersIO.getOwnerID());

        modelsCollection.find(filter).first().subscribe(new Subscriber<Document>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);

                // Clear the list with the old models stored
                loginController.dashboard.dbModelsList.clear();
            }

            @Override
            public void onNext(Document document) {
                loginController.dashboard.dbModelsList.add(document);
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
                                // Clear all the UI cards from the myModelsFlowPane on the myModelsView
                                loginController.dashboard.myModelsView.myModelsFlowPane.getChildren().clear();

                                // Modify UI accordingly on the my model view
                                if (loginController.dashboard.dbModelsList.isEmpty()) {
                                    loginController.dashboard.myModelsView.loadingAnchorPane.setVisible(false);
                                    loginController.dashboard.myModelsView.myModelsScrollPane.setVisible(false);
                                    loginController.dashboard.myModelsView.noModelsBtn.setVisible(true);
                                } else {
                                    loginController.dashboard.myModelsView.myModelsFlowPane.getChildren().clear();

                                    for (Document model : loginController.dashboard.dbModelsList) {
                                        loginController.dashboard.myModelsView.addMyModelsPreviewCard(model);
                                    }

                                    loginController.dashboard.myModelsView.noModelsBtn.setVisible(false);
                                    loginController.dashboard.myModelsView.loadingAnchorPane.setVisible(false);
                                    loginController.dashboard.myModelsView.myModelsScrollPane.setVisible(true);
                                }
                            }
                    );
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

    }

    /**
     * Gets the name of a model from a Document
     *
     * @param model a Document containing all the information for a 3D model
     * @return      a String containing the name of the model stored by the user
     */
    public static String getModelName(Document model){
        return (String) model.get("modelName");
    }

    /**
     * Gets the file contents of a model from a Document
     *
     * @param model a Document containing all the information for a 3D model
     * @return      a byte[] containing the file contents of the model stored by the user
     */
    public static byte[] getModelFile(Document model){
        return ((Binary) model.get("modelFile")).getData();
    }

    //*************************************************** UTILITY METHODS ********************************************//

    /**
     * Using the MongoDB driver, add a new model document that contains a 3D model in binary data
     *
     * @return 0 on success, -1 on error
     */
    public static int createNewModel(File model){
        try {
            Document modelDocument = new Document("owner_id", usersIO.getOwnerID());
            modelDocument.append("modelName", model.getName());
            byte[] data = Files.readAllBytes(model.toPath());
            modelDocument.append("modelFile", new BsonBinary(data));
            subscribers.OperationSubscriber<InsertOneResult> insertSubscriber = new subscribers.OperationSubscriber<>();
            modelsCollection.insertOne(modelDocument).subscribe(insertSubscriber);
            insertSubscriber.await();
            return 0;
        } catch (Throwable throwable) {
            // Handle errors
            throwable.printStackTrace();
            return -1;
        }
    }

    /**
     * Using the MongoDB driver, retrieve the 3D model with the specified name and save the file locally to the path specified
     *
     * @param modelName a String containing the name of the 3D model
     * @param savePath  a Path containing the location that the model will be saved to
     */
    public static void saveModelFile(String modelName, Path savePath) {
        Bson filter = and(eq("modelName", modelName), eq("owner_id", usersIO.getOwnerID()));
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        modelsCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Binary modelData = findSubscriber.await().getReceived().get(0).get("modelFile", org.bson.types.Binary.class);
            Files.write(savePath, modelData.getData());
        } catch (Throwable throwable) {
            // Handle errors
            throwable.printStackTrace();
        }
    }

    /**
     * Using the MongoDB driver, delete the 3D model with the specified name and all its properties from the user's account
     *
     * @param modelName a String containing the name of the 3D model
     */
    public static void deleteModelDocument(String modelName) {
        Bson filter = and(eq("modelName", modelName), eq("owner_id", usersIO.getOwnerID()));
        subscribers.OperationSubscriber<DeleteResult> deleteSubscriber = new subscribers.OperationSubscriber<>();
        modelsCollection.deleteOne(filter).subscribe(deleteSubscriber);

        try {
            deleteSubscriber.await();
        } catch (Throwable throwable) {
            // Handle errors
            throwable.printStackTrace();
        }
    }
}
