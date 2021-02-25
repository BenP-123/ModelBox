package com.modelbox.auth;

import static com.modelbox.auth.logIn.mongoClient;

public final class logOut {

    /**
     * Using the MongoDB driver, log the user out of the application's database
     *
     * @return       0 on success, -1 on error
     */
    public static int logUserOut(){
        try {
            mongoClient.close();
            System.out.println("Successfully logged out.");
            return 0;
        }
        catch (Exception e) {
            // Handle exception
            return -1;
        }
    }
}
