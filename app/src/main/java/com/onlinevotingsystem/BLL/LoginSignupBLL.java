package com.onlinevotingsystem.BLL;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;
import java.util.ArrayList;

import API.API;
import model.LoginSignupResponse;
import model.User;
import retrofit2.Call;
import retrofit2.Response;
import reusable.Reusable;

public class LoginSignupBLL {
    private String password ;
    private String username, name, usertype, email, address, contact;
    private boolean isSuccess = false;

    Context context;
    ArrayList<String> arrlist = new ArrayList<String>(3);

    public LoginSignupBLL(String password, String username) {
        this.password = password;
        this.username = username;
    }


    public LoginSignupBLL( String name, String username, String usertype, String email, String address, String contact, String password) {
        this.name= name;
        this.username = username;
        this.usertype= usertype;
        this.email = email;
        this.address = address;
        this.contact= contact;
        this.password = password;
    }

    public LoginSignupBLL() {
    }

    public LoginSignupBLL(String name, String username, String email, String address, String contact) {
        this.name= name;
        this.username = username;
        this.email = email;
        this.address = address;
        this.contact= contact;
    }

    public ArrayList<String> checkUser(){
        User user = new User(username, password);
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> heroesCall = api.loginUser(user);

        try {
            Response<LoginSignupResponse> userCall = heroesCall.execute();
            if (userCall.body().getSuccess()){

                // use add() method to add elements in the list
                arrlist.add(userCall.body().getToken());
                arrlist.add(userCall.body().getId()+"");
                arrlist.add(userCall.body().getUsertype());

                isSuccess = true;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return arrlist;
    }

    public ArrayList<String> registerUser(){
        User user = new User(name, username, usertype, email, address, contact, password);
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> heroesCall = api.signupUser(user);

        try {
            Response<LoginSignupResponse> userCall = heroesCall.execute();
            if (userCall.body().getSuccess()){
                // use add() method to add elements in the list
                arrlist.add(userCall.body().getToken());
                arrlist.add(userCall.body().getId()+"");
                arrlist.add(userCall.body().getUsertype());

                isSuccess = true;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return arrlist;
    }

    public boolean insertUser(){
        User user = new User(name, username, usertype, email, address, contact, password);
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> heroesCall = api.insertUser(user);
        try {
            Response<LoginSignupResponse> userCall = heroesCall.execute();
            if (userCall.body().getSuccess()){
                isSuccess = true;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean updateUser(String token, int id){
        User user = new User(name, username, email, address, contact);
        API api = Reusable.getInstance().create(API.class);

        Call<LoginSignupResponse> memberCall = api.updateUser(token, id, user);

        try {
            Response<LoginSignupResponse> userCall = memberCall.execute();
            if (userCall.body().getSuccess()){
                isSuccess = true;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean deleteUser (String token, int id){
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> memberCall = api.deleteUser(token, id);

        try {
            Response<LoginSignupResponse> userCall = memberCall.execute();
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
