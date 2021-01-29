package com.askapp.helpers;

import android.app.Activity;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.ContactsContract;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.askapp.authSession.LoadContacts;

public class ContactsListner extends ContentObserver {
    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     *
     *
     */

    Activity activity;
    int contactsCount;
    CONSTANTS constants;

    public ContactsListner(Handler handler, Activity activity, int contactsCount) {
        super(handler);
        this.activity = activity;
        this.contactsCount = contactsCount;
        constants = new CONSTANTS();
    }




    @Override
    public void onChange(boolean selfChange, @Nullable Uri uri) {
        super.onChange(selfChange, uri);



            int currentContacts = getContactCount();
            Toast.makeText(activity, "Current num is "+currentContacts+"     and recent : "+contactsCount, Toast.LENGTH_SHORT).show();

            LoadContacts loadContacts = new LoadContacts(activity, new LoadContacts.OnLoadedCallback() {
                @Override
                public void onLoaded() {

                }
            });

            if (currentContacts<contactsCount){
                loadContacts.setChange(constants.DELETE);
            }else if (currentContacts == contactsCount){
                loadContacts.setChange(constants.UPDATE);
            }else {
                loadContacts.setChange(constants.INSERT);
            }
            if (!loadContacts.running){
                loadContacts.start();
            }

            contactsCount = currentContacts;

    }





    private int getContactCount() {
        Cursor cursor = null;
        try {
            cursor = activity.getContentResolver().query(
                    ContactsContract.Contacts.CONTENT_URI, null, null, null,
                    null);
            if (cursor != null) {
                return cursor.getCount();
            } else {
                return 0;
            }
        } catch (Exception ignore) {
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }
}
