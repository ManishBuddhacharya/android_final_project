package com.onlinevotingsystem;

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
import android.view.View;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import API.API;
import adapter.CandidateAdapter;
import adapter.VoteResultAdapter;
import model.Candidate;
import model.Position;
import model.VoteRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class VoteResultActivity extends AppCompatActivity {
    private RecyclerView rvVotes;
    int pos_id;

    SensorManager sensorManager;
    Sensor sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_result);

        rvVotes = findViewById(R.id.rvResult);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            pos_id= bundle.getInt("pos_id");
        }
        SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
        String token = sharedPreference.getString("token","");

        API Api = Reusable.getInstance().create(API.class);
        Call<List<VoteRes>> listCall = Api.fetchResult(token, pos_id);

        listCall.enqueue(new Callback<List<VoteRes>>() {
            @Override
            public void onResponse(Call<List<VoteRes>> call, Response<List<VoteRes>> response) {
                if (response.body() != null){
                    generateList(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<VoteRes>> call, Throwable t) {
                Toast.makeText(VoteResultActivity.this, "Error : "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
    private void generateList(List<VoteRes> body) {
        List<VoteRes> itemList = body;
        List<VoteRes> candidateList = new ArrayList<>();
        for (VoteRes item: itemList){
            String name = item.getPos_name();
            candidateList.add(new VoteRes(item.getY(), item.getLabel(), item.getPath()));
        }
        VoteResultAdapter voteResAdapter = new VoteResultAdapter(this, candidateList);
        rvVotes.setAdapter(voteResAdapter);
        rvVotes.setLayoutManager(new GridLayoutManager(this, 1));
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
