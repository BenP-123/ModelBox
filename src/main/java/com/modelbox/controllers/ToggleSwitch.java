package com.modelbox.controllers;

import com.modelbox.app;
import javafx.animation.FillTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.Parent;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 * Provides a simple UI implementation for a toggle switch, which is our component to turn dark mode on and off
 */
public class ToggleSwitch extends Parent {

    private BooleanProperty switchedOn = new SimpleBooleanProperty(false);
    private TranslateTransition translateAnimation = new TranslateTransition(Duration.seconds(0.25));
    private FillTransition fillAnimation = new FillTransition(Duration.seconds(0.25));
    private ParallelTransition animation = new ParallelTransition(translateAnimation, fillAnimation);

    /**
     * Sets the status of the switch to the provided value
     * @param state the desired boolean value for the toggle component
     */
    public void setSwitchedOnProperty(Boolean state) {
        switchedOn.setValue(state);
    }

    /**
     * Gets the status of the switch
     * @return the BooleanProperty that holds the internal state of the toggle component
     */
    public BooleanProperty switchedOnProperty() {
        return switchedOn;
    }

    /**
     * Constructs and initializes a ToggleSwitch object
     */
    public ToggleSwitch() {
        Rectangle background = new Rectangle(40, 20);
        background.setArcWidth(20);
        background.setArcHeight(20);
        background.setFill(Color.TRANSPARENT);
        background.setStroke(Color.LIGHTGRAY);

        Circle trigger = new Circle(10);
        trigger.setCenterX(10);
        trigger.setCenterY(10);
        trigger.setFill(Color.WHITE);
        trigger.setStroke(Color.LIGHTGRAY);

        DropShadow shadow = new DropShadow();
        shadow.setRadius(2);
        trigger.setEffect(shadow);

        translateAnimation.setNode(trigger);
        fillAnimation.setShape(background);

        getChildren().addAll(background, trigger);

        switchedOn.addListener((obs, oldState, newState) -> {
            boolean isOn = newState;
            translateAnimation.setToX(isOn ? 40 - 20 : 0);
            fillAnimation.setFromValue(isOn ? Color.TRANSPARENT : Color.color(0, 0.4824, 0.9098));
            fillAnimation.setToValue(isOn ? Color.color(0, 0.4824, 0.9098) : Color.TRANSPARENT);

            if (isOn) {
                app.userPrefs.putBoolean("darkModeActiveStatus", true);

                app.dashboard.dashboardAnchorPane.getStylesheets().remove(0);
                app.dashboard.dashboardAnchorPane.getStylesheets().add(getClass().getResource("/css/dark-mode.css").toString());
            } else {
                app.userPrefs.remove("darkModeActiveStatus");

                app.dashboard.dashboardAnchorPane.getStylesheets().remove(0);
                app.dashboard.dashboardAnchorPane.getStylesheets().add(getClass().getResource("/css/light-mode.css").toString());
            }
            app.isDarkModeActive = app.userPrefs.getBoolean("darkModeActiveStatus", false);

            animation.play();
        });

        setOnMouseClicked(event -> {
            switchedOn.set(!switchedOn.get());
        });
    }
}
