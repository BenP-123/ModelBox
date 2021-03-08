package com.modelbox.auth;

import com.modelbox.controllers.loginController;

public class logOut {

    /**
     * Logs the user out of the MongoDB database for the app
     *
     * @return 0 on success, -1 on error
     */
    public int logUserOut() {
        try {
            loginController.activeLogin.getMongoClient().close();
            return 0;
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
            return -1;
        }
    }
}
