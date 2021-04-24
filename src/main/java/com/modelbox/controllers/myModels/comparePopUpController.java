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
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.SubScene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.Line;
import javafx.scene.shape.MeshView;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.stage.FileChooser;
import org.bson.BsonDocument;
import java.nio.file.Files;

/**
 * Provides a JavaFX controller implementation for the comparePopUp.fxml view
 */
public class comparePopUpController {

    @FXML public AnchorPane compareModelAnchorPane1;
    @FXML public Text modelName1;
    @FXML public SubScene compareModelSubScene1;
    @FXML public ImageView wireMeshBtn1Icon;
    @FXML public AnchorPane compareModelAnchorPane2;
    @FXML public Text modelName2;
    @FXML public SubScene compareModelSubScene2;
    @FXML public ImageView wireMeshBtn2Icon;
    @FXML public AnchorPane compareRootAnchorPane;
    @FXML public Line dividerLine;

    private double anchorX1, anchorY1, originalDistance1, originalDistance2;
    private double anchorAngleX1 = 0;
    private double anchorAngleY1 = 0;
    private final DoubleProperty angleX1 = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY1 = new SimpleDoubleProperty(0);
    public final DoubleProperty positionX1 = new SimpleDoubleProperty(0);
    public final DoubleProperty positionY1 = new SimpleDoubleProperty(0);
    private final ObjectProperty<Color> customColorProperty1 = new SimpleObjectProperty<>(Color.WHITE);
    private final DoubleProperty hue1 = new SimpleDoubleProperty(-1);
    private final DoubleProperty sat1 = new SimpleDoubleProperty(-1);
    private final DoubleProperty bright1 = new SimpleDoubleProperty(100);

    private double anchorX2, anchorY2;
    private double anchorAngleX2 = 0;
    private double anchorAngleY2 = 0;
    private final DoubleProperty angleX2 = new SimpleDoubleProperty(0);
    private final DoubleProperty angleY2 = new SimpleDoubleProperty(0);
    public final DoubleProperty positionX2 = new SimpleDoubleProperty(0);
    public final DoubleProperty positionY2 = new SimpleDoubleProperty(0);
    private final ObjectProperty<Color> customColorProperty2 = new SimpleObjectProperty<>(Color.WHITE);
    private final DoubleProperty hue2 = new SimpleDoubleProperty(-1);
    private final DoubleProperty sat2 = new SimpleDoubleProperty(-1);
    private final DoubleProperty bright2 = new SimpleDoubleProperty(100);

    private Boolean isWireMesh1ToolActive = false;
    private Boolean isWireMesh2ToolActive = false;

    /**
     * Closes and removes the comparison pop-up from view
     * @param event a JavaFX Event
     */
    @FXML
    private void closeCompareBtnClicked(Event event) {
        AnchorPane currentComparison = (AnchorPane) ((Button) event.getSource()).getParent().getParent();

        app.myModelsView.checkboxCount = 0;
        for (int i = 0; i <  app.myModelsView.myModelsFlowPane.getChildren().size(); i++) {
            // Uncheck each checkbox and remove visibility
            ((CheckBox)  app.myModelsView.myModelsFlowPane.getChildren().get(i).lookup("#checkbox")).setSelected(false);
            app.myModelsView.myModelsFlowPane.getChildren().get(i).lookup("#checkbox").setVisible(false);
        }

        app.myModelsView.compareModelsBtnIcon.setImage(new Image(String.valueOf(getClass().getResource("/images/compare-model-btn.png"))));
        app.myModelsView.myModelsAnchorPane.getChildren().remove(currentComparison);
    }

    /**
     * Downloads the selected model to the user's computer
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
     * Creates, styles, and shows the color picker pane in the first interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void changeColorBtn1Clicked(Event event) {
        customColorProperty1.addListener((ov, t, t1) -> colorChanged1());

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
        hueSliderSelector.translateXProperty().bind(hue1.divide(360).multiply(hueSliderBar.widthProperty()));
        hueSliderBar.getChildren().add(hueSliderSelector);

        // Handle setting the color when sliding
        EventHandler<MouseEvent> hueSliderHandler = hueSliderEvent -> {
            final double x = hueSliderEvent.getX();
            hue1.set(clamp(x / hueSliderBar.getWidth()) * 360);
            updateCustomColor1();
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
                bind(hue1);
            }

            @Override protected Background computeValue() {
                return new Background(new BackgroundFill(
                        Color.hsb(hue1.getValue(), 1.0, 1.0),
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
                sat1.divide(100).multiply(colorBox.widthProperty()).subtract(colorBox.widthProperty().divide(2)));
        colorBoxSelector.translateYProperty().bind(
                Bindings.subtract(1, bright1.divide(100)).multiply(colorBox.heightProperty()).subtract(colorBox.heightProperty().divide(2)));

        // Handle setting the color when moving the selector in the box
        EventHandler<MouseEvent> colorBoxHandler = colorBoxEvent -> {
            final double x = colorBoxEvent.getX();
            final double y = colorBoxEvent.getY();
            sat1.set(clamp(x / colorBox.getWidth()) * 100);
            bright1.set(100 - (clamp(y / colorBox.getHeight()) * 100));
            updateCustomColor1();
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
                bind(customColorProperty1);
            }

            @Override protected Background computeValue() {
                return new Background(new BackgroundFill(customColorProperty1.get(), new CornerRadii(15), Insets.EMPTY));
            }
        });

        // Add color selection items to color picker
        colorBox.getChildren().setAll(colorBoxHuePanel, colorBoxSaturationPanel, colorBoxBrightnessPanel, colorBoxSelector);
        colorPickerAnchorPane.getChildren().addAll(hueSliderBar, colorBox, newColorBar);

        Button changeModelColorBtn = new Button();
        changeModelColorBtn.setOnAction(this::changeModelColorBtn1Clicked);
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
        closeColorPickerBtn.setOnAction(this::closeColorPickerBtn1Clicked);
        closeColorPickerBtn.setStyle("-fx-background-color: none;");
        colorPickerAnchorPane.getChildren().add(closeColorPickerBtn);
        AnchorPane.setTopAnchor(closeColorPickerBtn, 15.0);
        AnchorPane.setRightAnchor(closeColorPickerBtn, 10.0);

        // Show the color picker pop-up
        compareModelAnchorPane1.getChildren().add(colorPickerAnchorPane);
    }

    /**
     * Creates, styles, and shows the color picker pane in the second interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void changeColorBtn2Clicked(Event event) {
        customColorProperty2.addListener((ov, t, t1) -> colorChanged2());

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
        hueSliderSelector.translateXProperty().bind(hue2.divide(360).multiply(hueSliderBar.widthProperty()));
        hueSliderBar.getChildren().add(hueSliderSelector);

        // Handle setting the color when sliding
        EventHandler<MouseEvent> hueSliderHandler = hueSliderEvent -> {
            final double x = hueSliderEvent.getX();
            hue2.set(clamp(x / hueSliderBar.getWidth()) * 360);
            updateCustomColor2();
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
                bind(hue2);
            }

            @Override protected Background computeValue() {
                return new Background(new BackgroundFill(
                        Color.hsb(hue2.getValue(), 1.0, 1.0),
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
                sat2.divide(100).multiply(colorBox.widthProperty()).subtract(colorBox.widthProperty().divide(2)));
        colorBoxSelector.translateYProperty().bind(
                Bindings.subtract(1, bright2.divide(100)).multiply(colorBox.heightProperty()).subtract(colorBox.heightProperty().divide(2)));

        // Handle setting the color when moving the selector in the box
        EventHandler<MouseEvent> colorBoxHandler = colorBoxEvent -> {
            final double x = colorBoxEvent.getX();
            final double y = colorBoxEvent.getY();
            sat2.set(clamp(x / colorBox.getWidth()) * 100);
            bright2.set(100 - (clamp(y / colorBox.getHeight()) * 100));
            updateCustomColor2();
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
                bind(customColorProperty2);
            }

            @Override protected Background computeValue() {
                return new Background(new BackgroundFill(customColorProperty2.get(), new CornerRadii(15), Insets.EMPTY));
            }
        });

        // Add color selection items to color picker
        colorBox.getChildren().setAll(colorBoxHuePanel, colorBoxSaturationPanel, colorBoxBrightnessPanel, colorBoxSelector);
        colorPickerAnchorPane.getChildren().addAll(hueSliderBar, colorBox, newColorBar);

        Button changeModelColorBtn = new Button();
        changeModelColorBtn.setOnAction(this::changeModelColorBtn2Clicked);
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
        closeColorPickerBtn.setOnAction(this::closeColorPickerBtn2Clicked);
        closeColorPickerBtn.setStyle("-fx-background-color: none;");
        colorPickerAnchorPane.getChildren().add(closeColorPickerBtn);
        AnchorPane.setTopAnchor(closeColorPickerBtn, 15.0);
        AnchorPane.setRightAnchor(closeColorPickerBtn, 10.0);

        // Show the color picker pop-up
        compareModelAnchorPane2.getChildren().add(colorPickerAnchorPane);
    }

    /**
     * Sets HSB color property values when the user changes the first custom color property
     */
    private void colorChanged1() {
        hue1.set(customColorProperty1.get().getHue());
        sat1.set(customColorProperty1.get().getSaturation() * 100);
        bright1.set(customColorProperty1.get().getBrightness() * 100);
    }

    /**
     * Sets HSB color property values when the user changes the second custom color property
     */
    private void colorChanged2() {
        hue2.set(customColorProperty2.get().getHue());
        sat2.set(customColorProperty2.get().getSaturation() * 100);
        bright2.set(customColorProperty2.get().getBrightness() * 100);
    }

    /**
     * Updates the first custom color property with the color specified by the user
     */
    private void updateCustomColor1() {
        Color newColor = Color.hsb(hue1.get(), clamp(sat1.get() / 100),
                clamp(bright1.get() / 100), 1.0);
        customColorProperty1.set(newColor);
    }

    /**
     * Updates the second custom color property with the color specified by the user
     */
    private void updateCustomColor2() {
        Color newColor = Color.hsb(hue2.get(), clamp(sat2.get() / 100),
                clamp(bright2.get() / 100), 1.0);
        customColorProperty2.set(newColor);
    }

    /**
     * Processes a value to be between 0 and 1 for future use
     * @return a double value between 0 and 1 inclusive
     */
    private double clamp(double value) {
        return value < 0 ? 0 : value > 1 ? 1 : value;
    }

    /**
     * Creates a hue gradient for use in a color selection slider
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
     * Closes the color picker pane in the first interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void closeColorPickerBtn1Clicked(Event event) {
        AnchorPane colorPickerPopUp = (AnchorPane) ((Button) event.getSource()).getParent();
        compareModelAnchorPane1.getChildren().remove(colorPickerPopUp);
    }

    /**
     * Closes the color picker pane in the second interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void closeColorPickerBtn2Clicked(Event event) {
        AnchorPane colorPickerPopUp = (AnchorPane) ((Button) event.getSource()).getParent();
        compareModelAnchorPane2.getChildren().remove(colorPickerPopUp);
    }

    /**
     * Changes the model color currently in the first interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void changeModelColorBtn1Clicked(Event event) {
        MeshView modelMesh = (MeshView) compareModelSubScene1.getRoot().getChildrenUnmodifiable().get(0);
        modelMesh.setMaterial(new PhongMaterial(customColorProperty1.get()));

        // Close the color picker to show the model with the new color
        closeColorPickerBtn1Clicked(event);
    }

    /**
     * Changes the model color currently in the second interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void changeModelColorBtn2Clicked(Event event) {
        MeshView modelMesh = (MeshView) compareModelSubScene2.getRoot().getChildrenUnmodifiable().get(0);
        modelMesh.setMaterial(new PhongMaterial(customColorProperty2.get()));

        // Close the color picker to show the model with the new color
        closeColorPickerBtn2Clicked(event);
    }

    /**
     * Zooms in on the model currently in the first interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void zoomInBtn1Clicked(Event event) {
        compareModelSubScene1.getRoot().setTranslateZ(compareModelSubScene1.getRoot().getTranslateZ() - 100);
    }

    /**
     * Zooms in on the model currently in the second interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void zoomInBtn2Clicked(Event event) {
        compareModelSubScene2.getRoot().setTranslateZ(compareModelSubScene2.getRoot().getTranslateZ() - 100);
    }

    /**
     * Zooms out of the model currently in the first interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void zoomOutBtn1Clicked(Event event) {
        compareModelSubScene1.getRoot().setTranslateZ(compareModelSubScene1.getRoot().getTranslateZ() + 100);
    }

    /**
     * Zooms out of the model currently in the second interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void zoomOutBtn2Clicked(Event event) {
        compareModelSubScene2.getRoot().setTranslateZ(compareModelSubScene2.getRoot().getTranslateZ() + 100);
    }

    /**
     * Creates, styles, and shows the help pane in the first interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void helpBtn1Clicked(Event event) {
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
        closeHelpBtn.setOnAction(this::closeHelpBtn1Clicked);
        closeHelpBtn.setStyle("-fx-background-color: none;");
        helpAnchorPane.getChildren().add(closeHelpBtn);
        AnchorPane.setTopAnchor(closeHelpBtn, 15.0);
        AnchorPane.setRightAnchor(closeHelpBtn, 10.0);

        // Show the help pop-up
        compareModelAnchorPane1.getChildren().add(helpAnchorPane);
    }

    /**
     * Creates, styles, and shows the help pane in the second interactive panel
     * @param event a JavaFX Event
     */
    @FXML
    private void helpBtn2Clicked(Event event) {
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
        closeHelpBtn.setOnAction(this::closeHelpBtn2Clicked);
        closeHelpBtn.setStyle("-fx-background-color: none;");
        helpAnchorPane.getChildren().add(closeHelpBtn);
        AnchorPane.setTopAnchor(closeHelpBtn, 15.0);
        AnchorPane.setRightAnchor(closeHelpBtn, 10.0);

        // Show the help pop-up
        compareModelAnchorPane2.getChildren().add(helpAnchorPane);
    }

    /**
     * Closes the help pane in the first interactive panel section
     * @param event a JavaFX Event
     */
    @FXML
    private void closeHelpBtn1Clicked(Event event) {
        AnchorPane helpPopUp = (AnchorPane) ((Button) event.getSource()).getParent();
        compareModelAnchorPane1.getChildren().remove(helpPopUp);
    }

    /**
     * Closes the help pane in the second interactive panel section
     * @param event a JavaFX Event
     */
    @FXML
    private void closeHelpBtn2Clicked(Event event) {
        AnchorPane helpPopUp = (AnchorPane) ((Button) event.getSource()).getParent();
        compareModelAnchorPane2.getChildren().remove(helpPopUp);
    }

    /**
     * Sets the orientation in the first interactive preview panel back to the default view
     * @param event a JavaFX Event
     */
    @FXML
    private void resetViewBtn1Clicked(Event event) {
        // Center the model
        positionX1.bind(compareModelAnchorPane1.widthProperty().divide(2));
        positionY1.bind(compareModelAnchorPane1.heightProperty().divide(2));

        // Reset the rotation angle
        angleX1.set(0.0);
        angleY1.set(0.0);

        // Reset the models distance from the camera
        compareModelSubScene1.getRoot().setTranslateZ(originalDistance1);
    }

    /**
     * Sets the orientation in the second interactive preview panel back to the default view
     * @param event a JavaFX Event
     */
    @FXML
    private void resetViewBtn2Clicked(Event event) {
        // Center the model
        positionX2.bind(compareModelAnchorPane2.widthProperty().divide(2));
        positionY2.bind(compareModelAnchorPane2.heightProperty().divide(2));

        // Reset the rotation angle
        angleX2.set(0.0);
        angleY2.set(0.0);

        // Reset the models distance from the camera
        compareModelSubScene2.getRoot().setTranslateZ(originalDistance2);
    }

    /**
     * Toggles the visibility of the first model's mesh, instead of a solid model
     * @param event a JavaFX Event
     */
    @FXML
    private void wireMeshBtn1Clicked(Event event) {
        if (isWireMesh1ToolActive) {
            isWireMesh1ToolActive = false;
            wireMeshBtn1Icon.setImage(new Image(String.valueOf(getClass().getResource("/images/wire-mesh-btn.png"))));
            ((MeshView) ((Group) compareModelSubScene1.getRoot()).getChildren().get(0)).setDrawMode(DrawMode.FILL);
        } else {
            isWireMesh1ToolActive = true;
            wireMeshBtn1Icon.setImage(new Image(String.valueOf(getClass().getResource("/images/wire-mesh-btn-active.png"))));
            ((MeshView) ((Group) compareModelSubScene1.getRoot()).getChildren().get(0)).setDrawMode(DrawMode.LINE);
        }
    }

    /**
     * Toggles the visibility of the second model's mesh, instead of a solid model
     * @param event a JavaFX Event
     */
    @FXML
    private void wireMeshBtn2Clicked(Event event) {
        if (isWireMesh2ToolActive) {
            isWireMesh2ToolActive = false;
            wireMeshBtn2Icon.setImage(new Image(String.valueOf(getClass().getResource("/images/wire-mesh-btn.png"))));
            ((MeshView) ((Group) compareModelSubScene2.getRoot()).getChildren().get(0)).setDrawMode(DrawMode.FILL);
        } else {
            isWireMesh2ToolActive = true;
            wireMeshBtn2Icon.setImage(new Image(String.valueOf(getClass().getResource("/images/wire-mesh-btn-active.png"))));
            ((MeshView) ((Group) compareModelSubScene2.getRoot()).getChildren().get(0)).setDrawMode(DrawMode.LINE);
        }
    }

    /**
     * Sets up the functionality for a user to rotate, zoom, and pan the first selected model
     * @param meshGroup a JavaFX Group
     * @param scene a JavaFX SubScene
     */
    public void initMouseControlModel1(Group meshGroup, SubScene scene) {
        Rotate xRotate1;
        Rotate yRotate1;
        meshGroup.getTransforms().addAll(
                xRotate1 = new Rotate(0, Rotate.X_AXIS),
                yRotate1 = new Rotate(0, Rotate.Y_AXIS)
        );

        xRotate1.angleProperty().bind(angleX1);
        yRotate1.angleProperty().bind(angleY1);
        meshGroup.translateXProperty().bind(positionX1);
        meshGroup.translateYProperty().bind(positionY1);
        originalDistance1 = meshGroup.getTranslateZ();

        scene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                anchorX1 = event.getSceneX();
                anchorY1 = event.getSceneY();
                anchorAngleX1 = angleX1.get();
                anchorAngleY1 = angleY1.get();
            }
        });

        scene.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                angleX1.set(anchorAngleX1 - (anchorY1 - event.getSceneY()));
                angleY1.set(anchorAngleY1 + anchorX1 - event.getSceneX());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                app.comparePopUpView.positionX1.unbind();
                app.comparePopUpView.positionY1.unbind();

                positionX1.set(event.getX());
                positionY1.set(event.getY());
            }

        });

        scene.getParent().addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            meshGroup.translateZProperty().set(meshGroup.getTranslateZ() - delta);
        });
    }

    /**
     * Sets up the functionality for a user to rotate, zoom, and pan the second selected model
     * @param meshGroup a JavaFX Group
     * @param scene a JavaFX SubScene
     */
    public void initMouseControlModel2(Group meshGroup, SubScene scene) {
        Rotate xRotate2;
        Rotate yRotate2;
        meshGroup.getTransforms().addAll(
                xRotate2 = new Rotate(0, Rotate.X_AXIS),
                yRotate2 = new Rotate(0, Rotate.Y_AXIS)
        );

        xRotate2.angleProperty().bind(angleX2);
        yRotate2.angleProperty().bind(angleY2);
        meshGroup.translateXProperty().bind(positionX2);
        meshGroup.translateYProperty().bind(positionY2);
        originalDistance2 = meshGroup.getTranslateZ();

        scene.setOnMousePressed(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                anchorX2 = event.getSceneX();
                anchorY2 = event.getSceneY();
                anchorAngleX2 = angleX2.get();
                anchorAngleY2 = angleY2.get();
            }
        });

        scene.setOnMouseDragged(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                angleX2.set(anchorAngleX2 - (anchorY2 - event.getSceneY()));
                angleY2.set(anchorAngleY2 + anchorX2 - event.getSceneX());
            } else if (event.getButton() == MouseButton.SECONDARY) {
                app.comparePopUpView.positionX2.unbind();
                app.comparePopUpView.positionY2.unbind();

                positionX2.set(event.getX());
                positionY2.set(event.getY());
            }

        });

        scene.getParent().addEventHandler(ScrollEvent.SCROLL, event -> {
            double delta = event.getDeltaY();
            meshGroup.translateZProperty().set(meshGroup.getTranslateZ() - delta);
        });
    }
}
