package com.modelbox.auth;

import com.mongodb.ConnectionString;
import com.mongodb.client.*;
import com.mongodb.MongoClientSettings;
import org.bson.Document;
import com.mongodb.client.MongoCollection;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;


public class dbConnection
{
    private String username;
    private String password;
    private String db_name;
    public static MongoClient mongo_client;
    public static MongoDatabase database;

/*
    Name: Adis Delanovic
    Date: 02/14/2021
    Last Revision: 02/14/2021
    Revision Number: 1
    Description: Successfully create a connection to MongoDB Atlas
*/
    public void connect_database()
    {
        //Sets a level to the JULLogger, lots of visible text in red on console.
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);

        try
        {

            String username = "ado@ado.com";
            String encodeUsername = encodeValue(username);

            //TestUser is hardcoded logging on for testing purposes.
            //Permissions are read only collections in TestDB
            ConnectionString conn_string = new ConnectionString(
                    "mongodb://" + encodeUsername + ":mongodb2021@realm.mongodb.com:27020/?authMechanism=PLAIN&authSource=%24external&ssl=true&appName=modelbox-vqzyc:Model-Box:local-userpass");


            //Set the settings for the connection
            MongoClientSettings settings = MongoClientSettings.builder()
                    .applyConnectionString(conn_string)
                    .retryWrites(true)
                    .build();

            //Create the connection
            mongo_client = MongoClients.create(settings);

            try
            {
                //Access particular database
                database = mongo_client.getDatabase("TestDB");
                MongoCollection<Document> collection = database.getCollection("TestDB");

                //Retrieving the documents
                System.out.println("Displaying entire collection in 'TestDB'");
                FindIterable<Document> iterDoc = collection.find();
                for (Document document : iterDoc) {
                    System.out.println("1");
                    System.out.println(document);
                }
            }
            catch(IllegalArgumentException e)
            {
                System.out.println("Not a proper Database!");
                e.printStackTrace();
            }

        }
        catch(Exception e)
        {
            System.out.println("Did not connect to Database properly");
            e.printStackTrace();
        }

    }

/*
    Name: Adis Delanovic
    Date: 02/14/2021
    Last Revision: 02/14/2021
    Revision Number: 1
    Description: Terminate a connection to MongoDB
*/

    //Uses URLEncoder for email and password login.
    private static String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    public static void disconnect_database()
    {
        try
        {
            mongo_client.close();
            System.out.println("Connection to MongoDB Terminated");

        }
        catch(Exception e)
        {
            System.out.println("Unable to disconnect from Database!");
        }
    }

    //Getter and setter functions
    public void set_password(String password)
    {
        this.password = password;
    }

    public void set_dbname(String db_name)
    {
        this.db_name = db_name;
    }

    public void set_username(String username)
    {
        this.username = username;
    }

    public String get_dbname()
    {
        return this.db_name;
    }

    public String get_password()
    {
        return this.password;
    }

    public String get_username()
    {
        return this.username;
    }
}
