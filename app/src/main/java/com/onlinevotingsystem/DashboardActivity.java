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
import android.text.Layout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import API.API;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class DashboardActivity extends AppCompatActivity {
    private TextView tvPosition, tvMember, tvCandidate, tvResult, tvVote, tvProfile, tvLogout;
    private ImageView imgPosition, imgMember, imgCandidate, imgVote, imgResult, imgProfile, imgLogout;

    SensorManager sensorManager;
    Sensor sensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        tvPosition = findViewById(R.id.tvPosition);
        imgPosition = findViewById(R.id.imgPosition);
        tvMember= findViewById(R.id.tvMember);
        imgMember= findViewById(R.id.imgMember);
        tvCandidate= findViewById(R.id.tvCandidate);
        imgCandidate= findViewById(R.id.imgCandidate);
        tvVote= findViewById(R.id.tvVote);
        imgVote= findViewById(R.id.imgVote);
        tvResult= findViewById(R.id.tvResult);
        imgResult= findViewById(R.id.imgResult);
        tvProfile= findViewById(R.id.tvProfile);
        imgProfile= findViewById(R.id.imgProfile);
        tvLogout= findViewById(R.id.tvLogout);
        imgLogout= findViewById(R.id.imgLogout);

        SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
        final String usertype = sharedPreference.getString("usertype","");

        tvPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usertype.equals("admin")){
                    Intent intent = new Intent(DashboardActivity.this, PositionActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(DashboardActivity.this, PositionForMemberActivity.class);
                    startActivity(intent);
                }
            }
        });

        imgPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usertype.equals("admin")) {
                    Intent intent = new Intent(DashboardActivity.this, PositionActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(DashboardActivity.this, PositionForMemberActivity.class);
                    startActivity(intent);
                }
            }
        });

        tvMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usertype.equals("admin")) {
                    Intent intent = new Intent(DashboardActivity.this, MemberActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(DashboardActivity.this, MemberForMemberActivity.class);
                    startActivity(intent);
                }
            }
        });

        imgMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usertype.equals("admin")) {
                    Intent intent = new Intent(DashboardActivity.this, MemberActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(DashboardActivity.this, MemberForMemberActivity.class);
                    startActivity(intent);
                }
            }
        });

        tvCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usertype.equals("admin")) {
                    Intent intent = new Intent(DashboardActivity.this, CandidateActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(DashboardActivity.this, CandidateForMemberActivity.class);
                    startActivity(intent);
                }
            }
        });

        imgCandidate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usertype.equals("admin")) {
                    Intent intent = new Intent(DashboardActivity.this, CandidateActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(DashboardActivity.this, CandidateForMemberActivity.class);
                    startActivity(intent);
                }
            }
        });

        tvVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, VoteActivity.class);
                startActivity(intent);
            }
        });

        imgVote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, VoteActivity.class);
                startActivity(intent);
            }
        });

        tvResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });

        imgResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DashboardActivity.this, ResultActivity.class);
                startActivity(intent);
            }
        });

        tvProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API Api = Reusable.getInstance().create(API.class);
                SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
                String token = sharedPreference.getString("token","");
                int user_id =sharedPreference.getInt("id",0);
                Call<User> listCall = Api.searchProfile(token, user_id);

                listCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                        intent.putExtra("name", response.body().getName());
                        intent.putExtra("address", response.body().getAddress());
                        intent.putExtra("contact", response.body().getContact());
                        intent.putExtra("username", response.body().getUsername());
                        intent.putExtra("email", response.body().getEmail());
                        intent.putExtra("image", response.body().getPath());
                        intent.putExtra("id", response.body().getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(DashboardActivity.this, "Error ",Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                API Api = Reusable.getInstance().create(API.class);
                SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
                String token = sharedPreference.getString("token","");
                int user_id =sharedPreference.getInt("id",0);
                Call<User> listCall = Api.searchProfile(token, user_id);

                listCall.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                        intent.putExtra("name", response.body().getName());
                        intent.putExtra("address", response.body().getAddress());
                        intent.putExtra("contact", response.body().getContact());
                        intent.putExtra("username", response.body().getUsername());
                        intent.putExtra("email", response.body().getEmail());
                        intent.putExtra("image", response.body().getPath());
                        intent.putExtra("id", response.body().getId());
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        Toast.makeText(DashboardActivity.this, "Error ",Toast.LENGTH_SHORT).show();
                    }
                });

                Intent intent = new Intent(DashboardActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("token", MODE_PRIVATE);
                settings.edit().clear().commit();
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        imgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("token", MODE_PRIVATE);
                settings.edit().clear().commit();
                Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
                startActivity(intent);
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
            int count = 0;
            boolean dialogShown = false;
            if (proximity <6){
                userTooClose();
            }
        }
    };


    public AlertDialog myDialog;

    public void userTooClose() {
        if( myDialog != null && myDialog.isShowing() ) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
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
