package com.huji_postpc_avih.sharemyshelter.alerts;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;

import java.util.Date;
import java.util.Map;
import java.util.Random;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.preference.PreferenceManager;

public class AlertRecievedService extends Service {

    public final static String ACTION_TEST = "TEST_ALERT";
    public static final String ACTION_ALERT_RECIEVED = "Alert_Recieved";
    public static final String EXTRA_KEY_DEADLINE = "deadline";
    public static final String ACTION_DISMISS_ALERT = "dismiss_alert";
    public static final String EXTRA_IS_FOREGROUND_NOTIFICATION = "Service_to_background";
    public static final String EXTRA_IS_TEST = "isTest";
    private static final String TAG = "AlertReceivedService";

    private int notificationId;
    private boolean isForeground;
    private boolean isNavigating = false;

    public AlertRecievedService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null)
        {
            return super.onStartCommand(null, flags, startId);
        }
        if (intent.getAction().equals(ACTION_TEST) && !isNavigating)
        {
            testAlert();
        }
        if (intent.getAction().equals(ACTION_ALERT_RECIEVED) && !isNavigating)
        {
            long deadline = intent.getLongExtra(EXTRA_KEY_DEADLINE, new Date().getTime());
            launchNavigation(deadline, false);
        }
        if (intent.getAction().equals(ACTION_DISMISS_ALERT))
        {
            isNavigating = false;
            if (isForeground)
            {
                stopForeground(true);
            }
            else
            {
                NotificationManagerCompat.from(this).cancel(notificationId);;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }


    private boolean checkIfInArea(Map<String, String> messageData)
    {
        return true;
    }

    private long extractDeadlineFromData(Map<String, String> messageData)
    {
        int DEFAULT_ALERT_LENGTH_MIN = 2;
        return new Date().getTime() + 1000 * 60 * DEFAULT_ALERT_LENGTH_MIN;
    }

    public void testAlert()
    {
        int DEFAULT_ALERT_LENGTH_MIN = 2;
        launchNavigation(new Date().getTime() + 1000 * 60 * DEFAULT_ALERT_LENGTH_MIN, true);
    }



    private void launchNavigation(long arrivalDeadline, boolean isTest) {


        int notification_id = new Random(123456).nextInt();
        notificationId = notification_id;
        String title;
        String description;

        if (isTest)
        {
            title = "Testing Red Alert Notifications";
            description = "This is a test notification for Red Alerts.\n" +
                    "This DOES NOT indicate a rocket attack.";
        }
        else
        {
            title = "Red Alert!";
            description = "Red Alert in your area";
        }


        boolean show_popup = PreferenceManager.getDefaultSharedPreferences(this).getBoolean("preference_show_popup", true);
        isNavigating = true;
        Intent dismissIntent = new Intent(this, this.getClass());
        dismissIntent.setAction(ACTION_DISMISS_ALERT);

        PendingIntent dismissPendingIntent = PendingIntent.getService(this, 0, dismissIntent, PendingIntent.FLAG_UPDATE_CURRENT );
        Intent fullScreenIntent = new Intent(this, AlertRecievedActivity.class);
        fullScreenIntent.putExtra(EXTRA_KEY_DEADLINE, arrivalDeadline);
        fullScreenIntent.putExtra(EXTRA_IS_TEST, isTest);
        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        fullScreenIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, SheltersApp.NOTIFICATION_ALERTS_CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_alert_notification)
                            .setContentTitle(title)
                            .setContentText(description)
                            .addAction(R.drawable.ic_alert_notification, "Dismiss", dismissPendingIntent)
                            .addAction(R.drawable.ic_alert_notification, "Navigate", PendingIntent.getActivity(this, 0, fullScreenIntent, PendingIntent.FLAG_UPDATE_CURRENT));
            Notification notification = notificationBuilder.build();
            NotificationManagerCompat.from(this).notify(notification_id, notification);

            if (show_popup) {

                getApplication().startActivity(fullScreenIntent);
            }
        }
        else {
            Log.d(TAG, "Starting with full screen intent");
            fullScreenIntent.putExtra(EXTRA_IS_FOREGROUND_NOTIFICATION, true);
            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                    fullScreenIntent, PendingIntent.FLAG_ONE_SHOT);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, SheltersApp.NOTIFICATION_ALERTS_CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_alert_notification)
                            .setContentTitle(title)
                            .setContentText(description)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .addAction(R.drawable.ic_alert_notification, "Dismiss", dismissPendingIntent)
                            .addAction(R.drawable.ic_alert_notification, "Navigate", PendingIntent.getActivity(this, 0, fullScreenIntent, 0));

            if (show_popup)
            {

                notificationBuilder.setFullScreenIntent(fullScreenPendingIntent, true);
            }
            Notification notification = notificationBuilder.build();

            if(show_popup)
            {
                isForeground = true;
                startForeground(notification_id, notification);
            }
            else
            {
                NotificationManagerCompat.from(this).notify(notification_id, notification);
            }
        }
    }
}