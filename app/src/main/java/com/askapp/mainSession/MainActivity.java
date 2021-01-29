package com.askapp.mainSession;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.askapp.R;
import com.askapp.helpers.ContactsListner;
import com.askapp.mainSession.adapters.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    ViewPager viewPager;
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();

        this.getApplicationContext()
                .getContentResolver()
                .registerContentObserver(
                        ContactsContract.Contacts.CONTENT_URI, true,
                        new ContactsListner(new Handler(),this,getContactCount()));

    }

    private int getContactCount() {
        Cursor cursor = null;
        try {
            cursor = getContentResolver().query(
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

    private void init() {
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabs);

        setUp();
    }

    private void setUp() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.chat_icon_outline);
        tabLayout.getTabAt(1).setIcon(R.drawable.status_icon);
        tabLayout.getTabAt(2).setIcon(R.drawable.call_icon);
        tabLayout.getTabAt(3).setIcon(R.drawable.account_icon);
    }



    public void openContacts(MenuItem item) {
        startActivity(new Intent(MainActivity.this, ContactsActivity.class));
    }
}