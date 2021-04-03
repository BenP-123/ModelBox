package com.modelbox.controllers.myModels;

import com.modelbox.app;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
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
    @FXML public VBox collaboratorPermissionsList;


    /**
     * Closes and removes the share pop-up from view
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void closeShareBtnClicked(Event event) {
        AnchorPane currentSharePanel = (AnchorPane) ((Button) event.getSource()).getParent().getParent();
        app.myModelsView.myModelsAnchorPane.getChildren().remove(currentSharePanel);
    }

    @FXML
    private void addCollaboratorBtnClicked(Event event) {
        AnchorPane currentSharePanel = (AnchorPane) ((Button) event.getSource()).getParent().getParent();

        // Share the model with another user in the database
        BsonDocument shareModelConfiguration = new BsonDocument()
                .append("modelId", new BsonObjectId(new ObjectId(currentSharePanel.getId())))
                .append("recipientEmail", new BsonString(collaboratorEmailTextField.getText()));
        String functionCall = String.format("ModelBox.Models.shareCurrentUserModel('%s');", shareModelConfiguration.toJson());
        app.mongoApp.eval(functionCall);
    }

    @FXML
    public void leaveCollaborationBtnClicked(ActionEvent actionEvent) {
        //Place holder for leaving a collaboration
        System.out.println("Placeholder function...");
    }
}
