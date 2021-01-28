package com.askapp.authSession;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.askapp.R;
import com.askapp.mainSession.MainActivity;

public class InitializingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_initializing);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(InitializingActivity.this, MainActivity.class));
                overridePendingTransition(R.anim.slide_left_a,R.anim.slide_left_b);
            }
        },4000);
    }
}