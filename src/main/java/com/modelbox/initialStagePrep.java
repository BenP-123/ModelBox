package com.modelbox;

import com.modelbox.controllers.createAccountController;
import com.modelbox.controllers.dashboardController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.fxml.FXMLLoader;

public class initialStagePrep extends Application {
    public void start(Stage stage) throws Exception {
        dashboardController dashController = new dashboardController();
        createAccountController createController = new createAccountController();

        Boolean loggedIn = false;

        if (loggedIn) {
            FXMLLoader dashboardLoader = new FXMLLoader();
            dashboardLoader.setController(dashController);
            Parent root = dashboardLoader.load(getClass().getResource("/views/dashboard.fxml"));
            stage.getIcons().add(new Image(initialStagePrep.class.getResourceAsStream("/images/Logo.png")));
            stage.setScene(new Scene(root, 1000, 650));
            stage.setMinWidth(1000);
            stage.setMinHeight(650);
            stage.show();
        } else {
            FXMLLoader createAccountLoader = new FXMLLoader();
            createAccountLoader.setController(createController);
            Parent root = createAccountLoader.load(getClass().getResource("/views/createAccount.fxml"));
            stage.getIcons().add(new Image(initialStagePrep.class.getResourceAsStream("/images/Logo.png")));
            stage.setScene(new Scene(root, 1000, 650));
            stage.setMinWidth(1000);
            stage.setMinHeight(650);
            stage.show();
        }

    }
}
