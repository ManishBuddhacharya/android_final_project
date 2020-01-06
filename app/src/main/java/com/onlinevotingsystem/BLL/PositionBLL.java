package com.onlinevotingsystem.BLL;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import API.API;
import model.LoginSignupResponse;
import model.Position;
import model.User;
import retrofit2.Call;
import retrofit2.Response;
import reusable.Reusable;

public class PositionBLL {
    private String name;
    Context context;
    private boolean isSuccess = false;

    public PositionBLL() {
    }

    public PositionBLL(String name) {
        this.name = name;
    }

    public boolean addPosition(String token){
        Position position = new Position(name);
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> positionCall = api.insertPosition(token, position);

        try {
            Response<LoginSignupResponse> userCall = positionCall.execute();
            if (userCall.body().getSuccess()){
                isSuccess = true;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean updatePosition(String token, int id){
        Position position = new Position(name);
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> positionCall = api.updatePosition(token, id, position);

        try {
            Response<LoginSignupResponse> userCall = positionCall.execute();
            if (userCall.body().getSuccess()){
                isSuccess = true;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean deletePosition(String token, int id){
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> positionCall = api.deletePosition(token, id);

        try {
            Response<LoginSignupResponse> userCall = positionCall.execute();
            if (userCall.body().getSuccess()){
                isSuccess = true;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return isSuccess;
    }
}
