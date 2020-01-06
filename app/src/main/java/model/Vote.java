package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Vote {
    private String pos_name;
    private String user_name, path;

    private int id;
    private int position_id, candidate_id, voter_id;

    @SerializedName("user_id")
    @Expose
    private int user_id;

    public Vote(String pos_name, String user_name) {
        this.pos_name = pos_name;
        this.user_name = user_name;
    }

    public Vote(String pos_name, String user_name, String path, int id, int position_id) {
        this.pos_name = pos_name;
        this.user_name = user_name;
        this.path = path;
        this.id = id;
        this.position_id = position_id;
    }

    public Vote(int position_id, int candidate_id, int voter_id) {
        this.position_id = position_id;
        this.candidate_id = candidate_id;
        this.voter_id = voter_id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPosition_name() {
        return pos_name;
    }

    public void setPosition_name(String pos_name) {
        this.pos_name = pos_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPosition_id() {
        return position_id;
    }

    public void setPosition_id(int position_id) {
        this.position_id = position_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
