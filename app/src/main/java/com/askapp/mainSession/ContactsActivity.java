package com.askapp.mainSession;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.askapp.R;
import com.askapp.mainSession.adapters.ContactsRecyclerAdapter;

public class ContactsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        init();
    }

    private void init() {
        recyclerView = findViewById(R.id.contacts_recycler_view);

        setUp();
    }

    private void setUp() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemViewCacheSize(10);

        recyclerView.setAdapter(new ContactsRecyclerAdapter(this));
    }
}