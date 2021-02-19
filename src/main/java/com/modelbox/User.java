package com.modelbox;

import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mongodb.gridfs.GridFS;

//Class that defines how a User is represented in our Database and outside of it.
public class User {

    //By using SerializedName i can change the name in my variables.
    //Expose allows me to 'select' which properties of the JSON object we want.
    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("user_id")
    @Expose
    private Integer user_id;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("e_mail")
    @Expose
    private String e_mail;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("birth_date")
    @Expose
    private Date birth_date;

    @SerializedName("reg_date")
    @Expose
    private Date reg_date;

    @SerializedName("last_online")
    @Expose
    private Date last_online;

    @SerializedName("my_models")
    @Expose
    private List<Model> my_models;

    @SerializedName("collabs")
    @Expose
    private List<Model> collabs;

    @SerializedName("shared_models")
    @Expose
    private List<Model> shared_models;

    @SerializedName("profile_picture")
    @Expose
    private String profile_picture;

    @SerializedName("user_bio")
    @Expose
    private String user_bio;

    @SerializedName("user_tags")
    @Expose
    private List<String> user_tags;

    //To upload models need GridFS/GridFSBucket
    //Separates files into chunks and stores them as bytes. Max file size is 16MB
    //https://mongodb.github.io/mongo-java-driver/3.4/driver/tutorials/gridfs/
    @SerializedName("uploaded_models")
    @Expose
    private List<GridFS> upload_models;
}