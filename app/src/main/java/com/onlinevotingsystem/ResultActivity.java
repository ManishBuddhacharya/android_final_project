package com.onlinevotingsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import API.API;
import adapter.PositionAdapter;
import model.Candidate;
import model.Position;
import model.VoteRes;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class ResultActivity extends AppCompatActivity {
    private ListView listPosition;
    private Map<String, String> positionName;
    String pos_id;

    SensorManager sensorManager;
    Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        listPosition = findViewById(R.id.listPosition);

        API Api = Reusable.getInstance().create(API.class);
        Call<List<Position>> listCall = Api.fetchPositions();
        listCall.enqueue(new Callback<List<Position>>() {
            @Override
            public void onResponse(Call<List<Position>> call, Response<List<Position>> response) {
                generateList(response.body());
            }

            @Override
            public void onFailure(Call<List<Position>> call, Throwable t) {
                Toast.makeText(ResultActivity.this, "Error : "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void generateList(List<Position> body) {
        positionName = new HashMap<>();
        List<Position> itemList = body;
        for (Position item: itemList){
            positionName.put(item.getName(),item.getId()+"");
        }

        ArrayAdapter arrayAdapter = new ArrayAdapter<>(
                this,R.layout.list_white_text,
                new ArrayList<String>(positionName.keySet())
        );

        listPosition.setAdapter(arrayAdapter);

        listPosition.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = parent.getItemAtPosition(position).toString();
                pos_id = positionName.get(key);

                SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
                String token = sharedPreference.getString("token","");

                API Api = Reusable.getInstance().create(API.class);
                Call<List<VoteRes>> listCall = Api.fetchResult(token, Integer.parseInt(pos_id));

                listCall.enqueue(new Callback<List<VoteRes>>() {
                    @Override
                    public void onResponse(Call<List<VoteRes>> call, Response<List<VoteRes>> response) {
                        if(response.body().size() <= 0){
                            Toast.makeText(ResultActivity.this, "No Candidates and Result in this position ", Toast.LENGTH_LONG).show();

                        }
                        else{
                            Intent intent = new Intent(ResultActivity.this, VoteResultActivity.class);
                            intent.putExtra("pos_id", Integer.parseInt(pos_id));
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onFailure(Call<List<VoteRes>> call, Throwable t) {
                        Toast.makeText(ResultActivity.this, "Error : "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                    }
                });



            }
        });
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
