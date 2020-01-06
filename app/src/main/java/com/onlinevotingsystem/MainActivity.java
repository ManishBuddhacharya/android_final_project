package com.onlinevotingsystem;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.ConnectivityManager;
import android.os.StrictMode;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
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
import model.ContextSet;
import model.ShakeEventListener;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnLogin;
    private TextView tvSignup;
    private EditText etUsername, etPassword;
    private SensorManager mSensorManager;

    private ShakeEventListener mSensorListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ContextSet.setContext(getApplicationContext());

        btnLogin = findViewById(R.id.btnLogin);
        tvSignup = findViewById(R.id.tvSignup);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);

        btnLogin.setOnClickListener(this);
        tvSignup.setOnClickListener(this);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Toast.makeText(MainActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
                alertUser();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            
            case R.id.btnLogin:
                if (validation()){
                    final LoginSignupBLL bll = new LoginSignupBLL( etPassword.getText().toString(), etUsername.getText().toString());
                    StrictMode();
                    ArrayList<String> arrlist = new ArrayList<String>(3);
                    arrlist = bll.checkUser();
                    if (bll.checkUser().size() > 0){
                        SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreference.edit();

                        editor.putString("token", arrlist.get(0));
                        editor.putInt("id" ,Integer.parseInt(arrlist.get(1)));
                        editor.putString("usertype",arrlist.get(2));
                        editor.commit();
                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(MainActivity.this, "Username or password incorrect", Toast.LENGTH_LONG).show();
                    }
                }
                break;

            case R.id.tvSignup:
                Intent intent1 = new Intent(MainActivity.this, SignupActivity.class);
                startActivity(intent1);
                break;
        }

    }
    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    BroadcastRecieverExample broadcastRecieverExample = new BroadcastRecieverExample(this);

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(broadcastRecieverExample, intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastRecieverExample);
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
                etUsername.requestFocus();
            }});
        builder.setNegativeButton(android.R.string.no, null);
        myDialog = builder.create();
        myDialog.show();
    }

    public boolean validation(){
        boolean flag = true;
        if (TextUtils.isEmpty(etUsername.getText().toString())){
            etUsername.setError("Please enter Username");
            etUsername.requestFocus();
            flag = false;
        }
        else if (TextUtils.isEmpty(etPassword.getText().toString())){
            etPassword.setError("Please enter Password");
            etPassword.requestFocus();
            flag = false;
        }
        return flag;
    }

}
