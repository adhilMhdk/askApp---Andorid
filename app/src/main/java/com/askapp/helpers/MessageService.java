package com.askapp.helpers;

import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessageService extends FirebaseMessagingService {

    private static final String TAG = "MessageService";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Toast.makeText(this, "Got it", Toast.LENGTH_SHORT).show();
    }
}
