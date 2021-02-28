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
     *   Sets the account settings pane as visible.
     *
     *   @param  e      a JavaFX event with the properties and methods of the element that triggered the event
     *	 @return void   no value returned
     */
    @FXML
    private void accountSettingsBtnClicked(Event e) {
        accountSecurityAnchorPane.setVisible(false);
        accountSettingsAnchorPane.setVisible(true);
    }

    /**
     *   Sets the account security pane as visible.
     *
     *   @param  e      a JavaFX event with the properties and methods of the element that triggered the event
     *	 @return void   no value returned
     */
    @FXML
    private void accountSecurityBtnClicked(Event e) {
        accountSettingsAnchorPane.setVisible(false);
        accountSecurityAnchorPane.setVisible(true);
    }
}
