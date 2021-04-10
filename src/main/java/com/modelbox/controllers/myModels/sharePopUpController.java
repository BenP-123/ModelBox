package com.modelbox.controllers.myModels;

import com.modelbox.app;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
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
    @FXML public ChoiceBox<String> addPermissionsChoiceBox;
    @FXML public VBox removeCollaboratorPopUp;
    @FXML public Button removeConfirmationBtn;
    @FXML public Button closeConfirmationBtn;
    public String currentCollaborator;

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
            String functionCall = "ModelBox.Models.getCurrentUserModels();";
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @FXML
    private void addCollaboratorBtnClicked(Event event) {
        AnchorPane currentSharePanel = (AnchorPane) ((Button) event.getSource()).getParent().getParent().getParent();

        // Share the model with another user in the database
        BsonDocument shareModelConfiguration = new BsonDocument()
                .append("modelId", new BsonObjectId(new ObjectId(currentSharePanel.getId())))
                .append("recipientEmail", new BsonString(collaboratorEmailTextField.getText()))
                .append("permissions", new BsonString(addPermissionsChoiceBox.getValue()));
        String functionCall = String.format("ModelBox.Models.shareCurrentUserModel('%s');", shareModelConfiguration.toJson());
        app.mongoApp.eval(functionCall);
    }

    @FXML
    @SuppressWarnings("unchecked")
    private void closeConfirmationBtnClicked(Event event) {
        VBox confirmationPopUp = (VBox) ((Button) event.getSource()).getParent().getParent().getParent();

        // Set the dropdown back to what it should be, which depends on the role of the collaborator
        if (app.dashboard.getCollaboratorRoleByModelID(app.dashboard.dbModelsList, confirmationPopUp.getId(), currentCollaborator).equals("Editor")) {
            ((ChoiceBox<String>) app.sharePopUpView.collaboratorsVBox.lookup("#" + currentCollaborator).lookup("#changePermissionsChoiceBox")).setValue("Editor");
        } else {
            ((ChoiceBox<String>) app.sharePopUpView.collaboratorsVBox.lookup("#" + currentCollaborator).lookup("#changePermissionsChoiceBox")).setValue("Viewer");
        }

        confirmationPopUp.setVisible(false);
    }

    @FXML
    private void removeConfirmationBtnClicked(Event event) {
        VBox confirmationPopUp = (VBox) ((Button) event.getSource()).getParent().getParent().getParent().getParent().getParent();

        // Remove the collaborator and divider line
        app.sharePopUpView.collaboratorsVBox.getChildren().remove(app.sharePopUpView.collaboratorsVBox.lookup("#" + currentCollaborator));
        app.sharePopUpView.collaboratorsVBox.getChildren().remove(app.sharePopUpView.collaboratorsVBox.lookup("#" + currentCollaborator));

        if (app.sharePopUpView.collaboratorsVBox.getChildren().isEmpty()) {
            // Display a message saying there are no collaborators
            app.models.displayNoCollaborators();
        }

        confirmationPopUp.setVisible(false);

        BsonDocument removeAccessConfiguration = new BsonDocument()
                .append("modelId", new BsonObjectId(new ObjectId(confirmationPopUp.getId())))
                .append("collaboratorId", new BsonString(currentCollaborator));

        String functionCall = String.format("ModelBox.Models.removeCurrentModelCollaborator('%s');", removeAccessConfiguration.toJson());
        app.mongoApp.eval(functionCall);
    }
}
