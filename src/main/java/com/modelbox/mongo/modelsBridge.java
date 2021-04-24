package com.modelbox.mongo;

import com.github.robtimus.net.protocol.data.DataURLs;
import com.modelbox.app;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.scene.text.Text;
import org.apache.commons.io.FilenameUtils;
import org.bson.*;
import org.bson.types.ObjectId;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * Provides a connection mechanism to handle model-related callbacks from Javascript calls made in a JavaFX WebEngine
 * @see com.modelbox.app#mongoApp
 */
public class modelsBridge {

    private String shareModelStatus;

    /**
     * Gets the status of sharing a model
     * @return the String containing the status of sharing a model
     */
    public String getShareModelStatus() {
        return shareModelStatus;
    }

    /**
     * Sets the status of sharing a model
     * @param status the String containing the current status of sharing a model
     */
    public void setShareModelStatus(String status) {
        shareModelStatus = status;
    }

    /**
     * Prepares and populates the appropriate fields on the 'My Models' view and shows the app's 'My Models' view
     * @param currentUserModels a serialized BSON array containing database information to be shown
     */
    public void handleGetCurrentUserModels(String currentUserModels) {
        try {
            app.dashboard.dbModelsList = BsonArray.parse(currentUserModels);
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

                app.myModelsView.filterModelsChoiceBox.setValue("Show all models");
                app.myModelsView.filterModelsChoiceBox.getItems().addAll("Show all models", "Owned by me", "Shared with me");

                app.myModelsView.filterModelsChoiceBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                    app.myModelsView.myModelsToolBar.setVisible(false);
                    app.myModelsView.noModelsBtn.setVisible(false);
                    app.myModelsView.myModelsScrollPane.setVisible(false);
                    app.myModelsView.loadingAnchorPane.setVisible(true);

                    if (newValue.equals("Show all models")) {
                        app.myModelsView.myModelsFlowPane.getChildren().clear();
                        for (BsonValue model : app.dashboard.dbModelsList) {
                            app.myModelsView.addMyModelsPreviewCard(model.asDocument());
                        }
                        app.myModelsView.modelSearchField.setText("");
                    } else if (newValue.equals("Owned by me") && app.myModelsView.modelSearchField.getText().equals("")) {
                        app.myModelsView.myModelsFlowPane.getChildren().clear();
                        for (BsonValue model : app.dashboard.dbModelsList) {
                            if (model.asDocument().get("owner_id").asString().getValue().equals(app.users.ownerId)) {
                                app.myModelsView.addMyModelsPreviewCard(model.asDocument());
                            }
                        }
                    } else if (newValue.equals("Shared with me") && app.myModelsView.modelSearchField.getText().equals("")) {
                        app.myModelsView.myModelsFlowPane.getChildren().clear();
                        for (BsonValue model : app.dashboard.dbModelsList) {
                            if (!model.asDocument().get("owner_id").asString().getValue().equals(app.users.ownerId)) {
                                app.myModelsView.addMyModelsPreviewCard(model.asDocument());
                            }
                        }
                    } else if (newValue.equals("Owned by me")) {
                        app.myModelsView.myModelsFlowPane.getChildren().clear();
                        for (BsonValue model : app.dashboard.dbModelsList) {
                            if (!model.asDocument().get("modelName").asString().getValue().matches("(?i).*" + app.myModelsView.modelSearchField.getText() + ".*")) {
                                app.myModelsView.myModelsFlowPane.getChildren().remove(app.myModelsView.myModelsFlowPane.lookup("#" + model.asDocument().get("_id").asObjectId().getValue().toHexString()));
                            } else {
                                app.myModelsView.addMyModelsPreviewCard(model.asDocument());
                            }
                        }
                        for (BsonValue model : app.dashboard.dbModelsList) {
                            if (!model.asDocument().get("owner_id").asString().getValue().equals(app.users.ownerId)) {
                                app.myModelsView.myModelsFlowPane.getChildren().remove(app.myModelsView.myModelsFlowPane.lookup("#" + model.asDocument().get("_id").asObjectId().getValue().toHexString()));
                            }
                        }
                    } else if (newValue.equals("Shared with me")) {
                        app.myModelsView.myModelsFlowPane.getChildren().clear();
                        for (BsonValue model : app.dashboard.dbModelsList) {
                            if (!model.asDocument().get("modelName").asString().getValue().matches("(?i).*" + app.myModelsView.modelSearchField.getText() + ".*")) {
                                app.myModelsView.myModelsFlowPane.getChildren().remove(app.myModelsView.myModelsFlowPane.lookup("#" + model.asDocument().get("_id").asObjectId().getValue().toHexString()));
                            } else {
                                app.myModelsView.addMyModelsPreviewCard(model.asDocument());
                            }
                        }
                        for (BsonValue model : app.dashboard.dbModelsList) {
                            if (model.asDocument().get("owner_id").asString().getValue().equals(app.users.ownerId)) {
                                app.myModelsView.myModelsFlowPane.getChildren().remove(app.myModelsView.myModelsFlowPane.lookup("#" + model.asDocument().get("_id").asObjectId().getValue().toHexString()));
                            }
                        }
                    }

                    if (app.myModelsView.myModelsFlowPane.getChildren().isEmpty()) {
                        app.myModelsView.loadingAnchorPane.setVisible(false);
                        app.myModelsView.myModelsScrollPane.setVisible(false);
                        app.myModelsView.modelSearchField.setText("");
                        app.myModelsView.myModelsToolBar.setVisible(false);
                        app.myModelsView.noModelsBtn.setVisible(false);
                        app.myModelsView.noModelSearchResultsBtn.setVisible(true);
                    } else {
                        app.myModelsView.noModelSearchResultsBtn.setVisible(false);
                        app.myModelsView.noModelsBtn.setVisible(false);
                        app.myModelsView.loadingAnchorPane.setVisible(false);
                        app.myModelsView.myModelsFlowPane.minHeightProperty().bind(app.myModelsView.myModelsScrollPane.heightProperty());
                        app.myModelsView.myModelsToolBar.setVisible(true);
                        app.myModelsView.myModelsScrollPane.setVisible(true);
                    }
                });
                app.myModelsView.myModelsScrollPane.setVisible(true);
            }

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Prepares the UI after a user has deleted a model from their account
     */
    public void handleDeleteCurrentUserModel() {
        refreshCurrentUserModels();
    }

    /**
     * Prepares the UI after a user has removed themselves from a shared model collaboration
     */
    public void handleTerminateModelCollaboration() {
        refreshCurrentUserModels();
    }

    /**
     * Loads and refreshes the appropriate fields on the 'My Models' view
     */
    private void refreshCurrentUserModels() {
        try {
            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/myModels.fxml"));
            Parent root = app.viewLoader.load();
            app.myModelsView = app.viewLoader.getController();
            app.dashboard.dashViewsAnchorPane.getChildren().setAll(root);

            String functionCall = "ModelBox.Models.getCurrentUserModels();";
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Prepares and populates the appropriate fields on the 'Share with others' view after a collaborator has been added
     * @param newModelCollaborator a serialized BSON document containing database information to be shown
     */
    public void handleShareCurrentUserModel(String newModelCollaborator) {
        if (shareModelStatus.equals("success")) {
            BsonDocument collaborator = BsonDocument.parse(newModelCollaborator);

            if (collaborator.asDocument().get("isFirstCollaborator").asBoolean().getValue()) {
                app.sharePopUpView.collaboratorsVBox.getChildren().clear();
            }

            // Display new collaborator
            GridPane collaboratorInfo = new GridPane();
            collaboratorInfo.setPrefWidth(315);

            Text displayName = new Text(collaborator.asDocument().get("displayName").asString().getValue());
            displayName.setWrappingWidth(190);
            displayName.setStyle("-fx-fill: #007be8; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

            Text emailAddress = new Text(collaborator.asDocument().get("emailAddress").asString().getValue());
            emailAddress.setWrappingWidth(190);
            emailAddress.setStyle("-fx-fill: #181a1d; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

            ChoiceBox<String> changePermissionsChoiceBox = new ChoiceBox<>();
            changePermissionsChoiceBox.setPrefWidth(115);
            changePermissionsChoiceBox.setPrefHeight(35);
            changePermissionsChoiceBox.setId("changePermissionsChoiceBox");
            if (collaborator.asDocument().get("permissions").asString().getValue().equals("Editor")) {
                changePermissionsChoiceBox.setValue("Editor");
            } else {
                changePermissionsChoiceBox.setValue("Viewer");
            }
            changePermissionsChoiceBox.getItems().addAll("Viewer", "Editor", "Remove");

            GridPane.setConstraints(displayName, 0, 0);
            GridPane.setConstraints(emailAddress, 0, 1);
            GridPane.setConstraints(changePermissionsChoiceBox, 1, 0);
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPrefWidth(190);
            GridPane.setHalignment(changePermissionsChoiceBox, HPos.LEFT);
            collaboratorInfo.getColumnConstraints().add(colConstraints);
            collaboratorInfo.getChildren().addAll(displayName, emailAddress, changePermissionsChoiceBox);

            Line separator = new Line();
            separator.setStartX(0);
            separator.setEndX(300);
            separator.setStroke(Color.color(0.0941, 0.1019, 0.1137));
            separator.setStrokeWidth(1.25);
            app.sharePopUpView.collaboratorsVBox.getChildren().add(collaboratorInfo);
            app.sharePopUpView.collaboratorsVBox.getChildren().add(separator);
        } else {
            app.sharePopUpView.collaboratorErrorTextField.setText(shareModelStatus);
            app.sharePopUpView.collaboratorErrorTextField.setVisible(true);
        }
    }

    /**
     * Prepares and populates the appropriate fields on the 'Inspect your model' pop-up and subsequently shows the pop-up
     * @param modelId the specific identifier for the selected model
     * @param currentModelCollaborators a serialized BSON array containing database information to be shown
     */
    public void handleGetCurrentModelPreview(String modelId, String currentModelCollaborators) {
        try {
            BsonArray modelCollaborators = BsonArray.parse(currentModelCollaborators);
            Parent previewRoot;

            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/previewPopUp.fxml"));
            previewRoot = app.viewLoader.load();
            app.previewPopUpView = app.viewLoader.getController();

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
            app.myModelsView.myModelsAnchorPane.getChildren().add(previewRoot);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Determines if the current app user is an editor for a specific model
     * @param modelCollaborators a BSON array containing information on all the collaborators for the current model
     * @return a boolean flag that indicates if the current user is an editor for a specific model
     */
    private boolean isUserAnEditor(BsonArray modelCollaborators) {
        boolean isEditor = false;

        for (BsonValue collaborator : modelCollaborators) {
            if (collaborator.asDocument().get("id").asString().getValue().equals(app.users.ownerId) &&
                    collaborator.asDocument().get("permissions").asString().getValue().equals("Editor")) {
                isEditor = true;
            }
        }

        return isEditor;
    }

    /**
     * Prepares and populates the appropriate fields on the 'Share with others' pop-up and subsequently shows the pop-up
     * @param modelId the specific identifier for the selected model
     * @param currentModelCollaborators a serialized BSON array containing database information to be shown
     */
    public void handleGetCurrentModelCollaborators(String modelId, String currentModelCollaborators) {
        try {
            BsonArray modelCollaborators = BsonArray.parse(currentModelCollaborators);
            Parent shareRoot;

            app.viewLoader = new FXMLLoader(getClass().getResource("/views/myModels/sharePopUp.fxml"));
            shareRoot = app.viewLoader.load();
            app.sharePopUpView = app.viewLoader.getController();

            // Set the id of the shareRootAnchorPane to be equal to the model id
            app.sharePopUpView.shareRootAnchorPane.setId(modelId);

            app.sharePopUpView.addPermissionsChoiceBox.setValue("Viewer");
            app.sharePopUpView.addPermissionsChoiceBox.getItems().addAll("Viewer", "Editor");

            app.sharePopUpView.collaboratorsVBox.getChildren().clear();

            if (modelCollaborators.isEmpty()) {
                // Display a message saying there are no collaborators
                displayNoCollaborators();
            } else {
                // Display collaborators
                for (BsonValue collaborator : modelCollaborators) {
                    GridPane collaboratorInfo = new GridPane();
                    collaboratorInfo.setId(collaborator.asDocument().get("owner_id").asString().getValue());
                    collaboratorInfo.setPrefWidth(315);

                    Text displayName = new Text(collaborator.asDocument().get("displayName").asString().getValue());
                    displayName.setWrappingWidth(190);
                    displayName.setStyle("-fx-fill: #007be8; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                    Text emailAddress = new Text(collaborator.asDocument().get("emailAddress").asString().getValue());
                    emailAddress.setWrappingWidth(190);
                    emailAddress.setStyle("-fx-fill: #181a1d; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

                    ChoiceBox<String> changePermissionsChoiceBox = new ChoiceBox<>();
                    changePermissionsChoiceBox.setPrefWidth(115);
                    changePermissionsChoiceBox.setPrefHeight(35);
                    changePermissionsChoiceBox.setId("changePermissionsChoiceBox");
                    if (app.dashboard.getCollaboratorRoleByModelID(app.dashboard.dbModelsList, modelId, collaborator.asDocument().get("owner_id").asString().getValue()).equals("Editor")) {
                        changePermissionsChoiceBox.setValue("Editor");
                    } else {
                        changePermissionsChoiceBox.setValue("Viewer");
                    }
                    changePermissionsChoiceBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                        if (newValue.equals("Remove")) {
                            app.sharePopUpView.removeCollaboratorPopUp.setId(modelId);
                            app.sharePopUpView.currentCollaborator = collaborator.asDocument().get("owner_id").asString().getValue();
                            app.sharePopUpView.removeCollaboratorPopUp.setVisible(true);
                        } else {
                            BsonDocument updatePermissionsConfiguration = new BsonDocument()
                                    .append("modelId", new BsonObjectId(new ObjectId(modelId)))
                                    .append("collaboratorId", collaborator.asDocument().get("owner_id").asString())
                                    .append("newCollaboratorPermissions", new BsonString(newValue));

                            String functionCall = String.format("ModelBox.Models.changeCurrentModelPermissions('%s');", updatePermissionsConfiguration.toJson());
                            app.mongoApp.eval(functionCall);
                        }
                    });
                    changePermissionsChoiceBox.getItems().addAll("Viewer", "Editor", "Remove");

                    // GridPane configuration
                    GridPane.setConstraints(displayName, 0,0);
                    GridPane.setConstraints(emailAddress, 0, 1);
                    GridPane.setConstraints(changePermissionsChoiceBox, 1, 0);
                    ColumnConstraints colConstraints = new ColumnConstraints();
                    colConstraints.setPrefWidth(190);
                    GridPane.setHalignment(changePermissionsChoiceBox, HPos.LEFT);
                    collaboratorInfo.getColumnConstraints().add(colConstraints);
                    collaboratorInfo.getChildren().addAll(displayName, emailAddress, changePermissionsChoiceBox);

                    Line separator = new Line();
                    separator.setId(collaborator.asDocument().get("owner_id").asString().getValue());
                    separator.setStartX(0);
                    separator.setEndX(300);
                    separator.setStroke(Color.color(0.0941, 0.1019, 0.1137));
                    separator.setStrokeWidth(1.25);
                    app.sharePopUpView.collaboratorsVBox.getChildren().add(collaboratorInfo);
                    app.sharePopUpView.collaboratorsVBox.getChildren().add(separator);
                }
            }
            // Actually launch the share pop-up
            app.myModelsView.myModelsAnchorPane.getChildren().add(shareRoot);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Creates and displays a message in place of a list collaborators that indicates that there are no collaborators
     * for the specific model
     */
    public void displayNoCollaborators() {
        VBox noCollaboratorsMessage = new VBox();
        noCollaboratorsMessage.setSpacing(10);
        noCollaboratorsMessage.setPrefWidth(315);

        Text heading = new Text("No collaborators yet!");
        heading.setWrappingWidth(315);
        heading.setStyle("-fx-fill: #007be8; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");

        Text subHeading = new Text("To start sharing this model with others, add a collaborator.");
        subHeading.setWrappingWidth(315);
        subHeading.setStyle("-fx-fill: #181a1d; -fx-font-size: 14px; -fx-font-family: Arial; -fx-padding: 0; -fx-background-insets: 0");
        noCollaboratorsMessage.getChildren().addAll(heading, subHeading);

        Line separator = new Line();
        separator.setStartX(0);
        separator.setEndX(275);
        separator.setStroke(Color.color(0.0941, 0.1019, 0.1137));
        separator.setStrokeWidth(1.25);
        app.sharePopUpView.collaboratorsVBox.getChildren().add(noCollaboratorsMessage);
        app.sharePopUpView.collaboratorsVBox.getChildren().add(separator);
    }

    /**
     * Gets the file size for a provided 3D model in terms of KB or MB
     * @param bytes the number of bytes that a 3D file has
     * @return a String that cleanly shows the file size of a 3D model
     */
    public String getModelSize(int bytes){
        String modelSizeString;
        bytes /= 1024;

        if(bytes > 1000) {
            modelSizeString = Math.round(bytes / 1024.0) + " MB";
        } else {
            modelSizeString = bytes + " KB";
        }

        return modelSizeString;
    }

    /**
     * Gets the upload date for a provided 3D model
     * @param modelId the specific identifier for a given model
     * @return a String that cleanly shows the date the model was uploaded
     */
    public String getModelTimestamp(String modelId){
        long millis = parseInt(modelId.substring(0,8), 16);
        millis *= 1000;
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");
        Date result = new Date(millis);
        return format.format(result);
    }
}

