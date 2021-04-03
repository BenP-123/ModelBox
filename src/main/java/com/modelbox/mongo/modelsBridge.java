package com.modelbox.mongo;

import com.modelbox.app;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.text.Text;
import org.bson.BsonArray;
import org.bson.BsonDocument;
import org.bson.BsonValue;
import org.bson.conversions.Bson;

public class modelsBridge {

    private String shareModelStatus;

    public String getShareModelStatus() {
        return shareModelStatus;
    }

    public void setShareModelStatus(String status) {
        shareModelStatus = status;
    }

    public void handleGetCurrentUserModels(String currentUserModels) {
        try {
            app.dashboard.dbModelsList = BsonArray.parse(currentUserModels);

            // Clear all the UI cards from the myModelsFlowPane on the myModelsView
            app.myModelsView.myModelsFlowPane.getChildren().clear();

            if (app.dashboard.dbModelsList.isEmpty()) {
                app.myModelsView.loadingAnchorPane.setVisible(false);
                app.myModelsView.myModelsScrollPane.setVisible(false);
                app.myModelsView.noModelsBtn.setVisible(true);
            } else {
                for (BsonValue model : app.dashboard.dbModelsList) {
                    app.myModelsView.addMyModelsPreviewCard(model.asDocument());
                }
                app.myModelsView.noModelsBtn.setVisible(false);
                app.myModelsView.loadingAnchorPane.setVisible(false);

                app.myModelsView.myModelsFlowPane.minHeightProperty().bind(app.myModelsView.myModelsScrollPane.heightProperty());

                app.myModelsView.myModelsScrollPane.setVisible(true);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public void handleUploadCurrentUserModels() {
        // Asynchronously populate the my models view and show appropriate nodes when ready
        String functionCall = String.format("ModelBox.Models.getCurrentUserModels();");
        app.mongoApp.eval(functionCall);
    }

    public void handleDeleteCurrentUserModel() {
        // Asynchronously populate the my models view and show appropriate nodes when ready
        String functionCall = String.format("ModelBox.Models.getCurrentUserModels();");
        app.mongoApp.eval(functionCall);
    }

    public void handleShareCurrentUserModel(String sharedUser) {
        if (shareModelStatus.equals("success")) {
            BsonDocument sharedUserDocument = BsonDocument.parse(sharedUser);

            Text newAddition = new Text(sharedUserDocument.get("displayName").asString().getValue() + " is now a collaborator.");
            app.sharePopUpView.collaboratorPermissionsList.getChildren().add(newAddition);
        } else {
            app.sharePopUpView.collaboratorErrorTextField.setText(shareModelStatus);
            app.sharePopUpView.collaboratorErrorTextField.setVisible(true);
        }
    }

    public void handleGetCurrentModelCollaborators(String modelId, String currentModelCollaborators) {
        BsonArray modelCollaborators = BsonArray.parse(currentModelCollaborators);
        Parent shareRoot = null;

        // Load a share pop-up window
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/sharePopUp.fxml"));
            shareRoot = app.viewLoader.load();
            app.sharePopUpView = app.viewLoader.getController();
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }

        // Set the id of the shareRootAnchorPane to be equal to the model id
        app.sharePopUpView.shareRootAnchorPane.setId(modelId);

        // Display collaborators
        for (BsonValue collaborator : modelCollaborators) {
            System.out.println(collaborator.asDocument().toJson());
        }

        // Actually launch the share pop-up
        app.myModelsView.myModelsAnchorPane.getChildren().add(shareRoot);
    }
}
