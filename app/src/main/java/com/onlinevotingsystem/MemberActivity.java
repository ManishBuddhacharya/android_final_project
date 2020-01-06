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
import adapter.MemberAdapter;
import model.Position;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class MemberActivity extends AppCompatActivity {
    private RecyclerView rvMember;
    private Button bthInsert;

    SensorManager sensorManager;
    Sensor sensor;


    List<User> userList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
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
                Intent intent = new Intent(MemberActivity.this, AddMemberActivity.class);
                startActivity(intent);
            }
        });

        rvMember= findViewById(R.id.rvMembers);

        API Api = Reusable.getInstance().create(API.class);

        Call<List<User>> listCall = Api.fetchUsers();

        listCall.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {

                generateList(response.body());

            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(MemberActivity.this, "Error : "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void generateList(List<User> body) {
        List<User> itemList = body;
        List<User> contactList = new ArrayList<>();

        for (User item: itemList){
            contactList.add(new User(item.getName(),item.getUsername(),item.getEmail(),item.getAddress(),item.getContact(), item.getPath(), item.getId()));
        }
        MemberAdapter memberAdapter = new MemberAdapter(this, contactList);
        rvMember.setAdapter(memberAdapter);
        rvMember.setLayoutManager(new GridLayoutManager(this, 1));
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
