package com.modelbox.auth;

import com.modelbox.controllers.loginController;

public class logOut {

    /**
     * Using the MongoDB driver, log the user out of the application's database
     *
     * @return       0 on success, -1 on error
     */
    public int logUserOut(){
        try {
            loginController.activeLogin.getMongoClient().close();
            return 0;
        }
        catch (Exception e) {
            // Handle errors
            return -1;
        }
    }
}
