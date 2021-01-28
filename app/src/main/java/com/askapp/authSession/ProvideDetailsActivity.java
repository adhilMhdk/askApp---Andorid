package com.askapp.authSession;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.askapp.R;
import com.askapp.Threds.ProfileUploadThread;
import com.askapp.Threds.UpdateNameThread;
import com.askapp.database.Database;
import com.askapp.helpers.CONSTANTS;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProvideDetailsActivity extends AppCompatActivity {

    CircleImageView profileImage;


    private static final int SELECT_PICTURE = 2133;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_provide_details);
        init();

    }

    private void init() {
        profileImage = findViewById(R.id.profile_img);
    }

    public void start(View view) {



    }

    public void selectImage(View view) {
        Intent i = new Intent(
                Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data!=null){
            Uri selectedImage = data.getData();
            profileImage.setImageURI(selectedImage);




            File savePath = new File( new CONSTANTS().getRootDirName(),"pro_img.jpg");



            if (savePath.exists()){
                savePath.delete();
            }
            try {
                savePath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            saveUri(selectedImage,savePath);

            savePath = new File( new CONSTANTS().getRootDirName(),"pro_img_b.jpg");


            if (savePath.exists()){
                savePath.delete();
            }
            try {
                savePath.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }


            String AImagePath = new CONSTANTS().getRootDirName()+"/pro_img.jpg";
            String BImagePath =  getLowSizeImage(selectedImage,savePath);

            ArrayList<File> uploadList = new ArrayList<>();
            uploadList.add(new File(AImagePath));
            uploadList.add(new File(BImagePath));
            new ProfileUploadThread(uploadList,ProvideDetailsActivity.this).start();








        }
    }

    private void saveUri(Uri selectedImage,File savePath) {
        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(selectedImage, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            FileOutputStream out = new FileOutputStream(savePath);
            image.compress(Bitmap.CompressFormat.PNG, 100, out);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String getLowSizeImage(Uri bitmapImage,File savePath){

        try {
            ParcelFileDescriptor parcelFileDescriptor =
                    getContentResolver().openFileDescriptor(bitmapImage, "r");
            FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
            Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);

            int nh = (int) ( image.getHeight() * (64.0 / image.getWidth()) );
            image = Bitmap.createScaledBitmap(image, 64, nh, true);

            FileOutputStream out = new FileOutputStream(savePath);
            image.compress(Bitmap.CompressFormat.PNG, 100, out);

            parcelFileDescriptor.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return savePath.toString();
    }

    public void saveName(View view) {
        String name = ((TextView) findViewById(R.id.name_box)).getText().toString();
        if (!name.isEmpty()){
            HashMap<String,String> map = new HashMap<>();
            map.put("name",name);
            map.put("user",new Database(this).getPhone().replace("+",""));

            new UpdateNameThread(this,map).start();

            new Database(ProvideDetailsActivity.this).setName(name);
            startActivity(new Intent(ProvideDetailsActivity.this,InitializingActivity.class));
            overridePendingTransition(R.anim.slide_left_a,R.anim.slide_left_b);
            finish();


        }else{
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
        }
    }
}