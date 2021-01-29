package com.askapp.authSession;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.askapp.database.Database;
import com.askapp.helpers.CONSTANTS;
import com.askapp.server.Server;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoadContacts extends Thread {

    private static final String TAG = "LOAD_CONTACTS";
    Activity activity;
    Database database;
    OnLoadedCallback onLoadedCallback;
    String change = null;
    public Boolean running = false;
    CONSTANTS constants;
    private static String PHONE_ROW = "PHONE";

    public LoadContacts(Activity activity, OnLoadedCallback onLoadedCallback) {
        this.activity = activity;
        this.onLoadedCallback = onLoadedCallback;
        database = new Database(activity);
        constants = new CONSTANTS() ;
    }

    @Override
    public void run() {
        running = true;
        work();
    }

    private void work() {


        if (change == null){
            defaultLoad();
        }else if (change.equals(constants.INSERT)){
            defaultLoad();
        }else if (change.equals(constants.DELETE)){
            deletedLoad();
        }



    }

    private void deletedLoad() {
        ArrayList<ContactModel> addressBook = getContacts(activity);
        ArrayList<HashMap> contacts = database.getAllContacts();

        ArrayList<Integer> removePositions = new ArrayList<>();


        for (int i = 0; i < contacts.size(); i++) {
            Boolean contain = false;
            for (int j = 0; j < addressBook.size(); j++) {
                if (contacts.get(i).get(PHONE_ROW).toString().equals(addressBook.get(j).getMobileNumber())){
                    contain = true;
                    break;
                }
            }
            if (!contain){
                database.removeContact(contacts.get(i).get(PHONE_ROW).toString());
            }
        }

        for (int i = 0; i < removePositions.size(); i++) {
            contacts.remove(i);
        }

        running = false;

    }

    private void defaultLoad() {
        ArrayList<ContactModel> contacts = getContacts(activity);
        for (int i = 0; i < contacts.size(); i++) {
            HashMap<String,String> map = new HashMap<>();
            map.put("phone",contacts.get(i).getMobileNumber());
            Call<Void> call = new Server().getRetrofitInterface().executeCheckUser(map);
            int finalI = i;
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code()==200){
                        database.addContact(contacts.get(finalI).getName(),contacts.get(finalI).getMobileNumber());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });

        }
        running = false;
        onLoadedCallback.onLoaded();
    }

    public void setChange(String change) {
        this.change = change;
    }

    public ArrayList<ContactModel> getContacts(Context ctx) {
        ArrayList<ContactModel> list = new ArrayList<>();
        ContentResolver contentResolver = ctx.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                if (cursor.getInt(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor cursorInfo = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                    while (cursorInfo.moveToNext()) {
                        ContactModel info = new ContactModel();
                        info.id = id;
                        info.name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                        info.mobileNumber = cursorInfo.getString(cursorInfo.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replace(" ","").replace("+","");

                        list.add(info);
                    }

                    cursorInfo.close();
                }
            }
            cursor.close();
        }
        return list;
    }

    public interface OnLoadedCallback{
        void onLoaded();
    }
}
