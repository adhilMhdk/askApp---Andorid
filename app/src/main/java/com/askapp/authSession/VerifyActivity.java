package com.askapp.authSession;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.askapp.R;
import com.askapp.database.Database;
import com.askapp.mainSession.MainActivity;
import com.askapp.server.Server;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VerifyActivity extends AppCompatActivity {
    Database db;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        init();
    }

    @SuppressLint("SetTextI18n")
    private void init() {
        db = new Database(this);
        ((TextView)findViewById(R.id.hint)).setText("Enter the OTP send to "+db.getPhone());
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

    }

    public void verify(View view) {
        HashMap<String,String> map = new HashMap<>();
        map.put("otp",((TextView)findViewById(R.id.otp)).getText().toString());
        map.put("phone",db.getPhone());

        pd.show();
        pd.setMessage("Verifying");

        Call<Void> call = new Server().getRetrofitInterface().executePhoneVerification(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                pd.dismiss();
                if (response.code()==200){
                    startActivity(new Intent(VerifyActivity.this, ProvideDetailsActivity.class));
                }else if (response.code() == 504){
                    AlertDialog.Builder aBuilder = new AlertDialog.Builder(VerifyActivity.this);
                    aBuilder.setTitle("Incorrect OTP");
                    aBuilder.setMessage("You have entered incorrect OTP\nFixes\n1)Make sure that you have entered OTP correctly\n2)If you didn't get OTP, Check your phone number ");
                    aBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }else {
                    AlertDialog.Builder aBuilder = new AlertDialog.Builder(VerifyActivity.this);
                    aBuilder.setTitle("OTP have been expired");
                    aBuilder.setMessage("Your OTP have been expired. Try again by resending it");
                    aBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                AlertDialog.Builder aBuilder = new AlertDialog.Builder(VerifyActivity.this);
                aBuilder.setTitle("An error occurred");
                aBuilder.setMessage(t.getMessage());
                aBuilder.setPositiveButton("report", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                    }
                });
                pd.dismiss();
            }
        });
    }


    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                this.finishAndRemoveTask();
            }else{
                this.finishAffinity();
            }
            System.exit(0);

        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Prss BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}