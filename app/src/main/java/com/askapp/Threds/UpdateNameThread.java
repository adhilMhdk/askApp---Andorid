package com.askapp.Threds;

import android.content.Context;
import android.widget.Toast;

import com.askapp.server.Server;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateNameThread extends Thread {
    Context context;
    HashMap<String,String> map;

    public UpdateNameThread(Context context, HashMap<String, String> map) {
        this.context = context;
        this.map = map;
    }

    @Override
    public void run() {
        super.run();

        Call<Void> call = new Server().getRetrofitInterface().executeUpdateName(map);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(context, "Profile name updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
