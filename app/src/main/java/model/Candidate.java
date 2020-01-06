package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Candidate {
    private int  user_id, position_id, user, position;
    private String pos_name, path;

    private String user_name;

    private int id;

    public Candidate(String pos_name, String user_name, String path, int id) {
        this.pos_name = pos_name;
        this.user_name = user_name;
        this.path = path;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPos_name() {
        return pos_name;
    }

    public String getUser_name() {
        return user_name;
    }

    public Candidate(int user, int position) {
        this.user = user;
        this.position = position;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }


    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getPosition_id() {
        return position_id;
    }

    public void setPosition_id(int position_id) {
        this.position_id = position_id;
    }
}
