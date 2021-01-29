package com.askapp.authSession;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.askapp.R;
import com.askapp.database.Database;
import com.askapp.server.Server;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION = 254;
    ProgressDialog pd;
    Database database;
    Boolean ok = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        init();
    }

    private void init() {
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        grantPermission();

        database = new Database(this);
    }

    private void grantPermission() {
        String[] perms = { Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.WRITE_CONTACTS };

        if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,perms,REQUEST_PERMISSION);
        }else {
            ok = true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==REQUEST_PERMISSION ){
            if (resultCode == RESULT_OK){
                ok = true;
            }
        }
    }

    public void signUp(View view) {

       if (ok){
           pd.show();
           pd.setMessage("Connecting");

           String phone = ((TextView) findViewById(R.id.phone)).getText().toString();
           String countryCode = ((CountryCodePicker) findViewById(R.id.ccp)).getSelectedCountryCode();
           String country = ((CountryCodePicker) findViewById(R.id.ccp)).getSelectedCountryName();
           HashMap<String,String> map = new HashMap<>();
           map.put("phone",phone);
           map.put("country-code",countryCode);
           map.put("country-name",country);

           Call<Void> call = new Server().getRetrofitInterface().executeSignUp(map);
           call.enqueue(new Callback<Void>() {
               @Override
               public void onResponse(Call<Void> call, Response<Void> response) {
                   pd.dismiss();
                   database.setPhone("+"+countryCode+phone);
                   startActivity(new Intent(AuthActivity.this,VerifyActivity.class));
                   overridePendingTransition(R.anim.slide_left_a,R.anim.slide_left_b);
                   finish();

               }

               @Override
               public void onFailure(Call<Void> call, Throwable t) {
                   AlertDialog.Builder aBuilder = new AlertDialog.Builder(AuthActivity.this);
                   aBuilder.setTitle("An error occurred");
                   AlertDialog dialog;
                   aBuilder.setMessage(t.getMessage());
                   aBuilder.setPositiveButton("report", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           if (dialog!=null){
                               dialog.dismiss();
                           }
                       }
                   }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                       @Override
                       public void onClick(DialogInterface dialog, int which) {
                           if (dialog!=null){
                               dialog.dismiss();
                           }
                       }
                   });
                   dialog= aBuilder.show();
                   pd.dismiss();
               }
           });
       }else{
           Toast.makeText(this, "You have to allow storage and contacts permission to continue", Toast.LENGTH_SHORT).show();
           grantPermission();
       }


    }
}