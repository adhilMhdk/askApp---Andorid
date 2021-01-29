package com.askapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.askapp.authSession.AuthActivity;
import com.askapp.authSession.ProvideDetailsActivity;
import com.askapp.database.Database;
import com.askapp.mainSession.MainActivity;
import com.askapp.server.Server;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {

    Database database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);



        init();


        registerFCM();




    }




    private void registerFCM() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TAG", "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        HashMap<String,String> map = new HashMap<>();
                        map.put("user",database.getPhone());
                        map.put("token",token);
                        Call<Void> call = new Server().getRetrofitInterface().executeUpdatePTK(map);
                        call.enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {

                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {

                            }
                        });

                        // Log and toast
                    }
                });
    }

    private void init() {
        database = new Database(this);
        database.createUserDetailsTable();


        if (database.getStatus()!=null) {
            if (database.getStatus().equals("true")) {
                if (database.getName() == null) {
                    startActivity(new Intent(StartActivity.this, ProvideDetailsActivity.class));
                } else {
                    startActivity(new Intent(StartActivity.this, MainActivity.class));
                }
            } else {
                startActivity(new Intent(StartActivity.this, AuthActivity.class));

            }
        }else{
            startActivity(new Intent(StartActivity.this, AuthActivity.class));
        }

    }




}