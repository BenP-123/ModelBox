package com.modelbox.databaseIO;

import java.util.Date;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

//Class that defines how a model is represented in our database and outside of it.
public class model extends user {

    @SerializedName("model_name")
    @Expose
    private String model_name;

    @SerializedName("model_type")
    @Expose
    private String model_type;

    @SerializedName("date_created")
    @Expose
    private Date date_created;

    @SerializedName("last_update")
    @Expose
    private Date last_update;

    @SerializedName("model_size")
    @Expose
    private Double model_size;

    @SerializedName("model_owner")
    @Expose
    private user model_owner;

    @SerializedName("collaborators")
    @Expose
    private List<user> collaborators;

    @SerializedName("rev_number")
    @Expose
    private Integer rev_number;

    @SerializedName("is_complete")
    @Expose
    private Boolean is_complete;

    @SerializedName("model_tags")
    @Expose
    private String model_tags;
}
