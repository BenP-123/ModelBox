package com.modelbox;

import com.modelbox.MongoDriver.CreateUser;
import com.modelbox.MongoDriver.MongoDriver;
import javafx.application.Application;

public class LaunchApp {

    public static void main(String[] args) {
        Application.launch(ModelBox.class, "String arg");

        /* test db connection using hardcoded login
        MongoDriver mongoDriver = new MongoDriver();
        mongoDriver.connect_database();
        */
    }
}
