package com.modelbox.auth;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.MongoClient;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class logIn {

    private String emailAddress;
    private String password;
    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    /**
     * Using the MongoDB driver, log the user into the application's database
     *
     * @return       0 on success, -1 on error
     */
    public int logUserIn(){
        try {
            // Sets a level to the JULLogger, lots of visible text in red on console.
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
        } catch (Exception e) {
            // Handle errors
            System.err.println(e);
            return -1;
        }
    }

    /**
     * Verifies that all the fields on the login form have been provided
     *
     * @return       true on success, false on error
     */
    public boolean areRequiredFieldsMet()
    {
        if(!(emailAddress.equals("")) && !(password.equals(""))){
            // Required fields met
            return true;
        } else{
            // Missing values for proper log in
            return false;
        }

    }

    public String getEmailAddress()
    {
        return emailAddress;
    }
    public String getPassword()
    {
        return password;
    }
    public MongoClient getMongoClient()
    {
        return mongoClient;
    }
    public MongoDatabase getMongoDatabase() { return mongoDatabase; }


    public void setEmailAddress(String email) { emailAddress = email; }
    public void setPassword(String pass) { password = pass; }
    public void setMongoClient(MongoClient client) { mongoClient = client; }
    public void setMongoDatabase(MongoDatabase database) { mongoDatabase = database; }

    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
