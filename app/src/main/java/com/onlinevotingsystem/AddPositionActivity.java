package com.onlinevotingsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.onlinevotingsystem.BLL.LoginSignupBLL;
import com.onlinevotingsystem.BLL.PositionBLL;
import android.os.StrictMode;

import model.ShakeEventListener;


public class AddPositionActivity extends AppCompatActivity {
    private EditText etPosition;
    private Button btnPositionSave;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_position);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Toast.makeText(AddPositionActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
                alertUser();
            }
        });

        etPosition = findViewById(R.id.etPositionName);
        btnPositionSave = findViewById(R.id.btnPositionSave);

        btnPositionSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    String name = etPosition.getText().toString();
                    final PositionBLL bll = new PositionBLL(name);
                    StrictMode();
                    SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
                    String token = sharedPreference.getString("token","");
                    if (bll.addPosition(token)){
                        Toast.makeText(AddPositionActivity.this, "Successfully Added Position", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AddPositionActivity.this, PositionActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(AddPositionActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
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
                etPosition.getText().clear();
                etPosition.requestFocus();
            }});
        builder.setNegativeButton(android.R.string.no, null);
        myDialog = builder.create();
        myDialog.show();
    }

    public boolean validation(){
        boolean flag = true;
        if (TextUtils.isEmpty(etPosition.getText().toString())){
            etPosition.setError("Please enter Position Name");
            etPosition.requestFocus();
            flag = false;
        }
        return flag;
    }

}
