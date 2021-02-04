package com.modelbox;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class ModelBox extends Application
{
    public void start(Stage primaryStage)
    {
        Button btOK = new Button("Welcome to ModelBox!");

        Scene scene = new Scene(btOK, 400, 250);
        primaryStage.setTitle("ModelBox");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String [] args)
    {
        launch(args);
    }
}
