/*
    Date: 2/3/2021
    Class: CS370 Software Engineering
    Name: Adis Delanovic
    Description: Prompts for username and password and creates a connection to a MongoDB Atlas Database. Things to change
        can include using the 'Password' class instead.
 */


import com.mongodb.*;
import org.bson.Document;

import java.util.ArrayList;
import java.util.logging.*;
import java.util.Scanner;

public class MongoDriver {
    
        public static MongoClient mongoClient;
        public static MongoDatabase database;

   public static void main(String[] args) {


        String username;
        String password;

        System.out.println("Enter username: ");
        Scanner input = new Scanner(System.in);
        username = input.nextLine();

        System.out.println("Enter Password: ");
        password = input.nextLine();

        //Creates the connection to the Cluster 'Modexlbox' (typo) and the 'TestDB' Collection.
        mongoClient = new MongoClient((new MongoClientURI("mongodb+srv://" + username + ":" + password + "@modexlbox.hjap7.mongodb.net/TestDB?retryWrites=true&w=majority")));
        database = mongoClient.getDatabase("TestDB");


        //Turns off console logger for MongoDB
        Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
        mongoLogger.setLevel(Level.SEVERE);

        //Displays all items in the collection
        MongoCollection<Document> collection = database.getCollection("Directions");
        MongoCursor<Document> doc = collection.find().iterator();
        while(doc.hasNext()){
            ArrayList<Object> objects = new ArrayList<>(doc.next().values());
            for (Object object : objects) {
                System.out.println(object);
            }
        }


    }
}
