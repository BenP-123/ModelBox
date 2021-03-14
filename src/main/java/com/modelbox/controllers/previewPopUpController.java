package com.modelbox.controllers;

import com.modelbox.databaseIO.modelsIO;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class previewPopUpController {

    private double anchorX, anchorY;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    private final DoubleProperty positionX = new SimpleDoubleProperty(187.5);
    private final DoubleProperty positionY = new SimpleDoubleProperty(212.5);

    @FXML public AnchorPane previewModelAnchorPane;
    @FXML public Text modelNameText;
    @FXML public Text modelTypeText;
    @FXML public SubScene previewModelSubScene;


    /**
     * Closes and removes the preview pop-up from view
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void closePreviewBtnClicked(Event event) {
        AnchorPane currentPreview = (AnchorPane) ((Button) event.getSource()).getParent().getParent();
        loginController.dashboard.myModelsView.myModelsAnchorPane.getChildren().remove(currentPreview);
    }

    /**
     * Downloads (really saves) the selected model to the users local computer
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void downloadModelBtnClicked(Event event) {
        AnchorPane currentModel = (AnchorPane) ((Button) event.getSource()).getParent();
        FileChooser fileSaver = new FileChooser();
        fileSaver.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("STL File","*.stl"));
        fileSaver.setTitle("Save 3D Model");
        modelsIO.saveModelFile(currentModel.getId(), (fileSaver.showSaveDialog(loginController.dashboard.dashboardAnchorPane.getScene().getWindow())).toPath());
    }

    /**
     * Creates, styles, and shows the help pane in the preview pop-up
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void helpBtnClicked(Event event) {
        // Add help anchor pane to window.
        AnchorPane helpAnchorPane = new AnchorPane();
        AnchorPane.setTopAnchor(helpAnchorPane, 50.0);
        AnchorPane.setLeftAnchor(helpAnchorPane, 50.0);
        AnchorPane.setRightAnchor(helpAnchorPane, 50.0);
        AnchorPane.setBottomAnchor(helpAnchorPane, 50.0);
        helpAnchorPane.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15px;");

        // Add heading label
        Text helpHeading = new Text("Help");
        helpHeading.setStyle("-fx-fill: #0088ff; -fx-font-size: 24px; -fx-font-family: Arial; -fx-font-weight: bold;");
        helpAnchorPane.getChildren().add(helpHeading);
        AnchorPane.setTopAnchor(helpHeading, 30.0);
        AnchorPane.setLeftAnchor(helpHeading, 30.0);

        // Add rotate instruction
        Text rotateHelpText = new Text("To rotate the model, left click and drag the model to change the angle of the model.");
        rotateHelpText.setStyle("-fx-fill: #171a1d; -fx-font-size: 14px; -fx-font-family: Arial;");
        helpAnchorPane.getChildren().add(rotateHelpText);
        AnchorPane.setTopAnchor(rotateHelpText, 85.0);
        AnchorPane.setLeftAnchor(rotateHelpText, 30.0);
        rotateHelpText.wrappingWidthProperty().bind(helpAnchorPane.widthProperty().subtract(60));

        // Add zoom instruction
        Text zoomHelpText = new Text("To zoom, use the scroll on your mouse to zoom in and out of your model.");
        zoomHelpText.setStyle("-fx-fill: #171a1d; -fx-font-size: 14px; -fx-font-family: Arial;");
        helpAnchorPane.getChildren().add(zoomHelpText);
        AnchorPane.setTopAnchor(zoomHelpText, 155.0);
        AnchorPane.setLeftAnchor(zoomHelpText, 30.0);
        zoomHelpText.wrappingWidthProperty().bind(helpAnchorPane.widthProperty().subtract(60));

        // Add pan instruction
        Text panHelpText = new Text("To pan the model, right click and drag the model to change the position of the model.");
        panHelpText.setStyle("-fx-fill: #171a1d; -fx-font-size: 14px; -fx-font-family: Arial;");
        helpAnchorPane.getChildren().add(panHelpText);
        AnchorPane.setTopAnchor(panHelpText, 225.0);
        AnchorPane.setLeftAnchor(panHelpText, 30.0);
        panHelpText.wrappingWidthProperty().bind(helpAnchorPane.widthProperty().subtract(60));

        // Add close btn
        ImageView closeHelpIcon = new ImageView(new Image("/images/close-btn.png"));
        closeHelpIcon.setFitHeight(30.0);
        closeHelpIcon.setFitWidth(30.0);
        Button closeHelpBtn = new Button();
        closeHelpBtn.setGraphic(closeHelpIcon);
        closeHelpBtn.setOnAction(this::closeHelpBtnClicked);
        closeHelpBtn.setStyle("-fx-background-color: none;");
        helpAnchorPane.getChildren().add(closeHelpBtn);
        AnchorPane.setTopAnchor(closeHelpBtn, 15.0);
        AnchorPane.setRightAnchor(closeHelpBtn, 10.0);

        // Show the help pop-up
        previewModelAnchorPane.getChildren().add(helpAnchorPane);
    }

    /**
     * Closes the help pane in the preview pop-up
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void closeHelpBtnClicked(Event event) {
        // Remove help anchor pane from window
        AnchorPane helpPopUp = (AnchorPane) ((Button) event.getSource()).getParent();
        previewModelAnchorPane.getChildren().remove(helpPopUp);
    }

    /*************************************************** UTILITY METHODS **********************************************/

    /**
     * Sets up the functionality for a user to rotate, zoom, and pan their model
     *
     * @param  meshGroup a JavaFX Group
     * @param  scene     a JavaFX SubScene
     * @param  stage     a JavaFX Stage
     */
    public void initMouseControl(Group meshGroup, SubScene scene, Stage stage) {
        Rotate xRotate;
        Rotate yRotate;
        meshGroup.getTransforms().addAll(
                xRotate = new Rotate(0, Rotate.X_AXIS),
                yRotate = new Rotate(0, Rotate.Y_AXIS)
        );

        xRotate.angleProperty().bind(angleX);
        yRotate.angleProperty().bind(angleY);
        meshGroup.translateXProperty().bind(positionX);
        meshGroup.translateYProperty().bind(positionY);

        scene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                anchorX = event.getSceneX();
                anchorY = event.getSceneY();
                anchorAngleX = angleX.get();
                anchorAngleY = angleY.get();
            }
        });

        scene.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                angleX.set(anchorAngleX - (anchorY - event.getSceneY()));
                angleY.set(anchorAngleY + anchorX - event.getSceneX());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                positionX.set(event.getX());
                positionY.set(event.getY());
            }

        });

        stage.addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            meshGroup.translateZProperty().set(meshGroup.getTranslateZ() - delta);
        });
    }
}
