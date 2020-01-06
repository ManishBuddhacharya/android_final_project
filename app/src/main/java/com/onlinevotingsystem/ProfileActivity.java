package com.onlinevotingsystem;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import API.API;
import model.ImageResponse;
import model.User;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import reusable.Reusable;

public class ProfileActivity extends AppCompatActivity {
    private EditText etName,etUsername, etPassword, etEmail, etContact, etAddress, etConPassword;
    private Button btnUpdate;
    private ImageView imgItem;
    int id;
    String imagePath, path;
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        etName = findViewById(R.id.etName);
        etAddress= findViewById(R.id.etAddress);
        etUsername= findViewById(R.id.etUsername);
        etEmail= findViewById(R.id.etEmail);
        etContact= findViewById(R.id.etContact);
        etPassword= findViewById(R.id.etPassword);
        etConPassword= findViewById(R.id.etConPassword);
        btnUpdate = findViewById(R.id.btnUpdate);
        imgItem = findViewById(R.id.imgItem);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null){
            etName.setText(bundle.getString("name"));
            etContact.setText(bundle.getString("contact"));
            etAddress.setText(bundle.getString("address"));
            etEmail.setText(bundle.getString("email"));
            etUsername.setText(bundle.getString("username"));
            path = bundle.getString("image");
            id = bundle.getInt("id");
        }
        final String imagePath = Reusable.BASE_URL+path;
        StrictMode();
        try {
            URL url = new URL(imagePath);
            imgItem.setImageBitmap(BitmapFactory.decodeStream((InputStream) url.getContent()));
        }
        catch (Exception e){
            e.printStackTrace();
        }

        btnUpdate .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });
        imgItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
            }
        });
    }

    private void updateProfile() {
        if (validation()) {
            String name = etName.getText().toString();
            String address = etAddress.getText().toString();
            String email = etEmail.getText().toString();
            String username = etUsername.getText().toString();
            String contact = etContact.getText().toString();
            String password = etPassword.getText().toString();
            String conPassword = etConPassword.getText().toString();

            if (password.equals(conPassword)) {
                saveImageOnly();
                User user = new User(name, username, email, address, contact, "public/uploads/" + imageName, password, 1);
                SharedPreferences sharedPreference = getSharedPreferences("token", MODE_PRIVATE);
                String token = sharedPreference.getString("token", "");
                API api = Reusable.getInstance().create(API.class);
                Call<Void> userCall = api.updateProfileAndroid(token, id, user);

                userCall.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Toast.makeText(ProfileActivity.this, "Profile Updated Successful;", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(ProfileActivity.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this, "Error : " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            } else {
                Toast.makeText(ProfileActivity.this, "Password and Confirm password did not match.", Toast.LENGTH_LONG).show();

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){
            if (data == null){
                Toast.makeText(ProfileActivity.this, "Please Select Image",Toast.LENGTH_LONG).show();
            }
        }
        Uri uri = data.getData();
        imagePath = getRealPathFromUri(uri);
        previewImage(imagePath);
    }

    private void BrowseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    private void previewImage(String imagePath) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imgItem.setImageBitmap(myBitmap);
        }
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(), uri, projection, null,null,null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void saveImageOnly(){
        File file = new File(imagePath);

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        API itemsAPI1 = Reusable.getInstance().create(API.class);
        Call<ImageResponse> responseBodyCall = itemsAPI1.uploadImage(body);

        StrictMode();

        try {

            Response<ImageResponse> imageResponseResponse = responseBodyCall.execute();
            Log.d("mero", "saveImageOnly: " + imageResponseResponse.body().toString());
            imageName = imageResponseResponse.body().getFileName();
        }
        catch (IOException e)
        {
            Toast.makeText(ProfileActivity.this, "error : "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

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
