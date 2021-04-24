package com.modelbox.mongo;

/**
 * Provides a connection mechanism to handle error-related callbacks from Javascript calls made in a JavaFX WebEngine
 * @see com.modelbox.app#mongoApp
 */
public class errorBridge {

    /**
     * Prints out an error message received from the WebEngine environment
     * @param error a String containing an error message
     */
    public void handleError(String error) {
        System.out.println(error);
    }
}
