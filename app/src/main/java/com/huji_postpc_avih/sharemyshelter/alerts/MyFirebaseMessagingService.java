package com.huji_postpc_avih.sharemyshelter.alerts;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Map;

import androidx.annotation.NonNull;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";
    public static final String ALERTS_TOPIC_NAME = "/topics/Alerts";
    public static final String SUBSCRIBE_ALERTS_TOPIC = "Alerts";
    public static final String SUBSCRIBE_TEST_MESSAGE_TOPIC = "Test-Alerts";
    public static final String TEST_MESSAGE_TOPIC_NAME = "/topics/Test-Alerts";
    public static final String INTENT_ACTION_DISMISS = "Dismiss";
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";


    public static void initialiseMessaging(Context context)
    {
        FirebaseMessaging.getInstance().subscribeToTopic(SUBSCRIBE_ALERTS_TOPIC)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Subscribed for notifications";
                        if (!task.isSuccessful()) {
                            msg = "Failed to subscribe for notifications";
                        }
                        Log.d(TAG, msg);
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public MyFirebaseMessagingService() {
    }



    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Intent intent = new Intent(getBaseContext(), AlertRecievedService.class);
            if (remoteMessage.getFrom().equals(ALERTS_TOPIC_NAME)) {
                if (checkIfInArea(remoteMessage.getData())) {
                    intent.putExtra(AlertRecievedService.EXTRA_KEY_DEADLINE, extractDeadlineFromData(remoteMessage.getData()));
                    intent.setAction(AlertRecievedService.ACTION_ALERT_RECIEVED);
                    startService(intent);
                }
            }
            if (remoteMessage.getFrom().equals(TEST_MESSAGE_TOPIC_NAME)) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(SUBSCRIBE_TEST_MESSAGE_TOPIC);

                intent.setAction(AlertRecievedService.ACTION_TEST);
                startService(intent);
            }

        }
    }

    private boolean checkIfInArea(Map<String, String> messageData)
    {
        return true;
    }

    private long extractDeadlineFromData(Map<String, String> messageData)
    {
        int DEFAULT_ALERT_LENGTH_MIN = 2;
        return new Date().getTime() + 1000 * 60 * 2;
    }


}