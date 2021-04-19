package com.modelbox.controllers.myModels;

import com.modelbox.app;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.BsonString;
import org.bson.types.ObjectId;

import java.nio.file.Files;

public class previewPopUpController {

    private double anchorX, anchorY, originalDistance;
    private double anchorAngleX = 0;
    private double anchorAngleY = 0;
    private final DoubleProperty angleX = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY = new SimpleDoubleProperty(0);
    public final DoubleProperty positionX = new SimpleDoubleProperty(0);
    public final DoubleProperty positionY = new SimpleDoubleProperty(0);
    private final ObjectProperty<Color> customColorProperty = new SimpleObjectProperty<>(Color.WHITE);
    private final DoubleProperty hue = new SimpleDoubleProperty(-1);
    private final DoubleProperty sat = new SimpleDoubleProperty(-1);
    private final DoubleProperty bright = new SimpleDoubleProperty(100);

    @FXML public AnchorPane previewModelAnchorPane;
    @FXML public AnchorPane previewInfoAnchorPane;
    @FXML public TextField modelNameEditorTextField;
    @FXML public Text modelNameViewerText;
    @FXML public Text modelSizeText;
    @FXML public Text modelDateText;
    @FXML public Text modelTypeText;
    @FXML public SubScene previewModelSubScene;
    @FXML public Button saveAttributesBtn;
    @FXML public ScrollPane collaboratorsScrollPane;
    @FXML public VBox collaboratorsVBox;
    @FXML public ImageView wireMeshBtnIcon;
    private Boolean isWireMeshToolActive = false;

    @FXML
    private void saveAttributesBtnClicked(Event event) {
        try {
            AnchorPane currentPreview = (AnchorPane) ((Button) event.getSource()).getParent();

            // Share the model with another user in the database
            BsonDocument saveModelConfiguration = new BsonDocument()
                    .append("modelId", new BsonObjectId(new ObjectId(currentPreview.getId())))
                    .append("newModelName", new BsonString(modelNameEditorTextField.getText() + ".stl"));
            String functionCall = String.format("ModelBox.Models.saveCurrentUserModelAttributes('%s');", saveModelConfiguration.toJson());
            app.mongoApp.eval(functionCall);
        } catch (Exception exception) {
            exception.printStackTrace();
        }

    }

    /**
     * Closes and removes the preview pop-up from view
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void closePreviewBtnClicked(Event event) {
        try {
            AnchorPane currentPreview = (AnchorPane) ((Button) event.getSource()).getParent().getParent();
            app.myModelsView.myModelsAnchorPane.getChildren().remove(currentPreview);

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

    /**
     * Downloads (really saves) the selected model to the users local computer
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void downloadModelBtnClicked(Event event) {
        try {
            AnchorPane currentModel = (AnchorPane) ((Button) event.getSource()).getParent();
            FileChooser fileSaver = new FileChooser();
            fileSaver.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("STL File","*.stl"));
            fileSaver.setTitle("Save 3D Model");

            // Load the model file from the database models list
            int modelIndex = app.dashboard.getDocumentIndexByModelID(app.dashboard.dbModelsList, currentModel.getId());
            BsonDocument model = app.dashboard.dbModelsList.get(modelIndex).asDocument();

            Files.write(fileSaver.showSaveDialog(app.dashboard.dashboardAnchorPane.getScene().getWindow()).toPath(), model.get("modelFile").asBinary().getData());
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Creates, styles, and shows the color picker pane in the preview pop-up
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void changeColorBtnClicked(Event event) {
        customColorProperty.addListener((ov, t, t1) -> colorChanged());

        // Add color picker anchor pane to window.
        AnchorPane colorPickerAnchorPane = new AnchorPane();
        AnchorPane.setTopAnchor(colorPickerAnchorPane, 60.0);
        AnchorPane.setLeftAnchor(colorPickerAnchorPane, 60.0);
        AnchorPane.setRightAnchor(colorPickerAnchorPane, 60.0);
        AnchorPane.setBottomAnchor(colorPickerAnchorPane, 60.0);
        colorPickerAnchorPane.setStyle("-fx-background-color: #ffffff; -fx-background-radius: 15px;");

        // Add heading label
        Text colorPickerHeading = new Text("Color Picker");
        colorPickerHeading.setStyle("-fx-fill: #0088ff; -fx-font-size: 24px; -fx-font-family: Arial; -fx-font-weight: bold;");
        AnchorPane.setTopAnchor(colorPickerHeading, 20.0);
        AnchorPane.setLeftAnchor(colorPickerHeading, 30.0);
        colorPickerAnchorPane.getChildren().add(colorPickerHeading);

        // Create a slider bar to select the hue
        Pane hueSliderBar = new Pane();
        hueSliderBar.setPrefHeight(16.0);
        AnchorPane.setLeftAnchor(hueSliderBar, 30.0);
        AnchorPane.setRightAnchor(hueSliderBar, 30.0);
        AnchorPane.setTopAnchor(hueSliderBar, 60.0);
        hueSliderBar.setBackground(new Background(new BackgroundFill(createColorSliderGradient(),
                new CornerRadii(15), Insets.EMPTY)));

        // Create a selector for the hue slider bar
        Circle hueSliderSelector = new Circle(8);
        hueSliderSelector.setStyle("-fx-fill: none; -fx-stroke: #262626; -fx-stroke-width: 2px;");
        hueSliderSelector.setLayoutY(8.0);
        hueSliderSelector.translateXProperty().bind(hue.divide(360).multiply(hueSliderBar.widthProperty()));
        hueSliderBar.getChildren().add(hueSliderSelector);

        // Handle setting the color when sliding
        EventHandler<MouseEvent> hueSliderHandler = hueSliderEvent -> {
            final double x = hueSliderEvent.getX();
            hue.set(clamp(x / hueSliderBar.getWidth()) * 360);
            updateCustomColor();
        };

        hueSliderBar.setOnMousePressed(hueSliderHandler);
        hueSliderBar.setOnMouseDragged(hueSliderHandler);

        // Create a box to select the saturation and color brightness
        StackPane colorBox = new StackPane();
        AnchorPane.setTopAnchor(colorBox,90.0);
        AnchorPane.setLeftAnchor(colorBox, 55.0);
        AnchorPane.setRightAnchor(colorBox, 55.0);
        AnchorPane.setBottomAnchor(colorBox, 90.0);

        // Create the hue pane for the color selection rectangle
        Pane colorBoxHuePanel = new Pane();
        colorBoxHuePanel.backgroundProperty().bind(new ObjectBinding<Background>() {
            {
                bind(hue);
            }

            @Override protected Background computeValue() {
                return new Background(new BackgroundFill(
                        Color.hsb(hue.getValue(), 1.0, 1.0),
                        CornerRadii.EMPTY, Insets.EMPTY));

            }
        });

        // Create saturation and brightness overlays
        Pane colorBoxSaturationPanel = new Pane();
        colorBoxSaturationPanel.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.rgb(255, 255, 255, 1)),
                        new Stop(1, Color.rgb(255, 255, 255, 0))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        Pane colorBoxBrightnessPanel = new Pane();
        colorBoxBrightnessPanel.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.rgb(0, 0, 0, 0)), new Stop(1, Color.rgb(0, 0, 0, 1))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        // Create a selector for the saturation and brightness box
        Circle colorBoxSelector = new Circle(8);
        colorBoxSelector.setStyle("-fx-fill: none; -fx-stroke: #262626; -fx-stroke-width: 2px;");
        colorBoxSelector.translateXProperty().bind(
                sat.divide(100).multiply(colorBox.widthProperty()).subtract(colorBox.widthProperty().divide(2)));
        colorBoxSelector.translateYProperty().bind(
                Bindings.subtract(1, bright.divide(100)).multiply(colorBox.heightProperty()).subtract(colorBox.heightProperty().divide(2)));

        // Handle setting the color when moving the selector in the box
        EventHandler<MouseEvent> colorBoxHandler = colorBoxEvent -> {
            final double x = colorBoxEvent.getX();
            final double y = colorBoxEvent.getY();
            sat.set(clamp(x / colorBox.getWidth()) * 100);
            bright.set(100 - (clamp(y / colorBox.getHeight()) * 100));
            updateCustomColor();
        };

        colorBoxBrightnessPanel.setOnMouseDragged(colorBoxHandler);
        colorBoxBrightnessPanel.setOnMousePressed(colorBoxHandler);

        // Create a new color preview bar
        Pane newColorBar = new Pane();
        newColorBar.setPrefHeight(25.0);
        newColorBar.setStyle("-fx-border-color: #757575; -fx-border-radius: 15px;");
        AnchorPane.setLeftAnchor(newColorBar, 30.0);
        AnchorPane.setRightAnchor(newColorBar, 30.0);
        AnchorPane.setBottomAnchor(newColorBar, 55.0);
        newColorBar.backgroundProperty().bind(new ObjectBinding<Background>() {
            {
                bind(customColorProperty);
            }

            @Override protected Background computeValue() {
                return new Background(new BackgroundFill(customColorProperty.get(), new CornerRadii(15), Insets.EMPTY));
            }
        });

        // Add color selection items to color picker
        colorBox.getChildren().setAll(colorBoxHuePanel, colorBoxSaturationPanel, colorBoxBrightnessPanel, colorBoxSelector);
        colorPickerAnchorPane.getChildren().addAll(hueSliderBar, colorBox, newColorBar);

        Button changeModelColorBtn = new Button();
        changeModelColorBtn.setOnAction(this::changeModelColorBtnClicked);
        changeModelColorBtn.setStyle("-fx-background-color: #171a1d; -fx-background-radius: 15px; -fx-font-family: Arial; -fx-text-fill: white;");
        changeModelColorBtn.setText("Change Model Color");
        colorPickerAnchorPane.getChildren().add(changeModelColorBtn);
        AnchorPane.setBottomAnchor(changeModelColorBtn, 20.0);
        AnchorPane.setLeftAnchor(changeModelColorBtn, 30.0);
        AnchorPane.setRightAnchor(changeModelColorBtn, 30.0);

        // Add close btn
        ImageView closeColorPickerIcon = new ImageView(new Image("/images/close-btn.png"));
        closeColorPickerIcon.setFitHeight(30.0);
        closeColorPickerIcon.setFitWidth(30.0);
        Button closeColorPickerBtn = new Button();
        closeColorPickerBtn.setGraphic(closeColorPickerIcon);
        closeColorPickerBtn.setOnAction(this::closeColorPickerBtnClicked);
        closeColorPickerBtn.setStyle("-fx-background-color: none;");
        colorPickerAnchorPane.getChildren().add(closeColorPickerBtn);
        AnchorPane.setTopAnchor(closeColorPickerBtn, 15.0);
        AnchorPane.setRightAnchor(closeColorPickerBtn, 10.0);

        // Show the help pop-up
        previewModelAnchorPane.getChildren().add(colorPickerAnchorPane);
    }

    /**
     * Sets HSB color property values when the user changes the custom color property
     *
     */
    private void colorChanged() {
        hue.set(customColorProperty.get().getHue());
        sat.set(customColorProperty.get().getSaturation() * 100);
        bright.set(customColorProperty.get().getBrightness() * 100);
    }

    /**
     * Updates the custom color property with the color specified by the user
     *
     */
    private void updateCustomColor() {
        Color newColor = Color.hsb(hue.get(), clamp(sat.get() / 100),
                clamp(bright.get() / 100), 1.0);
        customColorProperty.set(newColor);
    }

    /**
     * Processes a value to be between 0 and 1 for future use
     *
     * @return a double value between 0 and 1 inclusive
     */
    private double clamp(double value) {
        return value < 0 ? 0 : value > 1 ? 1 : value;
    }

    /**
     * Creates a hue gradient for use in a color selection slider
     *
     * @return a JavaFX LinearGradient
     */
    private LinearGradient createColorSliderGradient() {
        double offset;
        Stop[] stops = new Stop[255];
        for (int x = 0; x < 255; x++) {
            offset = (double)((1.0 / 255) * x);
            int h = (int)((x / 255.0) * 360);
            stops[x] = new Stop(offset, Color.hsb(h, 1.0, 1.0));
        }
        return new LinearGradient(0f, 0f, 1f, 0f, true, CycleMethod.NO_CYCLE, stops);
    }

    /**
     * Closes the color picker pane in the preview pop-up
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void closeColorPickerBtnClicked(Event event) {
        // Remove color picker anchor pane from window
        AnchorPane colorPickerPopUp = (AnchorPane) ((Button) event.getSource()).getParent();
        previewModelAnchorPane.getChildren().remove(colorPickerPopUp);
    }

    /**
     * Changes the model color currently in the interactive preview panel
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void changeModelColorBtnClicked(Event event) {
        MeshView modelMesh = (MeshView) previewModelSubScene.getRoot().getChildrenUnmodifiable().get(0);
        modelMesh.setMaterial(new PhongMaterial(customColorProperty.get()));

        // Close the color picker to show the model with the new color
        closeColorPickerBtnClicked(event);
    }

    /**
     * Zooms in on the model currently in the interactive preview panel
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void zoomInBtnClicked(Event event) {
        previewModelSubScene.getRoot().setTranslateZ(previewModelSubScene.getRoot().getTranslateZ() - 100);
    }

    /**
     * Zooms out of the model currently in the interactive preview panel
     *
     * @param event a JavaFX Event
     */
    @FXML
    private void zoomOutBtnClicked(Event event) {
        previewModelSubScene.getRoot().setTranslateZ(previewModelSubScene.getRoot().getTranslateZ() + 100);
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
        Text rotateHelpText = new Text("- To rotate the model, left click and drag to change the angle.");
        rotateHelpText.setStyle("-fx-fill: #171a1d; -fx-font-size: 14px; -fx-font-family: Arial;");
        AnchorPane.setTopAnchor(rotateHelpText, 65.0);
        AnchorPane.setLeftAnchor(rotateHelpText, 30.0);
        rotateHelpText.wrappingWidthProperty().bind(helpAnchorPane.widthProperty().subtract(60));
        helpAnchorPane.getChildren().add(rotateHelpText);

        // Add zoom instruction
        Text zoomHelpText = new Text("- To zoom, use the scroll on your mouse or the plus and minus buttons.");
        zoomHelpText.setStyle("-fx-fill: #171a1d; -fx-font-size: 14px; -fx-font-family: Arial;");
        AnchorPane.setTopAnchor(zoomHelpText, 110.0);
        AnchorPane.setLeftAnchor(zoomHelpText, 30.0);
        zoomHelpText.wrappingWidthProperty().bind(helpAnchorPane.widthProperty().subtract(60));
        helpAnchorPane.getChildren().add(zoomHelpText);

        // Add pan instruction
        Text panHelpText = new Text("- To pan the model, right click and drag to change the position.");
        panHelpText.setStyle("-fx-fill: #171a1d; -fx-font-size: 14px; -fx-font-family: Arial;");
        AnchorPane.setTopAnchor(panHelpText, 170.0);
        AnchorPane.setLeftAnchor(panHelpText, 30.0);
        panHelpText.wrappingWidthProperty().bind(helpAnchorPane.widthProperty().subtract(60));
        helpAnchorPane.getChildren().add(panHelpText);

        // Add change inspection color instruction
        Text colorHelpText = new Text("- To change the inspection color for the model, click the droplet button.");
        colorHelpText.setStyle("-fx-fill: #171a1d; -fx-font-size: 14px; -fx-font-family: Arial;");
        AnchorPane.setTopAnchor(colorHelpText, 215.0);
        AnchorPane.setLeftAnchor(colorHelpText, 30.0);
        colorHelpText.wrappingWidthProperty().bind(helpAnchorPane.widthProperty().subtract(60));
        helpAnchorPane.getChildren().add(colorHelpText);

        // Add change inspection color instruction
        Text resetHelpText = new Text("- To reset the orientation, click the backward pointing arrow.");
        resetHelpText.setStyle("-fx-fill: #171a1d; -fx-font-size: 14px; -fx-font-family: Arial;");
        AnchorPane.setTopAnchor(resetHelpText, 270.0);
        AnchorPane.setLeftAnchor(resetHelpText, 30.0);
        resetHelpText.wrappingWidthProperty().bind(helpAnchorPane.widthProperty().subtract(60));
        helpAnchorPane.getChildren().add(resetHelpText);


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

    @FXML
    private void resetViewBtnClicked(Event event) {
        // Center the model
        positionX.bind(previewModelAnchorPane.widthProperty().divide(2));
        positionY.bind(previewModelAnchorPane.heightProperty().divide(2));

        // Reset the rotation angle
        angleX.set(0.0);
        angleY.set(0.0);

        // Reset the models distance from the camera
        previewModelSubScene.getRoot().setTranslateZ(originalDistance);
    }

    @FXML
    private void wireMeshBtnClicked(Event event) {
        if (isWireMeshToolActive) {
            isWireMeshToolActive = false;
            wireMeshBtnIcon.setImage(new Image(String.valueOf(getClass().getResource("/images/wire-mesh-btn.png"))));
            ((MeshView) ((Group) previewModelSubScene.getRoot()).getChildren().get(0)).setDrawMode(DrawMode.FILL);
        } else {
            isWireMeshToolActive = true;
            wireMeshBtnIcon.setImage(new Image(String.valueOf(getClass().getResource("/images/wire-mesh-btn-active.png"))));
            ((MeshView) ((Group) previewModelSubScene.getRoot()).getChildren().get(0)).setDrawMode(DrawMode.LINE);
        }
    }

    /*************************************************** UTILITY METHODS **********************************************/

    /**
     * Sets up the functionality for a user to rotate, zoom, and pan their model
     *
     * @param  meshGroup a JavaFX Group
     * @param  scene     a JavaFX SubScene
     */
    public void initMouseControl(Group meshGroup, SubScene scene) {
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
        originalDistance = meshGroup.getTranslateZ();

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
                positionX.unbind();
                positionY.unbind();

                positionX.set(event.getX());
                positionY.set(event.getY());
            }

        });

        scene.getParent().addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            meshGroup.translateZProperty().set(meshGroup.getTranslateZ() - delta);
        });
    }
}
