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

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.onlinevotingsystem.BLL.CandidateBLL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import API.API;

import model.Candidate;
import model.Position;
import model.ShakeEventListener;
import model.User;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class AddCandidateActivity extends AppCompatActivity {
    private Spinner spinnerPosition, spinnerMember;
    private Button btnSave;

    private SensorManager mSensorManager;
    private ShakeEventListener mSensorListener;

    List<Position> itemList;
    HashMap<Integer, String> spinnerMap = new HashMap<Integer, String>();
    ArrayList<String> Position_name = new ArrayList<String>();
    ArrayList<Integer> Position_id = new ArrayList<Integer>();

    List<User> itemList1;
    HashMap<Integer, String> spinnerMap1 = new HashMap<Integer, String>();
    ArrayList<String> Member_name = new ArrayList<String>();
    ArrayList<Integer> Member_id = new ArrayList<Integer>();

    int pos_id, mem_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_candidate);

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorListener = new ShakeEventListener();

        mSensorListener.setOnShakeListener(new ShakeEventListener.OnShakeListener() {

            public void onShake() {
                Toast.makeText(AddCandidateActivity.this, "Shake!", Toast.LENGTH_SHORT).show();
                alertUser();
            }
        });

        spinnerPosition = findViewById(R.id.spinnerPosition);
        spinnerMember = findViewById(R.id.spinnerMember);
        btnSave = findViewById(R.id.btnSave);

        API Api = Reusable.getInstance().create(API.class);
        Call<List<Position>> listCall = Api.fetchPositions();
        listCall.enqueue(new Callback<List<Position>>() {
            @Override
            public void onResponse(Call<List<Position>> call, Response<List<Position>> response) {
                attachPosition(response.body());
            }

            @Override
            public void onFailure(Call<List<Position>> call, Throwable t) {
                Toast.makeText(AddCandidateActivity.this, "Error : " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });

        Call<List<User>> listCall1 = Api.fetchUsers();
        listCall1.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                attachMember(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(AddCandidateActivity.this, "Error : " + t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }

        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final CandidateBLL bll = new CandidateBLL(mem_id, pos_id);
                StrictMode();
                SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
                String token = sharedPreference.getString("token","");
                if (bll.saveCandidate(token)){
                    Toast.makeText(AddCandidateActivity.this, "Successfully Added Candidate", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(AddCandidateActivity.this, CandidateActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    Toast.makeText(AddCandidateActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
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
        spinnerPosition.setAdapter(adapter);

        spinnerPosition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = spinnerPosition.getSelectedItem().toString();
                String position_id = spinnerMap.get(spinnerPosition.getSelectedItemPosition());
                pos_id = Integer.parseInt(position_id);
                Toast.makeText(AddCandidateActivity.this, " "+position_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void attachMember(List<User> body) {
        itemList1 = body;
        for (User items : itemList1) {
            Member_name.add(items.getName());
            Member_id.add(items.getId());
        }

        String[] spinnerArray1 = new String[itemList1.size()];

        for (int i = 0; i < Member_id.size(); i++) {
            spinnerMap1.put(i, Member_id.get(i)+"");
            spinnerArray1[i] = Member_name.get(i);
        }

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray1);
        adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spinnerMember.setAdapter(adapter1);

        spinnerMember.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String name = spinnerMember.getSelectedItem().toString();
                String ids = spinnerMap1.get(spinnerMember.getSelectedItemPosition());
                mem_id = Integer.parseInt(ids);
                Toast.makeText(AddCandidateActivity.this, " "+ids, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
                dialog.dismiss();
            }});
        builder.setNegativeButton(android.R.string.no, null);
        myDialog = builder.create();
        myDialog.show();
    }

}
