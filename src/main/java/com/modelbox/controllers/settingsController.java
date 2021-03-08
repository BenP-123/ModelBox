package com.modelbox.controllers;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class settingsController {

    @FXML public TextField displayNameTextField;
    @FXML private AnchorPane accountSettingsAnchorPane;
    @FXML private AnchorPane accountSecurityAnchorPane;

    /**
     *   Sets the account settings pane as visible
     *
     *   @param  event a JavaFX Event
     *	 @return void
     */
    @FXML
    private void accountSettingsBtnClicked(Event event) {
        accountSecurityAnchorPane.setVisible(false);
        accountSettingsAnchorPane.setVisible(true);
    }

    /**
     *   Sets the account security pane as visible.
     *
     *   @param  event  a JavaFX Event
     *	 @return void
     */
    @FXML
    private void accountSecurityBtnClicked(Event event) {
        accountSettingsAnchorPane.setVisible(false);
        accountSecurityAnchorPane.setVisible(true);
    }
}
