package com.askapp.Threds;

import android.app.Activity;
import android.widget.Toast;

import com.askapp.database.Database;
import com.askapp.server.Server;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ProfileUploadThread extends Thread {

    ArrayList<File> arrayList;
    Activity activity;


    public ProfileUploadThread(ArrayList<File> arrayList, Activity activity) {
        this.arrayList = arrayList;
        this.activity = activity;
    }

    @Override
    public void run() {
        upload();
    }

    void upload(){
        File file = arrayList.get(0);
        OkHttpClient client = new OkHttpClient();
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("imgA", file.getName(),
                        RequestBody.create(MediaType.parse("text/plain"), file))
                .addFormDataPart("imgB",arrayList.get(1).getName(),
                        RequestBody.create(MediaType.parse("text/plane"),arrayList.get(1)))
                .addFormDataPart("user", new Database(activity).getPhone())
                .build();


        String url = new Server().getBASE_URL()+"/update/profile-image";
        Request request = new Request.Builder().url(url).post(formBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.code()==200){
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Profile image updated", Toast.LENGTH_SHORT).show();

                        }
                    });
                }else{
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(activity, "Profile image upload failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
