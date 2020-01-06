package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginSignupResponse {

    private boolean success;

    private String accessToken, usertype;

    private int id;

    public int getId() {
        return id;
    }

    public boolean getSuccess() {
        return success;
    }

    public String getToken() {
        return accessToken;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
