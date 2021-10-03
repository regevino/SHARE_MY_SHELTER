package com.huji_postpc_avih.sharemyshelter.alerts;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.navigation.NavigateToShelterActivity;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";
    public static final String ALERTS_TOPIC = "/topics/Alerts";
    public static final String TEST_MESSAGE_TOPIC_PREFIX = "/topics/test_message";
    public static final String INTENT_ACTION_DISMISS = "Dismiss";
    public static final String EXTRA_NOTIFICATION_ID = "notification_id";
    public static final String EXTRA_IS_FOREGROUND_NOTIFICATION = "Service_to_background";

    public static void initialiseMessaging(Context context)
    {
        FirebaseMessaging.getInstance().subscribeToTopic(ALERTS_TOPIC)
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

            if (remoteMessage.getFrom().equals(ALERTS_TOPIC)) {
                if (checkIfInArea(remoteMessage.getData())) {
                    launchNavigation(extractDeadlineFromData(remoteMessage.getData()));
                }
            }
            if (remoteMessage.getFrom().startsWith(TEST_MESSAGE_TOPIC_PREFIX)) {
                FirebaseMessaging.getInstance().unsubscribeFromTopic(remoteMessage.getFrom());
                testAlert();
            }

        }
    }

    private boolean checkIfInArea(Map<String, String> messageData)
    {
        return true;
    }

    private long extractDeadlineFromData(Map<String, String> messageData)
    {
        return new Date().getTime() + 1000 * 60 * 5;
    }

    public void testAlert()
    {
        launchNavigation(new Date().getTime() + 1000 * 60 * 5);
    }

    private void launchNavigation(long arrivalDeadline) {

        BroadcastReceiver b = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction())
                {
                    case INTENT_ACTION_DISMISS:
                        NotificationManagerCompat.from(context).cancel(intent.getIntExtra(EXTRA_NOTIFICATION_ID, -1));;
                        getApplicationContext().unregisterReceiver(this);
                    default:
                        break;
                }
            }
        };
        int notification_id = new Random(123456).nextInt();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(INTENT_ACTION_DISMISS);
        getApplicationContext().registerReceiver(b, intentFilter);
        Intent dismissIntent = new Intent();
        dismissIntent.setAction(INTENT_ACTION_DISMISS);
        dismissIntent.putExtra(EXTRA_NOTIFICATION_ID, notification_id);
        PendingIntent dismissPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, dismissIntent, 0);

        Intent fullScreenIntent = new Intent(this, AlertRecievedActivity.class);
        fullScreenIntent.putExtra(NavigateToShelterActivity.EXTRA_KEY_END_ALERT_TIME, arrivalDeadline);
        fullScreenIntent.putExtra(EXTRA_NOTIFICATION_ID, notification_id);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, SheltersApp.NOTIFICATION_ALERTS_CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_alert_notification)
                            .setContentTitle("Red Alert!")
                            .setContentText("Red Alert in your area")
                            .addAction(R.drawable.ic_alert_notification, INTENT_ACTION_DISMISS, dismissPendingIntent)
                            .addAction(R.drawable.ic_alert_notification, "Navigate", PendingIntent.getActivity(this, 0, fullScreenIntent, 0));
            Notification notification = notificationBuilder.build();
            NotificationManagerCompat.from(this).notify(notification_id, notification);


            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            getApplication().startActivity(fullScreenIntent);
            }
        else {
            Log.d(TAG, "Starting with full screen intent");
            fullScreenIntent.putExtra(EXTRA_IS_FOREGROUND_NOTIFICATION, true);
            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                    fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, SheltersApp.NOTIFICATION_ALERTS_CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_alert_notification)
                            .setContentTitle("Red Alert!")
                            .setContentText("Red Alert in your area")
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setFullScreenIntent(fullScreenPendingIntent, true)
                            .addAction(R.drawable.ic_alert_notification, INTENT_ACTION_DISMISS, dismissPendingIntent)
                            .addAction(R.drawable.ic_alert_notification, "Navigate", PendingIntent.getActivity(this, 0, fullScreenIntent, 0));

            Notification notification = notificationBuilder.build();

            startForeground(notification_id, notification);
            // TODO Make sure service is moved back to background later.
        }
    }

}