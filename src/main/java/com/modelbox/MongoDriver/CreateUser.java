package com.modelbox.MongoDriver;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoCommandException;

import java.util.Collections;
import java.util.Scanner;

/*
    Name: Adis Delanovic
    Date: 02/17/2021
    Last Revision: 02/17/2021
    Revision Number: 1
    Description: Successfully create a new user in a MongoDB with readWrite capabilities
*/

public class CreateUser extends MongoDriver {

    private String first_name;
    private String last_name;
    private String user_name;
    private String e_mail;
    private String date_of_birth;
    private String password;

    public void enter_information(){

        try {
                System.out.flush();
                Scanner input = new Scanner(System.in);

                System.out.println("Enter First Name: ");
                this.first_name = input.next();

                System.out.println("Enter Last Name: ");
                this.last_name = input.next();

                System.out.println("Enter Date of Birth");
                this.date_of_birth = input.next();

                System.out.println("Enter Username: ");
                this.user_name = input.next();

                System.out.println("Enter E-mail: ");
                this.e_mail = input.next();

                System.out.println("Enter Password: ");
                this.password = input.next();

                System.out.flush();

        }
        catch (MongoCommandException e){
            e.printStackTrace();
        }
    }

    public void create_user(){

        try {

            BasicDBObject user_command =
                    new BasicDBObject("createUser", this.getUser_name()).append("pwd", this.getPassword()).append("roles",
                            Collections.singletonList(new BasicDBObject("role", "readWrite").append("db", "ModelBox")));

            database.runCommand(user_command);

        }catch(MongoCommandException e){
            e.printStackTrace();
        }
    }

    public String getFirst_name(){
        return this.first_name;
    }

    public String getLast_name(){
        return this.last_name;
    }

    public String getUser_name(){
        return this.user_name;
    }

    public String getE_mail(){
        return this.e_mail;
    }

    public String getDate_of_birth(){
        return this.date_of_birth;
    }

    public String getPassword(){
        return this.password;
    }
    public void setUser_name(String user_name){
        this.user_name = user_name;
    }

    public void setFirst_name(String first_name){
        this.first_name = first_name;
    }

    public void setLast_name(String last_name){
        this.last_name = last_name;
    }

    public void setDate_of_birth(String date_of_birth){
        this.date_of_birth = date_of_birth;
    }

    public void setE_mail(String e_mail){
        this.e_mail = e_mail;
    }

    public void setPassword(String password){
        this.password = password;
    }

}
