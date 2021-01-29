package com.askapp.mainSession.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.askapp.R;
import com.askapp.database.Database;

import java.util.ArrayList;
import java.util.HashMap;

public class ContactsRecyclerAdapter extends RecyclerView.Adapter<ContactsRecyclerAdapter.ViewHolder> {

    private static String NAME = "NAME";
    private static String PHONE = "PHONE";

    Activity activity;
    ArrayList<HashMap> contacts;

    public ContactsRecyclerAdapter(Activity activity) {
        this.activity = activity;
        contacts = new Database(activity).getAllContacts();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_item_layout,parent,false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ((TextView) holder.itemView.findViewById(R.id.contact_profile_name)).setText(contacts.get(position).get(NAME).toString());
        ((TextView) holder.itemView.findViewById(R.id.contact_profile_phone)).setText("+"+contacts.get(position).get(PHONE).toString());

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
