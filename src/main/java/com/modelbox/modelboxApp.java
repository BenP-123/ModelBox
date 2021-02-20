package com.modelbox;

import com.modelbox.auth.dbConnection;
import javafx.application.Application;

public class modelboxApp {

    public static void main(String[] args) {
        Application.launch(initialStagePrep.class, "String arg");

        // Test db connection using hardcoded login
        dbConnection dbConnection = new dbConnection();
        dbConnection.connect_database();

    }
}
