package com.modelbox.mongo;

import com.modelbox.app;
import com.mongodb.BasicDBObject;
import com.mongodb.client.model.Aggregates;
import com.mongodb.client.model.Filters;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoCollection;
import javafx.application.Platform;
import org.bson.BsonArray;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.Binary;
import org.bson.types.ObjectId;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import static com.mongodb.client.model.Filters.and;
import static com.mongodb.client.model.Filters.eq;

public class modelsBridge {

    public MongoCollection<Document> modelsCollection;

    //************************************************** GETTER METHODS **********************************************//

    /**
     * Using the MongoDB driver, retrieve all the users model documents from the database and store them in a local list
     *
     */
    public void getAllModelsFromCurrentUser(){
        Bson filter = Filters.or(eq("owner_id", app.users.ownerId), eq("shared_id", app.users.ownerId));

        app.mongoDatabase.runCommand(new Document("find", "models").append("filter", filter).append("limit", 100)).subscribe(new Subscriber<Document>() {
            @Override
            public void onSubscribe(Subscription s) {
                s.request(Integer.MAX_VALUE);

                // Clear the list with the old models stored
                app.dashboard.dbModelsList.clear();
            }

            @Override
            public void onNext(Document document) {
                // Retrieve and parse the 'find' command result document
                try {
                    Document cursor = (Document) document.get("cursor");
                    ArrayList batch = (ArrayList) cursor.get("firstBatch");
                    for (Object model : batch) {
                        app.dashboard.dbModelsList.add((Document) model);
                    }
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
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
                                app.myModelsView.myModelsFlowPane.getChildren().clear();

                                // Modify UI accordingly on the my model view
                                if (app.dashboard.dbModelsList.isEmpty()) {
                                    app.myModelsView.loadingAnchorPane.setVisible(false);
                                    app.myModelsView.myModelsScrollPane.setVisible(false);
                                    app.myModelsView.noModelsBtn.setVisible(true);
                                } else {
                                    app.myModelsView.myModelsFlowPane.getChildren().clear();

                                    for (Document model : app.dashboard.dbModelsList) {
                                       app.myModelsView.addMyModelsPreviewCard(model);
                                    }

                                    app.myModelsView.noModelsBtn.setVisible(false);
                                    app.myModelsView.loadingAnchorPane.setVisible(false);

                                    app.myModelsView.myModelsFlowPane.minHeightProperty().bind(app.myModelsView.myModelsScrollPane.heightProperty());
                                    app.myModelsView.myModelsScrollPane.setVisible(true);
                                }
                            }
                    );
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

    }

    //*************************************************** UTILITY METHODS ********************************************//

    /**
     * Using the MongoDB driver, add a new model document that contains a 3D model in binary data
     *
     * @param  model a Document containing all the information for a 3D model
     * @return 0    on success, -1 on error
     */
    public int createNewModel(Document model){
        try {
            subscribers.OperationSubscriber<InsertOneResult> insertSubscriber = new subscribers.OperationSubscriber<>();
            modelsCollection.insertOne(model).subscribe(insertSubscriber);
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
     * @param modelID   a String containing the name of the 3D model
     * @param savePath  a Path containing the location that the model will be saved to
     */
    public void saveModelFile(String modelID, Path savePath) {
        Bson filter = and(eq("_id", new ObjectId(modelID)), eq("owner_id", app.users.ownerId));
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
     * @param modelID a String containing the name of the 3D model
     */
    public void deleteModelDocument(String modelID) {
        Bson filter = and(eq("_id", new ObjectId(modelID)), eq("owner_id", app.users.ownerId));
        subscribers.OperationSubscriber<DeleteResult> deleteSubscriber = new subscribers.OperationSubscriber<>();
        modelsCollection.deleteOne(filter).subscribe(deleteSubscriber);

        try {
            deleteSubscriber.await();
        } catch (Throwable throwable) {
            // Handle errors
            throwable.printStackTrace();
        }
    }

    /**
     * Using the MongoDB driver, adds another users "owner_id" to a "shared_id" of the model that the current user is
     * wishing to share.
     *
     * @param email a String containing the email address of the user that a logged in user is wishing to share a model with.
     * @param modelId  a String containing the id of the model that the user is sharing
     */
    public String shareModel(String email, String modelId){

        // Filter based on _id of model and the current logged in user's id.
        Bson filter = and(eq("_id", new ObjectId(modelId)), eq("owner_id", app.users.ownerId));
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        app.models.modelsCollection.find(filter).first().subscribe(findSubscriber);

        // Gets the owner_id of the person whose email the current user is wishing to share a model with.
        String sharedUserId = getSharedOwnerId(email);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                //Updates an array with $push, and a boolean with $set
                BasicDBObject update = new BasicDBObject().append("$push", new BasicDBObject().append("shared_id", sharedUserId))
                        .append("$set", new BasicDBObject().append("isShared", true ));;

                subscribers.OperationSubscriber<UpdateResult> updateSubscriber = new subscribers.OperationSubscriber<>();
                app.models.modelsCollection.updateOne(found, update).subscribe(updateSubscriber);
                updateSubscriber.await();
            }
            return "success";
        } catch (Throwable throwable) {
            // Don't print out the error for now
            return "Unable to share model with: " + email;
        }
    }

    /**
     * Using the MongoDB driver, gets the "owner_id" associated with a user name "emailAddress"
     *
     * @param email a String containing the email address of the user that a logged in user is wishing to share a model with.
     * @return a string that contains the owner_id associated with the userName.
     */
    private String getSharedOwnerId(String email){
        //Locate the ID of the user that matches the email
        Bson filter = eq("emailAddress", email);
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        app.users.usersCollection.find(filter).first().subscribe(findSubscriber);
        String sharedUserId = "";

        try {
            sharedUserId = (String) findSubscriber.await().getReceived().get(0).get("owner_id");
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return sharedUserId;
    }

    public ArrayList getModelCollaborators(String modelId) {
        // Filter based on _id of model and the current logged in user's id.
        Bson filter = and(eq("_id", new ObjectId(modelId)), eq("owner_id", app.users.ownerId));
        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        app.models.modelsCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                return (ArrayList) found.get("shared_id");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return new ArrayList();
    }

    public ArrayList getModelsSharedCollection(String modelId) {
        // Filter based on _id of model and the current logged in user's id.
        Bson filter = and(eq("_id", new ObjectId(modelId)), eq("shared_id", app.users.ownerId));

        subscribers.OperationSubscriber<Document> findSubscriber = new subscribers.OperationSubscriber<>();
        app.models.modelsCollection.find(filter).first().subscribe(findSubscriber);

        try {
            Document found = findSubscriber.await().getReceived().get(0);

            if(found != null){
                return (ArrayList) found.get("shared_id");
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return new ArrayList();
    }

    //************************************************ IN-PROGRESS METHODS *******************************************//
    /*
    public int modelUploadTracker;
    public int numModelsToUpload;

    public void handleAddToMyModels(String modelId, String modelName, String modelFile, Boolean isShared) {
        try {
            Document model = new Document("_id", modelId).append("modelName", modelName).append("modelFile", modelFile.getBytes("UTF-8")).append("isShared", isShared);
            app.dashboard.dbModelsList.add(model);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void handleGetAllModelsFromCurrentUser() {
        // Clear all the UI cards from the myModelsFlowPane on the myModelsView
        app.myModelsView.myModelsFlowPane.getChildren().clear();

        // Modify UI accordingly on the my model view
        if (app.dashboard.dbModelsList.isEmpty()) {
            app.myModelsView.loadingAnchorPane.setVisible(false);
            app.myModelsView.myModelsScrollPane.setVisible(false);
            app.myModelsView.noModelsBtn.setVisible(true);
        } else {
            for (Document model : app.dashboard.dbModelsList) {
                app.myModelsView.addMyModelsPreviewCard(model);
            }
            app.myModelsView.noModelsBtn.setVisible(false);
            app.myModelsView.loadingAnchorPane.setVisible(false);
            app.myModelsView.myModelsScrollPane.setVisible(true);
        }
    }

    public void handleUploadModelFromCurrentUser() {
        if (modelUploadTracker == numModelsToUpload) {
            // Asynchronously populate the my models view and show appropriate nodes when ready
            String functionCall = String.format("ModelBox.ModelsIO.getAllModelsFromCurrentUser();");
            app.mongoApp.eval(functionCall);
        }

        // Asynchronously populate the my models view and show appropriate nodes when ready
        String functionCall = String.format("ModelBox.ModelsIO.getAllModelsFromCurrentUser();");
        app.mongoApp.eval(functionCall);
    } */

}
