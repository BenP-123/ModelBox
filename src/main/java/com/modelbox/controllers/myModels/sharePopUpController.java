package com.modelbox.controllers.myModels;

import com.modelbox.app;
import com.modelbox.mongo.modelsBridge;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonString;
import org.bson.types.ObjectId;

public class sharePopUpController {

    @FXML public AnchorPane shareRootAnchorPane;
    @FXML public AnchorPane shareInfoAnchorPane;
    @FXML public AnchorPane sharePermissionsAnchorPane;
    @FXML public TextField collaboratorEmailTextField;
    @FXML public TextField collaboratorErrorTextField;
    @FXML public VBox collaboratorsVBox;

    /**
     * Closes and removes the share pop-up from view
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void closeShareBtnClicked(Event event) {
        try {
            AnchorPane currentSharePanel = (AnchorPane) ((Button) event.getSource()).getParent().getParent();
            app.myModelsView.myModelsAnchorPane.getChildren().remove(currentSharePanel);

            // Refresh the my models view
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/myModels.fxml"));
            Parent root = app.viewLoader.load();
            app.myModelsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);

            // Asynchronously populate the my models view and show appropriate nodes when ready
            String functionCall = String.format("ModelBox.Models.getCurrentUserModels();");
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void addCollaboratorBtnClicked(Event event) {
        AnchorPane currentSharePanel = (AnchorPane) ((Button) event.getSource()).getParent().getParent().getParent();

        // Share the model with another user in the database

        modelsBridge.heading.setText("Congratulations!");
        modelsBridge.subHeading.setText("You've added your first collaborator for this model!");
        BsonDocument shareModelConfiguration = new BsonDocument()
                .append("modelId", new BsonObjectId(new ObjectId(currentSharePanel.getId())))
                .append("recipientEmail", new BsonString(collaboratorEmailTextField.getText()))
                .append("permissions", new BsonString("Viewer"));
        String functionCall = String.format("ModelBox.Models.shareCurrentUserModel('%s');", shareModelConfiguration.toJson());
        app.mongoApp.eval(functionCall);
    }
}
