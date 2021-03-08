package com.modelbox.auth;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class logIn {

    private String emailAddress;
    private String password;
    private String loginErrorMessage;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    /**
     * Logs the user into the MongoDB database for the app
     *
     * @return 0 on success, -1 on error
     */
    public int logUserIn() {
        try {
            // Sets a level to the JULLogger to eliminate lots of visible red text in the console.
            java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

            ConnectionString connectString = new ConnectionString(
                    "mongodb://" + encodeValue(getEmailAddress()) + ":" + getPassword() + "@realm.mongodb.com:27020/?authMechanism=PLAIN&authSource=%24external&ssl=true&appName=modelbox-vqzyc:Model-Box:local-userpass");

            // Set the settings for the connection
            MongoClientSettings clientSettings = MongoClientSettings.builder()
                    .applyConnectionString(connectString)
                    .retryWrites(true)
                    .retryReads(true)
                    .build();

            // Create the connection
            this.setMongoClient(MongoClients.create(clientSettings));

            // Access the application database
            this.setMongoDatabase(mongoClient.getDatabase("modelboxApp"));

            return 0;
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
            return -1;
        }
    }

    /*********************************************** VERIFICATION/CHECK METHODS ***************************************/

    /**
     * Verifies that all the login form checks have succeeded
     *
     * @return true on success, false on error
     */
    public boolean didVerificationsPass() {
        if (areRequiredFieldsMet()) {
            return true;
        } else {
            loginErrorMessage = "Required fields must be provided! Please fill in the required fields.";
            return false;
        }
    }

    /**
     * Verifies that all the fields on the login view have been provided
     *
     * @return true on success, false on error
     */
    public boolean areRequiredFieldsMet() {
        if(!(emailAddress.equals("")) && !(password.equals(""))) {
            return true;
        } else{
            return false;
        }
    }

    /*************************************************** GETTER METHODS ***********************************************/

    /**
     * Gets the login error of the current app user
     *
     * @return a string containing the login error message that should be shown to the user
     */
    public String getLoginErrorMessage() {
        return loginErrorMessage;
    }

    /**
     * Gets the email address of the logged in user
     *
     * @return a string containing the email address
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Gets the password of the logged in user
     *
     * @return a string containing the UI-entered password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Gets the MongoDB connection client
     *
     * @return a MongoClient object for database uses
     */
    public MongoClient getMongoClient() {
        return mongoClient;
    }

    /**
     * Gets the MongoDB database for the application
     *
     * @return a MongoDatabase object for database uses
     */
    public MongoDatabase getMongoDatabase() {
        return mongoDatabase;
    }

    /*************************************************** SETTER METHODS ***********************************************/

    /**
     * Sets the email address for the current app user
     *
     * @return void
     */
    public void setEmailAddress(String email) {
        emailAddress = email;
    }

    /**
     * Sets the UI-entered password for the current app user
     *
     * @return void
     */
    public void setPassword(String pass) {
        password = pass;
    }

    /**
     * Sets the MongoClient for the current MongoDB connection
     *
     * @return void
     */
    public void setMongoClient(MongoClient client) {
        mongoClient = client;
    }

    /**
     * Sets the MongoDatabase for further app database uses
     *
     * @return void
     */
    public void setMongoDatabase(MongoDatabase database) {
        mongoDatabase = database;
    }

    /*************************************************** UTILITY METHODS **********************************************/

    /**
     * Method URL encodes the value of the provided string. This is used to escape characters in the email address
     * of the user for proper use in the MongoDB connection string.
     *
     * @return a String containing the encoded value if successful, or just the original String if unsuccessful
     */
    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
        }
        return value;
    }
}
