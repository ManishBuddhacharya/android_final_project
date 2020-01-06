package com.onlinevotingsystem;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.onlinevotingsystem.BLL.LoginSignupBLL;

import API.API;
import model.Position;
import model.ShakeEventListener;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class EditMemberActivity extends AppCompatActivity {
    private EditText etName,etUsername, etEmail, etContact, etAddress;
    private Button btnUpdate;
    int id;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_member);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Toast.makeText(EditMemberActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
                alertUser();
            }
        });

        etName = findViewById(R.id.etName);
        etAddress= findViewById(R.id.etAddress);
        etUsername= findViewById(R.id.etUsername);
        etEmail= findViewById(R.id.etEmail);
        etContact= findViewById(R.id.etContact);
        btnUpdate = findViewById(R.id.btnUpdate);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            etName.setText(bundle.getString("name"));
            etContact.setText(bundle.getString("contact"));
            etAddress.setText(bundle.getString("address"));
            etEmail.setText(bundle.getString("email"));
            etUsername.setText(bundle.getString("username"));
            id = bundle.getInt("id");
        }

        btnUpdate .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    String name = etName.getText().toString();
                    String address = etAddress.getText().toString();
                    String email = etEmail.getText().toString();
                    String username = etUsername.getText().toString();
                    String contact = etContact.getText().toString();
                    final LoginSignupBLL bll = new LoginSignupBLL( name, username, email, address, contact);
                    StrictMode();
                    SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
                    String token = sharedPreference.getString("token","");
                    if (bll.updateUser(token, id)){
                        Toast.makeText(EditMemberActivity.this, "Update Successful;", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(EditMemberActivity.this, MemberActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(EditMemberActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
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
                etUsername.getText().clear();
                etName.getText().clear();
                etAddress.getText().clear();
                etContact.getText().clear();
                etEmail.getText().clear();
                etName.requestFocus();
            }});
        builder.setNegativeButton(android.R.string.no, null);
        myDialog = builder.create();
        myDialog.show();
    }

    public boolean validation(){
        boolean flag = true;
        if (TextUtils.isEmpty(etName.getText().toString())){
            etName.setError("Please enter FullName");
            etName.requestFocus();
            flag = false;
        }
        else if (TextUtils.isEmpty(etContact.getText().toString())){
            etContact.setError("Please enter Contact Number");
            etContact.requestFocus();
            flag = false;
        }
        else if (TextUtils.isEmpty(etAddress.getText().toString())){
            etAddress.setError("Please enter Address");
            etAddress.requestFocus();
            flag = false;
        }
        else if (TextUtils.isEmpty(etEmail.getText().toString())){
            etEmail.setError("Please enter Email");
            etEmail.requestFocus();
            flag = false;
        }
        else if (TextUtils.isEmpty(etUsername.getText().toString())){
            etUsername.setError("Please enter Username");
            etUsername.requestFocus();
            flag = false;
        }

        return flag;
    }

}
