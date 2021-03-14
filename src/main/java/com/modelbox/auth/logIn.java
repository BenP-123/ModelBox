package com.modelbox.auth;

import com.modelbox.databaseIO.usersIO;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import com.mongodb.reactivestreams.client.MongoDatabase;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;

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
            Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
            mongoLogger.setLevel(Level.SEVERE);

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

            initializeNewAccount();

            return 0;
        } catch (Exception exception) {
            // Handle errors
            exception.printStackTrace();
            return -1;
        }
    }

    //********************************************** VERIFICATION/CHECK METHODS **************************************//

    /**
     * Verifies that all the login form checks have succeeded
     *
     * @return true on success, false on error
     */
    public boolean didVerificationsPass() {
        if (!areRequiredFieldsMet()) {
            loginErrorMessage = "Required fields must be provided! Please fill in the required fields.";
            return false;
        }
        else if(!isEmailInTheDatabase()){
            loginErrorMessage = "The provided email cannot be found.";
            return false;
        }
        else {
            return true;
        }
    }

    /**
     * Verifies that all the fields on the login view have been provided
     *
     * @return true on success, false on error
     */
    public boolean areRequiredFieldsMet() {
        return !(emailAddress.equals("")) && !(password.equals(""));
    }

    /**
     * Verifies that the email provided is a user in the database
     *
     * @return true on success, false on error
     */
    public boolean isEmailInTheDatabase() {
        // Need to implement

        return true;
    }

    //************************************************** GETTER METHODS **********************************************//

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
     * @return a string containing the email address of the user
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

    //************************************************** SETTER METHODS **********************************************//

    /**
     * Sets the email address for the current app user
     *
     * @param email a String containing the email address to use for the current user
     */
    public void setEmailAddress(String email) {
        emailAddress = email;
    }

    /**
     * Sets the UI-entered password for the current app user
     *
     * @param pass a String containing the password to use for the current user
     */
    public void setPassword(String pass) {
        password = pass;
    }

    /**
     * Sets the MongoClient for the current MongoDB connection
     *
     * @param client a MongoClient object used in further database I/O
     */
    public void setMongoClient(MongoClient client) {
        mongoClient = client;
    }

    /**
     * Sets the MongoDatabase for further app database uses
     *
     * @param database a MongoDatabase object used in further database I/O
     */
    public void setMongoDatabase(MongoDatabase database) {
        mongoDatabase = database;
    }

    //************************************************** UTILITY METHODS *********************************************//

    /**
     * URL encodes the value of the provided string. This is used to escape characters in the email address
     * of the user for proper use in the MongoDB connection string.
     *
     * @param value a String that does not have its characters properly escaped for use in a URL
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

    /**
     * If this is the user's first login, update their custom_data object in the database with the values they
     * specified when they created their account.
     *
     */
    private void initializeNewAccount(){
        Preferences prefs = Preferences.userRoot().node("/com/modelbox");
        String displayName = prefs.get("displayName", "null");
        String firstName = prefs.get("firstName", "null");
        String lastName = prefs.get("lastName", "null");

        if (!displayName.equals("null") && !firstName.equals("null") && !lastName.equals("null") && (mongoDatabase != null)) {
            usersIO.setInitialCustomData(displayName, firstName, lastName);
            prefs.remove("displayName");
            prefs.remove("firstName");
            prefs.remove("lastName");
        }
    }
}
