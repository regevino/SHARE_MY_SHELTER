package com.huji_postpc_avih.sharemyshelter.alerts;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;

import java.util.Random;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import static com.google.firebase.messaging.Constants.MessageNotificationKeys.CHANNEL;
import static com.google.firebase.messaging.Constants.MessageNotificationKeys.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    public static void initialiseMessaging(Context context)
    {
        FirebaseMessaging.getInstance().subscribeToTopic("Alerts")
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
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }

        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
        Intent intent = new Intent(this, AlertRecievedActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            intent.setFlags(Intent.FLAG_FROM_BACKGROUND);
//            intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        getApplication().startActivity(intent);
            }
        else {
            Log.d(TAG, "Starting with full screen intent");
            Intent fullScreenIntent = new Intent(this, AlertRecievedActivity.class);
            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                    fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, SheltersApp.NOTIFICATION_ALERTS_CHANNEL_ID)
                            .setSmallIcon(R.drawable.common_full_open_on_phone)
                            .setContentTitle("Incoming call")
                            .setContentText("(919) 555-1234")
                            .setPriority(NotificationCompat.PRIORITY_MAX)

                            // Use a full-screen intent only for the highest-priority alerts where you
                            // have an associated activity that you would like to launch after the user
                            // interacts with the notification. Also, if your app targets Android 10
                            // or higher, you need to request the USE_FULL_SCREEN_INTENT permission in
                            // order for the platform to invoke this notification.
                            .setFullScreenIntent(fullScreenPendingIntent, true);

            Notification notification = notificationBuilder.build();

            // notificationId is a unique int for each notification that you must define

            startForeground(new Random(123456).nextInt(), notification);
        }


    }

    @Override
    public void onCreate() {
        super.onCreate();
//        NotificationCompat.Builder notificationBuilder =
//                new NotificationCompat.Builder(this, "12345")
//                        .setSmallIcon(R.drawable.common_full_open_on_phone)
//                        .setContentTitle("Red Alert Running")
//                        .setContentText("Red Alert is running so you can receive popup notifications")
//                        .setPriority(NotificationCompat.PRIORITY_HIGH);
//
//        Notification notification = notificationBuilder.build();
//
//        // notificationId is a unique int for each notification that you must define
//        Log.d(TAG, "Starting service in foreground");
//        startForeground(new Random(123456).nextInt(), notification);
    }
}