package com.modelbox;

import com.modelbox.controllers.account.profileController;
import com.modelbox.controllers.account.settingsController;
import com.modelbox.controllers.auth.accountDeletedController;
import com.modelbox.controllers.auth.createAccountController;
import com.modelbox.controllers.dashboardController;
import com.modelbox.controllers.auth.forgotPasswordController;
import com.modelbox.controllers.auth.loginController;
import com.modelbox.controllers.myModels.*;
import com.modelbox.controllers.uploadModels.editPopUpController;
import com.modelbox.controllers.uploadModels.uploadModelsController;
import com.modelbox.controllers.uploadModels.verifyModelsController;
import com.modelbox.mongo.authBridge;
import com.modelbox.mongo.errorBridge;
import com.modelbox.mongo.modelsBridge;
import com.modelbox.mongo.usersBridge;
import javafx.application.Application;
import javafx.concurrent.Worker;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

import java.io.File;
import java.util.prefs.Preferences;

public class app extends Application{

    public static JSObject mongoApp;
    private final WebEngine mongoEngine;
    public static FXMLLoader viewLoader;
    public static accountDeletedController accountDeletedView;
    public static loginController loginView;
    public static dashboardController dashboard;
    public static myModelsController myModelsView;
    public static previewPopUpController previewPopUpView;
    public static comparePopUpController comparePopUpView;
    public static sharePopUpController sharePopUpView;
    public static uploadModelsController uploadModelsView;
    public static verifyModelsController verifyModelsView;
    public static editPopUpController editPopUpView;
    public static profileController profileView;
    public static settingsController settingsView;
    public static createAccountController createAccountView;
    public static forgotPasswordController forgotPasswordView;
    public static authBridge auth;
    public static modelsBridge models;
    public static usersBridge users;
    public static errorBridge errors;
    public static Preferences userPrefs;
    public static Boolean isDarkModeActive = false;

    public app() {
        userPrefs = Preferences.userRoot().node("/com/modelbox");
        isDarkModeActive = userPrefs.getBoolean("darkModeActiveStatus", isDarkModeActive);
        viewLoader = new FXMLLoader(getClass().getResource("/views/auth/login.fxml"));
        mongoEngine = new WebEngine();
        mongoEngine.setJavaScriptEnabled(true);
        auth = new authBridge();
        models = new modelsBridge();
        users = new usersBridge();
        errors = new errorBridge();
    }

    @Override
    public void start(Stage stage) {
        mongoEngine.getLoadWorker().stateProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == Worker.State.SUCCEEDED) {
                try {
                    mongoApp = (JSObject) mongoEngine.executeScript("window");
                    mongoApp.setMember("AuthBridge", auth);
                    mongoApp.setMember("ModelsBridge", models);
                    mongoApp.setMember("UsersBridge", users);
                    mongoApp.setMember("ErrorBridge", errors);

                    Parent root = viewLoader.load();
                    app.loginView = viewLoader.getController();
                    stage.getIcons().add(new Image(app.class.getResourceAsStream("/images/modelboxLogo.png")));
                    stage.setScene(new Scene(root, 1000, 650));
                    stage.setMinWidth(1000);
                    stage.setMinHeight(650);
                    stage.show();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });

        mongoEngine.load("https://modelbox-vqzyc.mongodbstitch.com/");
    }
}
