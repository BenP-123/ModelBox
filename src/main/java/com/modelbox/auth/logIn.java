package com.modelbox.auth;

import com.modelbox.databaseIO.usersIO;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.*;
import org.bson.Document;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class logIn {

    private String emailAddress;
    private String password;
    public static MongoClient mongoClient;
    public static MongoDatabase database;

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

            mongoClient = MongoClients.create(clientSettings);

            // Access the application database
            database = mongoClient.getDatabase("modelboxApp");

            // Retrieving the documents
            FindIterable<Document> iterDoc = usersIO.usersCollection.find();
            for (Document document : iterDoc) {
                System.out.println(document);
            }

            System.out.println("Successfully logged in.");

            return 0;
        } catch (Exception e) {
            System.out.println(e);
            System.out.println("Error!");
            return -1;
        }
    }

    /**
     * Verifies that all the fields on the login form have been provided
     *
     * @return       0 on success, -1 on error
     */
    private int areRequiredFieldsMet() {
        // Need to implement

        return 0;
    }

    /**
     * Retrieve the email address provided by the user
     *
     * @return       a string containing the email address
     */
    public String getEmailAddress()
    {
        return this.emailAddress;
    }

    /**
     * Retrieve the password provided by the user
     *
     * @return       a string containing the provided password
     */
    public String getPassword()
    {
        return this.password;
    }

    /**
     * Set the email provided equal to the email address used to authenticate
     *
     * @param  emailAddress
     * @return              void
     */
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }

    /**
     * Set the password provided equal to the password used to authenticate
     *
     * @param  password
     * @return          void
     */
    public void setPassword(String password) { this.password = password; }


    /**
     * Encode the provided value in order to handle characters such as the @ symbol
     * i.e: The function encodes the email address provided by the user (user@example.com)
     *
     * @return       a string containing the encoded value
     */
    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public void clearLogin(){
        this.password = "";
        this.emailAddress = "";
    }
}
