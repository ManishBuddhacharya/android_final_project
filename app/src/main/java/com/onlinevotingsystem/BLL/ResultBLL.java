package com.onlinevotingsystem.BLL;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import API.API;
import model.LoginSignupResponse;
import model.Vote;
import model.VoteRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class ResultBLL {
    Context context;
    private boolean isSuccess = false;

    public ResultBLL(Context context) {
        this.context = context;
    }

    public boolean fetchResult(int pos_id){

        SharedPreferences sharedPreference = context.getSharedPreferences("token", context.MODE_PRIVATE);
        String token = sharedPreference.getString("token","");

        API Api = Reusable.getInstance().create(API.class);
        Call<List<VoteRes>> listCall = Api.fetchResult(token, pos_id);

        listCall.enqueue(new Callback<List<VoteRes>>() {
            @Override
            public void onResponse(Call<List<VoteRes>> call, Response<List<VoteRes>> response) {
                if(response.body().size() <= 0){
                    isSuccess = false;
                }
                else{
                    isSuccess = true;
                }
            }

            @Override
            public void onFailure(Call<List<VoteRes>> call, Throwable t) {
                Toast.makeText(context, "Error : "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
        return isSuccess;
    }
}
