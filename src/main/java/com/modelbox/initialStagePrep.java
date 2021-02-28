package com.modelbox;

import com.modelbox.controllers.createAccountController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class initialStagePrep extends Application {
    public void start(Stage stage) throws Exception {

        // Load the create account view
        createAccountController createController = new createAccountController();
        FXMLLoader createAccountLoader = new FXMLLoader();
        createAccountLoader.setController(createController);
        Parent root = createAccountLoader.load(getClass().getResource("/views/createAccount.fxml"));
        stage.getIcons().add(new Image(initialStagePrep.class.getResourceAsStream("/images/modelboxLogo.png")));
        stage.setScene(new Scene(root, 1000, 650));
        stage.setMinWidth(1000);
        stage.setMinHeight(650);
        stage.show();
    }
}
