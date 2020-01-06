package com.onlinevotingsystem.BLL;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.IOException;

import API.API;
import model.LoginSignupResponse;
import model.Position;
import model.Vote;
import retrofit2.Call;
import retrofit2.Response;
import reusable.Reusable;

public class VoteBLL {
    Context context;
    private int candidate_id, pos_id, voter_id;
    private boolean isSuccess = false;

    public VoteBLL() {

    }

    public VoteBLL(int pos_id, int candidate_id, int voter_id) {
        this.pos_id = pos_id;
        this.candidate_id = candidate_id;
        this.voter_id = voter_id;
    }

    public boolean castVote(String token, int candidateID){
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> voteCall = api.castVote(token, candidateID);

        try {
            Response<LoginSignupResponse> userCall = voteCall.execute();
            if (userCall.body().getSuccess()){
                isSuccess = true;
            }

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return isSuccess;
    }

    public boolean castVoteHistory(String token){
        Vote vote= new Vote(pos_id, candidate_id, voter_id);
        API api = Reusable.getInstance().create(API.class);
        Call<LoginSignupResponse> voteCall = api.castVoteHistory(token, vote);

        try {
            Response<LoginSignupResponse> userCall = voteCall.execute();
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
