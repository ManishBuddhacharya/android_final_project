package com.onlinevotingsystem;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import API.API;
import model.Position;
import model.Vote;
import okhttp3.Headers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class VoteActivity extends AppCompatActivity {

    private Spinner positionSpinner;

    List<Position> itemList;
    HashMap<Integer, String> spinnerMap = new HashMap<Integer, String>();
    ArrayList<String> Position_name = new ArrayList<String>();
    ArrayList<Integer> Position_id = new ArrayList<Integer>();

    int pos_id;
    String pos_name;

    SensorManager sensorManager;
    Sensor sensor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        positionSpinner = findViewById(R.id.spinnerPosition);

        API Api = Reusable.getInstance().create(API.class);
        Call<List<Position>> listCall = Api.fetchPositions();
        listCall.enqueue(new Callback<List<Position>>() {
            @Override
            public void onResponse(Call<List<Position>> call, Response<List<Position>> response) {
                attachPosition(response.body());
            }

            @Override
            public void onFailure(Call<List<Position>> call, Throwable t) {
                Toast.makeText(VoteActivity.this, "Error : " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }

    private void attachPosition(List<Position> body) {

        itemList = body;
        for (Position item : itemList) {
            Position_name.add(item.getName());
            Position_id.add(item.getId());
        }

        String[] spinnerArray = new String[itemList.size()];

        for (int i = 0; i < Position_id.size(); i++) {
            spinnerMap.put(i, Position_id.get(i)+"");
            spinnerArray[i] = Position_name.get(i);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        positionSpinner.setAdapter(adapter);

        positionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pos_name = positionSpinner.getSelectedItem().toString();
                String position_id = spinnerMap.get(positionSpinner.getSelectedItemPosition());
                pos_id = Integer.parseInt(position_id);

                SharedPreferences sharedPreference = getSharedPreferences("token", Context.MODE_PRIVATE);
                String token = sharedPreference.getString("token", "");
                int voter_id = sharedPreference.getInt("id", 0);

                API Api = Reusable.getInstance().create(API.class);
                Call<List<Vote>> listCall1 = Api.fetchPositionCandidatesAndroid(token, pos_id, voter_id);

                listCall1.enqueue(new Callback<List<Vote>>() {
                    @Override
                    public void onResponse(Call<List<Vote>> call, Response<List<Vote>> response) {
                        Headers headers = response.headers();
                        // get header value
                        String voted = response.headers().get("voted");
                        if(response.body().size() <= 0){
                            Toast.makeText(VoteActivity.this, "NO Candidates In "+pos_name+" position" , Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (voted.equals("true")) {
                                Toast.makeText(VoteActivity.this, "Already Voted for " + pos_name + " position", Toast.LENGTH_SHORT).show();
                            } else {
                                Intent intent = new Intent(VoteActivity.this, VotingActivity.class);
                                intent.putExtra("pos_id", pos_id);
                                intent.putExtra("pos_name", pos_name);
                                startActivity(intent);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Vote>> call, Throwable t) {
                        Toast.makeText(VoteActivity.this, "No NO " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
