package com.modelbox.mongo;

import com.github.robtimus.net.protocol.data.DataURLs;
import com.modelbox.app;
import javafx.fxml.FXMLLoader;
import javafx.scene.Camera;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.PerspectiveCamera;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.apache.commons.io.FilenameUtils;
import org.bson.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

public class modelsBridge {

    private String shareModelStatus;
    public static Text heading = new Text();
    public static Text subHeading = new Text();

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
                app.myModelsView.myModelsToolBar.setVisible(false);
                app.myModelsView.noModelSearchResultsBtn.setVisible(false);
                app.myModelsView.noModelsBtn.setVisible(true);
            } else {
                for (BsonValue model : app.dashboard.dbModelsList) {
                    app.myModelsView.addMyModelsPreviewCard(model.asDocument());
                }
                app.myModelsView.noModelSearchResultsBtn.setVisible(false);
                app.myModelsView.noModelsBtn.setVisible(false);
                app.myModelsView.loadingAnchorPane.setVisible(false);
                app.myModelsView.myModelsFlowPane.minHeightProperty().bind(app.myModelsView.myModelsScrollPane.heightProperty());
                app.myModelsView.myModelsToolBar.setVisible(true);
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

    public void handleTerminateModelCollaboration() {
        // Asynchronously populate the my models view and show appropriate nodes when ready
        String functionCall = String.format("ModelBox.Models.getCurrentUserModels();");
        app.mongoApp.eval(functionCall);
    }

    public void handleShareCurrentUserModel(String sharedUser) {
        if (shareModelStatus.equals("success")) {
            BsonDocument collaborator = BsonDocument.parse(sharedUser);

            // Display Viewer
            VBox collaboratorInfo = new VBox();
            collaboratorInfo.setPrefWidth(315);

            Text displayName = new Text(collaborator.asDocument().get("displayName").asString().getValue());
            displayName.setWrappingWidth(315);
            displayName.setStyle("-fx-fill: #007be8; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

            Text emailAddress = new Text(collaborator.asDocument().get("emailAddress").asString().getValue());
            emailAddress.setWrappingWidth(315);
            emailAddress.setStyle("-fx-fill: #181a1d; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

            Text permissions = new Text("(Viewer)");
            permissions.setWrappingWidth(315);
            permissions.setStyle("-fx-fill: #181a1d; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

            collaboratorInfo.getChildren().addAll(displayName, emailAddress, permissions);

            Line separator = new Line();
            separator.setStartX(0);
            separator.setEndX(275);
            separator.setStroke(Color.color(0.0941, 0.1019, 0.1137));
            separator.setStrokeWidth(1.25);
            app.sharePopUpView.collaboratorsVBox.getChildren().add(collaboratorInfo);
            app.sharePopUpView.collaboratorsVBox.getChildren().add(separator);
        } else {
            app.sharePopUpView.collaboratorErrorTextField.setText(shareModelStatus);
            app.sharePopUpView.collaboratorErrorTextField.setVisible(true);
        }
    }

    public void handleGetCurrentModelPreview(String modelId, String currentModelCollaborators) {
        // Load a preview pop-up window
        try {
            BsonArray modelCollaborators = BsonArray.parse(currentModelCollaborators);
            Parent previewRoot = null;

            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/previewPopUp.fxml"));
            previewRoot = app.viewLoader.load();
            app.previewPopUpView = app.viewLoader.getController();

            // Set the id of the previewModelAnchorPane to be equal to the model id
            app.previewPopUpView.previewInfoAnchorPane.setId(modelId);
            app.previewPopUpView.previewModelAnchorPane.setId(modelId);

            // Load the model document and add the model file to the interactive preview panel
            int modelIndex = app.dashboard.getDocumentIndexByModelID(app.dashboard.dbModelsList, modelId);
            BsonDocument model = app.dashboard.dbModelsList.get(modelIndex).asDocument();
            int numBytes = model.get("modelFile").asBinary().getData().length;
            app.dashboard.stlImporter.read(DataURLs.builder(model.get("modelFile").asBinary().getData()).withBase64Data(true).withMediaType("model/stl").build());
            TriangleMesh currentModelMesh = app.dashboard.stlImporter.getImport();
            MeshView currentModelMeshView = new MeshView(currentModelMesh);
            Group previewModelGroup = new Group(currentModelMeshView);
            app.previewPopUpView.previewModelSubScene.setRoot(previewModelGroup);
            Camera camera = new PerspectiveCamera();
            app.previewPopUpView.previewModelSubScene.setCamera(camera);
            app.previewPopUpView.previewModelSubScene.widthProperty().bind(app.previewPopUpView.previewModelAnchorPane.widthProperty());
            app.previewPopUpView.previewModelSubScene.heightProperty().bind(app.previewPopUpView.previewModelAnchorPane.heightProperty());
            app.previewPopUpView.initMouseControl(previewModelGroup, app.previewPopUpView.previewModelSubScene);

            // Set the static attributes
            app.previewPopUpView.modelTypeText.setText(FilenameUtils.getExtension(model.get("modelName").asString().getValue()).toUpperCase());
            app.previewPopUpView.modelSizeText.setText(app.models.getModelSize(numBytes));
            app.previewPopUpView.modelDateText.setText(app.models.getModelTimestamp(modelId));

            // Set the attributes and view components that may vary based on permissions
            if (model.get("owner_id").asString().getValue().equals(app.users.ownerId)) {
                // Current user is the owner and the model is either shared or not shared
                app.previewPopUpView.modelNameViewerText.setVisible(false);
                app.previewPopUpView.collaboratorsScrollPane.setVisible(false);
                app.previewPopUpView.modelNameEditorTextField.setVisible(true);
                app.previewPopUpView.modelNameEditorTextField.setText(FilenameUtils.removeExtension(model.get("modelName").asString().getValue()));
                app.previewPopUpView.saveAttributesBtn.setVisible(true);
            } else if (!model.get("owner_id").asString().getValue().equals(app.users.ownerId) && model.get("isShared").asBoolean().getValue() && isUserAnEditor(model.get("collaborators").asArray())) {
                // Current user is not the owner, the model is shared, the current user has editor permissions
                app.previewPopUpView.modelNameViewerText.setVisible(false);
                app.previewPopUpView.modelNameEditorTextField.setVisible(true);
                app.previewPopUpView.modelNameEditorTextField.setText(FilenameUtils.removeExtension(model.get("modelName").asString().getValue()));
                app.previewPopUpView.saveAttributesBtn.setVisible(true);
                app.previewPopUpView.collaboratorsScrollPane.setVisible(true);
                app.previewPopUpView.collaboratorsVBox.getChildren().clear();

                // Display collaborators
                for (BsonValue collaborator : modelCollaborators) {
                    if (collaborator.asDocument().get("owner_id").asString().getValue().equals(model.get("owner_id").asString().getValue())) {
                        // Display owner
                        VBox collaboratorInfo = new VBox();
                        collaboratorInfo.setPrefWidth(315);

                        Text displayName = new Text(collaborator.asDocument().get("displayName").asString().getValue());
                        displayName.setWrappingWidth(315);
                        displayName.setStyle("-fx-fill: #55b0ff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text emailAddress = new Text(collaborator.asDocument().get("emailAddress").asString().getValue());
                        emailAddress.setWrappingWidth(315);
                        emailAddress.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text permissions = new Text("(Owner)");
                        permissions.setWrappingWidth(315);
                        permissions.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        collaboratorInfo.getChildren().addAll(displayName, emailAddress, permissions);

                        Line separator = new Line();
                        separator.setStartX(0);
                        separator.setEndX(275);
                        separator.setStroke(Color.WHITE);
                        separator.setStrokeWidth(1.25);
                        app.previewPopUpView.collaboratorsVBox.getChildren().add(collaboratorInfo);
                        app.previewPopUpView.collaboratorsVBox.getChildren().add(separator);
                    } else {
                        // Display collaborator
                        VBox collaboratorInfo = new VBox();
                        collaboratorInfo.setPrefWidth(315);

                        Text displayName = new Text(collaborator.asDocument().get("displayName").asString().getValue());
                        displayName.setWrappingWidth(315);
                        displayName.setStyle("-fx-fill: #55b0ff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text emailAddress = new Text(collaborator.asDocument().get("emailAddress").asString().getValue());
                        emailAddress.setWrappingWidth(315);
                        emailAddress.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text permissions = new Text("(Editor)");
                        permissions.setWrappingWidth(315);
                        permissions.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        collaboratorInfo.getChildren().addAll(displayName, emailAddress, permissions);

                        Line separator = new Line();
                        separator.setStartX(0);
                        separator.setEndX(275);
                        separator.setStroke(Color.WHITE);
                        separator.setStrokeWidth(1.25);
                        app.previewPopUpView.collaboratorsVBox.getChildren().add(collaboratorInfo);
                        app.previewPopUpView.collaboratorsVBox.getChildren().add(separator);
                    }
                }

            } else if (!model.get("owner_id").asString().getValue().equals(app.users.ownerId) && model.get("isShared").asBoolean().getValue()) {
                // Current user is not the owner, the model is shared, the current user has viewer permissions
                app.previewPopUpView.modelNameEditorTextField.setVisible(false);
                app.previewPopUpView.saveAttributesBtn.setVisible(false);
                app.previewPopUpView.modelNameViewerText.setVisible(true);
                app.previewPopUpView.modelNameViewerText.setText(FilenameUtils.removeExtension(model.get("modelName").asString().getValue()));
                app.previewPopUpView.collaboratorsScrollPane.setVisible(true);
                app.previewPopUpView.collaboratorsVBox.getChildren().clear();

                // Display collaborators
                for (BsonValue collaborator : modelCollaborators) {
                    if (collaborator.asDocument().get("owner_id").asString().getValue().equals(model.get("owner_id").asString().getValue())) {
                        // Display owner
                        VBox collaboratorInfo = new VBox();
                        collaboratorInfo.setPrefWidth(315);

                        Text displayName = new Text(collaborator.asDocument().get("displayName").asString().getValue());
                        displayName.setWrappingWidth(315);
                        displayName.setStyle("-fx-fill: #55b0ff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text emailAddress = new Text(collaborator.asDocument().get("emailAddress").asString().getValue());
                        emailAddress.setWrappingWidth(315);
                        emailAddress.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text permissions = new Text("(Owner)");
                        permissions.setWrappingWidth(315);
                        permissions.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        collaboratorInfo.getChildren().addAll(displayName, emailAddress, permissions);

                        Line separator = new Line();
                        separator.setStartX(0);
                        separator.setEndX(275);
                        separator.setStroke(Color.WHITE);
                        separator.setStrokeWidth(1.25);
                        app.previewPopUpView.collaboratorsVBox.getChildren().add(collaboratorInfo);
                        app.previewPopUpView.collaboratorsVBox.getChildren().add(separator);
                    } else {
                        // Display collaborator
                        VBox collaboratorInfo = new VBox();
                        collaboratorInfo.setPrefWidth(315);

                        Text displayName = new Text(collaborator.asDocument().get("displayName").asString().getValue());
                        displayName.setWrappingWidth(315);
                        displayName.setStyle("-fx-fill: #55b0ff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text emailAddress = new Text(collaborator.asDocument().get("emailAddress").asString().getValue());
                        emailAddress.setWrappingWidth(315);
                        emailAddress.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text permissions = new Text("(Viewer)");
                        permissions.setWrappingWidth(315);
                        permissions.setStyle("-fx-fill: #ffffff; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        collaboratorInfo.getChildren().addAll(displayName, emailAddress, permissions);

                        Line separator = new Line();
                        separator.setStartX(0);
                        separator.setEndX(275);
                        separator.setStroke(Color.WHITE);
                        separator.setStrokeWidth(1.25);
                        app.previewPopUpView.collaboratorsVBox.getChildren().add(collaboratorInfo);
                        app.previewPopUpView.collaboratorsVBox.getChildren().add(separator);
                    }
                }
            }

            app.previewPopUpView.positionX.bind(app.previewPopUpView.previewModelAnchorPane.widthProperty().divide(2));
            app.previewPopUpView.positionY.bind(app.previewPopUpView.previewModelAnchorPane.heightProperty().divide(2));

            // Actually launch the preview pop-up
            app.myModelsView.myModelsAnchorPane.getChildren().add(previewRoot);
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }
    }

    private Boolean isUserAnEditor(BsonArray modelCollaborators) {
        Boolean isEditor = false;

        for (BsonValue collaborator : modelCollaborators) {
            if (collaborator.asDocument().get("id").asString().getValue().equals(app.users.ownerId) &&
                    collaborator.asDocument().get("permissions").asString().getValue().equals("Editor")) {
                isEditor = true;
            }
        }

        return isEditor;
    }

    public void handleGetCurrentModelCollaborators(String modelId, String currentModelCollaborators) {
        // Load a share pop-up window
        try {
            BsonArray modelCollaborators = BsonArray.parse(currentModelCollaborators);
            Parent shareRoot = null;

            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/sharePopUp.fxml"));
            shareRoot = app.viewLoader.load();
            app.sharePopUpView = app.viewLoader.getController();

            // Set the id of the shareRootAnchorPane to be equal to the model id
            app.sharePopUpView.shareRootAnchorPane.setId(modelId);

            heading.setWrappingWidth(315);
            heading.setStyle("-fx-fill: #007be8; -fx-font-size: 16px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");
            subHeading.setWrappingWidth(315);
            subHeading.setStyle("-fx-fill: #181a1d; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

            if (modelCollaborators.isEmpty()) {
                // Display a message saying there are no collaborators
                VBox noCollaboratorsMessage = new VBox();
                noCollaboratorsMessage.setSpacing(10);
                noCollaboratorsMessage.setPrefWidth(315);

                heading.setText("No collaborators yet!");
                subHeading.setText("To start sharing this model with others, add a collaborator.");

                noCollaboratorsMessage.getChildren().addAll(heading, subHeading);

                Line separator = new Line();
                separator.setStartX(0);
                separator.setEndX(275);
                separator.setStroke(Color.color(0.0941, 0.1019, 0.1137));
                separator.setStrokeWidth(1.25);
                app.sharePopUpView.collaboratorsVBox.getChildren().add(noCollaboratorsMessage);
                app.sharePopUpView.collaboratorsVBox.getChildren().add(separator);
            } else {
                // Display collaborators
                for (BsonValue collaborator : modelCollaborators) {
                    if (app.dashboard.getCollaboratorRoleByModelID(app.dashboard.dbModelsList, modelId, collaborator.asDocument().get("owner_id").asString().getValue()).equals("Editor")) {
                        // Display Editor
                        VBox collaboratorInfo = new VBox();
                        collaboratorInfo.setPrefWidth(315);

                        Text displayName = new Text(collaborator.asDocument().get("displayName").asString().getValue());
                        displayName.setWrappingWidth(315);
                        displayName.setStyle("-fx-fill: #007be8; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text emailAddress = new Text(collaborator.asDocument().get("emailAddress").asString().getValue());

                        emailAddress.setWrappingWidth(315);
                        emailAddress.setStyle("-fx-fill: #181a1d; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text permissions = new Text("(Editor)");
                        permissions.setWrappingWidth(315);
                        permissions.setStyle("-fx-fill: #181a1d; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        collaboratorInfo.getChildren().addAll(displayName, emailAddress, permissions);

                        Line separator = new Line();
                        separator.setStartX(0);
                        separator.setEndX(275);
                        separator.setStroke(Color.color(0.0941, 0.1019, 0.1137));
                        separator.setStrokeWidth(1.25);
                        app.sharePopUpView.collaboratorsVBox.getChildren().add(collaboratorInfo);
                        app.sharePopUpView.collaboratorsVBox.getChildren().add(separator);
                    } else {
                        // Display Viewer
                        VBox collaboratorInfo = new VBox();
                        collaboratorInfo.setPrefWidth(315);

                        Text displayName = new Text(collaborator.asDocument().get("displayName").asString().getValue());
                        displayName.setWrappingWidth(315);
                        displayName.setStyle("-fx-fill: #007be8; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text emailAddress = new Text(collaborator.asDocument().get("emailAddress").asString().getValue());
                        emailAddress.setWrappingWidth(315);
                        emailAddress.setStyle("-fx-fill: #181a1d; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        Text permissions = new Text("(Viewer)");
                        permissions.setWrappingWidth(315);
                        permissions.setStyle("-fx-fill: #181a1d; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                        collaboratorInfo.getChildren().addAll(displayName, emailAddress, permissions);

                        Line separator = new Line();
                        separator.setStartX(0);
                        separator.setEndX(275);
                        separator.setStroke(Color.color(0.0941, 0.1019, 0.1137));
                        separator.setStrokeWidth(1.25);
                        app.sharePopUpView.collaboratorsVBox.getChildren().add(collaboratorInfo);
                        app.sharePopUpView.collaboratorsVBox.getChildren().add(separator);
                    }
                }
            }

            // Actually launch the share pop-up
            app.myModelsView.myModelsAnchorPane.getChildren().add(shareRoot);
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }
    }

    public void handleGetCurrentUserModelSearch(String modelSearchResults) {
        try {
            app.dashboard.dbModelsList = BsonArray.parse(modelSearchResults);

            // Clear all the UI cards from the myModelsFlowPane on the myModelsView
            app.myModelsView.myModelsFlowPane.getChildren().clear();

            if (app.dashboard.dbModelsList.isEmpty()) {
                app.myModelsView.loadingAnchorPane.setVisible(false);
                app.myModelsView.myModelsScrollPane.setVisible(false);
                app.myModelsView.modelSearchField.setText("");
                app.myModelsView.myModelsToolBar.setVisible(false);
                app.myModelsView.noModelsBtn.setVisible(false);
                app.myModelsView.noModelSearchResultsBtn.setVisible(true);
            } else {
                for (BsonValue model : app.dashboard.dbModelsList) {
                    app.myModelsView.addMyModelsPreviewCard(model.asDocument());
                }
                app.myModelsView.noModelSearchResultsBtn.setVisible(false);
                app.myModelsView.noModelsBtn.setVisible(false);
                app.myModelsView.loadingAnchorPane.setVisible(false);
                app.myModelsView.myModelsFlowPane.minHeightProperty().bind(app.myModelsView.myModelsScrollPane.heightProperty());
                app.myModelsView.modelSearchField.setText("");
                app.myModelsView.myModelsToolBar.setVisible(true);
                app.myModelsView.myModelsScrollPane.setVisible(true);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public String getModelSize(int bytes){
        String modelSizeString;
        bytes /= 1024;

        if(bytes > 1000) { modelSizeString = Math.round(bytes / 1024) + " MB"; }

        else{ modelSizeString = bytes + " KB"; }

        return modelSizeString;
    }

    public String getModelTimestamp(String modelId){
        long millis = parseInt(modelId.substring(0,8), 16);
        millis *= 1000;
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        Date result = new Date(millis);
        return format.format(result);
    }
}
