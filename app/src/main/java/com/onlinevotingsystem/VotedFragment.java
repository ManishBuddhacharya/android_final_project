package com.onlinevotingsystem;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import API.API;
import adapter.VoteAdapter;
import model.Vote;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;


/**
 * A simple {@link Fragment} subclass.
 */
public class VotedFragment extends Fragment {
    private RecyclerView rvVote;
    int pos_id;

    public VotedFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_voted, container, false);
        rvVote = view.findViewById(R.id.rvVoted);

        if (getArguments() != null) {
            pos_id = Integer.parseInt(getArguments().getString("pos_id"));
        }

        SharedPreferences sharedPreference = this.getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        String token = sharedPreference.getString("token","");

        API Api = Reusable.getInstance().create(API.class);
        Call<List<Vote>> listCall1 = Api.fetchPositionCandidatesAndroid(token, 1, 2);

        listCall1.enqueue(new Callback<List<Vote>>() {
            @Override
            public void onResponse(Call<List<Vote>> call, Response<List<Vote>> response) {
                generateList(response.body());
                Toast.makeText(getActivity(), "Yosh Yosh ",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<Vote>> call, Throwable t) {
                Toast.makeText(getActivity(), "No NO "+t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void generateList(List<Vote> body) {
        List<Vote> itemList = body;
        List<Vote> candidateList = new ArrayList<>();
        for (Vote item: itemList){
            candidateList.add(new Vote(item.getPosition_name(), item.getUser_name(), item.getPath(), item.getId(), item.getId()));
        }
        VoteAdapter voteAdapter = new VoteAdapter(getActivity(), candidateList);
        rvVote.setAdapter(voteAdapter);
        rvVote.setLayoutManager(new GridLayoutManager(getActivity(), 1));
    }

}
