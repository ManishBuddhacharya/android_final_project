package com.onlinevotingsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.onlinevotingsystem.BLL.PositionBLL;

import java.net.URL;

import API.API;
import model.Position;
import model.ShakeEventListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class EditPositionActivity extends AppCompatActivity {
    private EditText etPositionName;
    private Button btnUpdatePosition;
    int id = 0;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_position);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Toast.makeText(EditPositionActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
                alertUser();
            }
        });

        etPositionName = findViewById(R.id.etPositionName);
        btnUpdatePosition = findViewById(R.id.btnPositionUpdate);
        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            etPositionName.setText(bundle.getString("name"));
            id = bundle.getInt("id");
        }

        btnUpdatePosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    String name = etPositionName.getText().toString();
                    final PositionBLL bll = new PositionBLL(name);
                    StrictMode();
                    SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
                    String token = sharedPreference.getString("token","");
                    if (bll.updatePosition(token, id)){
                        Toast.makeText(EditPositionActivity.this, "Update Successful;", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditPositionActivity.this, PositionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(EditPositionActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(mSensorListener,
                mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onPause() {
        mSensorManager.unregisterListener(mSensorListener);
        super.onPause();
    }

    public AlertDialog myDialog;
    public void alertUser() {
        if( myDialog != null && myDialog.isShowing() ) return;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Undo Typing?");
        builder.setMessage("Do you really want to reset form?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                etPositionName.getText().clear();
                etPositionName.requestFocus();
            }});
        builder.setNegativeButton(android.R.string.no, null);
        myDialog = builder.create();
        myDialog.show();
    }

    public boolean validation(){
        boolean flag = true;
        if (TextUtils.isEmpty(etPositionName.getText().toString())){
            etPositionName.setError("Please enter Position Name");
            etPositionName.requestFocus();
            flag = false;
        }
        return flag;
    }

}
