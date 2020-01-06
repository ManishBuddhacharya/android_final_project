package com.onlinevotingsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import API.API;
import adapter.VoteAdapter;
import model.Position;
import model.Vote;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class VotingActivity extends AppCompatActivity {
    private RecyclerView rvVote;
    private TextView tvHeading;
    int pos_id, voter_id;
    String pos_name;

    SensorManager sensorManager;
    Sensor sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voting);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            pos_id= bundle.getInt("pos_id");
            pos_name = bundle.getString("pos_name");
        }

        SharedPreferences sharedPreference = getSharedPreferences("token", Context.MODE_PRIVATE);
        int voter_id = sharedPreference.getInt("id", 0);
        String token = sharedPreference.getString("token", "");

        rvVote = findViewById(R.id.rvVote);
        tvHeading = findViewById(R.id.tvHeading);
        tvHeading.setText("Voting For "+pos_name+" Position");


        API Api = Reusable.getInstance().create(API.class);
        Call<List<Vote>> listCall1 = Api.fetchPositionCandidatesAndroid(token, pos_id, voter_id);

        listCall1.enqueue(new Callback<List<Vote>>() {
            @Override
            public void onResponse(Call<List<Vote>> call, Response<List<Vote>> response) {
                generateList(response.body());
            }

            @Override
            public void onFailure(Call<List<Vote>> call, Throwable t) {
                Toast.makeText(VotingActivity.this, "No NO " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateList(List<Vote> body) {
        List<Vote> itemList = body;
        List<Vote> candidateList = new ArrayList<>();
        for (Vote item : itemList) {
            candidateList.add(new Vote(item.getPosition_name(), item.getUser_name(), item.getPath(), item.getId(), item.getPosition_id()));
        }
        VoteAdapter voteAdapter = new VoteAdapter(this, candidateList);
        rvVote.setAdapter(voteAdapter);
        rvVote.setLayoutManager(new GridLayoutManager(this, 1));
    }

    public void onResume() {
        super.onResume();
        sensorManager.registerListener(proxiListener, sensor,SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void onStop() {
        super.onStop();
        sensorManager.unregisterListener(proxiListener);
    }

    public SensorEventListener proxiListener = new SensorEventListener() {
        public void onAccuracyChanged(Sensor sensor, int acc) { }

        public void onSensorChanged(SensorEvent event) {
            float proximity = event.values[0];
            if (proximity <6){
                userTooClose();
            }
        }
    };

    public AlertDialog myDialog;

    public void userTooClose() {
        if( myDialog != null && myDialog.isShowing() ) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User too close");
        builder.setMessage("Please maintain distance from phone for healthier eye.");
        builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.dismiss();
            }});
        builder.setCancelable(false);
        myDialog = builder.create();
        myDialog.show();
    }

}
