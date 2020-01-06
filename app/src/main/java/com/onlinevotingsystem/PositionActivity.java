package com.onlinevotingsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import API.API;
import adapter.PositionAdapter;
import model.Position;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class PositionActivity extends AppCompatActivity {
    private RecyclerView rvPositions;
    private Button bthInsert;
    List<Position> positionList = new ArrayList<>();

    SensorManager sensorManager;
    Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                Intent intent = new Intent(PositionActivity.this, AddPositionActivity.class);
                startActivity(intent);
            }
        });

        rvPositions= findViewById(R.id.rvPositions);

        API Api = Reusable.getInstance().create(API.class);

        Call<List<Position>> listCall = Api.fetchPositions();

        listCall.enqueue(new Callback<List<Position>>() {
            @Override
            public void onResponse(Call<List<Position>> call, Response<List<Position>> response) {

                generateList(response.body());

            }

            @Override
            public void onFailure(Call<List<Position>> call, Throwable t) {
                Toast.makeText(PositionActivity.this, "Error : "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateList(List<Position> body) {
        List<Position> itemList = body;
        List<Position> contactList = new ArrayList<>();

        for (Position item: itemList){
            contactList.add(new Position(item.getName(),item.getId()));
        }
        PositionAdapter PositionAdapter = new PositionAdapter(this, contactList);
        rvPositions.setAdapter(PositionAdapter);
        rvPositions.setLayoutManager(new GridLayoutManager(this, 1));
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
