package com.onlinevotingsystem.BLL;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import API.API;
import model.Candidate;
import model.LoginSignupResponse;
import retrofit2.Call;
import retrofit2.Response;
import reusable.Reusable;

public class CandidateBLL {
    private int member_id, posiition_id;
    private boolean isSuccess = false;

    public CandidateBLL( int member_id, int posiition_id) {
        this.member_id = member_id;
        this.posiition_id = posiition_id;
    }

    public CandidateBLL() {

    }

    public boolean saveCandidate(String token){
        Candidate candidate = new Candidate(member_id, posiition_id);
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> candidateCall = api.insertCandidate(token, candidate);

        try {
            Response<LoginSignupResponse> userCall = candidateCall.execute();
            if (userCall.body().getSuccess()){
                isSuccess = true;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean updateCandidate(String token, int id){
        Candidate candidate = new Candidate(member_id, posiition_id);
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> candidateCall = api.updateCandidate(token, id, candidate);

        try {
            Response<LoginSignupResponse> userCall = candidateCall.execute();
            if (userCall.body().getSuccess()){
                isSuccess = true;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean deleteCandidate(String token, int id){
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> candidateCall = api.deleteCandidate(token, id);

        try {
            Response<LoginSignupResponse> userCall = candidateCall.execute();
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
