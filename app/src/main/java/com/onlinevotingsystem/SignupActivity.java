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
import android.widget.TextView;
import android.widget.Toast;

import com.onlinevotingsystem.BLL.LoginSignupBLL;

import java.util.ArrayList;

import model.ShakeEventListener;


public class SignupActivity extends AppCompatActivity {
    private EditText etName,etUsername, etPassword, etConPassword, etEmail, etContact, etAddress;
    private Button btnRegister;
    private TextView tvLogin;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Toast.makeText(SignupActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
                alertUser();
            }
        });

        etName = findViewById(R.id.etName);
        etAddress= findViewById(R.id.etAddress);
        etUsername= findViewById(R.id.etUsername);
        etEmail= findViewById(R.id.etEmail);
        etContact= findViewById(R.id.etContact);
        etPassword= findViewById(R.id.etPassword);
        etConPassword= findViewById(R.id.etConPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvLogin= findViewById(R.id.tvLogin);

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validation()){
                    String name = etName.getText().toString();
                    String contact = etContact.getText().toString();
                    String email = etEmail.getText().toString();
                    String address = etAddress.getText().toString();
                    String password = etPassword.getText().toString();
                    String conPassword = etConPassword.getText().toString();
                    String username = etUsername.getText().toString();
                    String usertype= "member";

                    if(conPassword.equals(password)){

                        final LoginSignupBLL bll = new LoginSignupBLL( name, username, usertype, email, address, contact, password);
                        StrictMode();
                        ArrayList<String> arrlist = new ArrayList<String>(3);
                        arrlist = bll.registerUser();
                        if(bll.registerUser().size() >= 0){
                            SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreference.edit();

                            editor.putString("token", arrlist.get(0));
                            editor.putInt("id" ,Integer.parseInt(arrlist.get(1)));
                            editor.putString("usertype",arrlist.get(2));
                            editor.commit();
                            Toast.makeText(SignupActivity.this, "Successfully Added", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignupActivity.this, DashboardActivity.class);
                            startActivity(intent);
                            finish();
                        }
                        else {
                            Toast.makeText(SignupActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                        }
                    }
                    else {
                        Toast.makeText(SignupActivity.this, "Password and Confirm Password did mot match.", Toast.LENGTH_LONG).show();
                        etPassword.requestFocus();
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
                etPassword.getText().clear();
                etName.getText().clear();
                etAddress.getText().clear();
                etEmail.getText().clear();
                etContact.getText().clear();
                etConPassword.getText().clear();
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
        else if (TextUtils.isEmpty(etPassword.getText().toString())){
            etPassword.setError("Please enter Password");
            etPassword.requestFocus();
            flag = false;
        }
        else if (TextUtils.isEmpty(etConPassword.getText().toString())){
            etConPassword.setError("Please enter Confirm Password");
            etConPassword.requestFocus();
            flag = false;
        }

        return flag;
    }
}
