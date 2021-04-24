package com.modelbox.controllers.uploadModels;

import com.modelbox.app;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import org.bson.BsonDocument;
import org.bson.BsonString;

/**
 * Provides a JavaFX controller implementation for the editPopUp.fxml view
 */
public class editPopUpController {
    @FXML public AnchorPane editModelAnchorPane;
    @FXML public SubScene editModelSubScene;
    @FXML public TextField modelNameTextField;
    @FXML public Text modelTypeText;
    @FXML public AnchorPane editInfoAnchorPane;
    @FXML public Button saveAttributesBtn;

    /**
     * Closes and removes the edit pop-up from the 'Verify Models' view
     * @param event a JavaFX Event
     */
    @FXML
    private void closeEditPaneBtnClicked(Event event) {
        AnchorPane currentEditPopUp = (AnchorPane) ((Button) event.getSource()).getParent().getParent();
        app.verifyModelsView.verifyModelsAnchorPane.getChildren().remove(currentEditPopUp);
    }

    /**
     * Saves any modified attributes to the selected document prior to being uploaded to the database
     * @param event a JavaFX Event
     */
    @FXML
    private void saveAttributesBtnClicked(Event event) {
        AnchorPane currentEditInfoPane = (AnchorPane) ((Button) event.getSource()).getParent();
        editInfoAnchorPane.setVisible(false);

        int currentModelIndex = app.dashboard.getDocumentIndexByModelID(app.dashboard.verifyModelsList, currentEditInfoPane.getId());
        BsonDocument currentModelDocument = app.dashboard.verifyModelsList.get(currentModelIndex).asDocument();

        currentModelDocument.remove("modelName");
        currentModelDocument.append("modelName", new BsonString(modelNameTextField.getText() + "." + modelTypeText.getText().toLowerCase()));

        app.dashboard.verifyModelsList.remove(currentModelIndex);
        app.dashboard.verifyModelsList.add(currentModelDocument);

        editInfoAnchorPane.setVisible(true);

        app.verifyModelsView.verifyModelsAnchorPane.getChildren().remove(currentEditInfoPane.getParent());
    }
}
