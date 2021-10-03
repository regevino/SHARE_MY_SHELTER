package com.huji_postpc_avih.sharemyshelter;

import android.Manifest;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.huji_postpc_avih.sharemyshelter.alerts.MyFirebaseMessagingService;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;
import com.huji_postpc_avih.sharemyshelter.data.ShelterStorage;
import com.huji_postpc_avih.sharemyshelter.users.UserManagerFirebase;

public class SheltersApp extends Application {
    public static final String NOTIFICATION_ALERTS_CHANNEL_NAME = "Red Alerts";
    public static final String NOTIFICATION_ALERTS_CHANNEL_DESC = "Red Alerts";
    public static final String NOTIFICATION_ALERTS_CHANNEL_ID = "12345";
    private FirebaseFirestore firebaseApp;
    private ShelterDB db;
    private ShelterStorage storage;
    private FirebaseStorage firebaseStorage;
    private UserManagerFirebase userManager;

    @Override
    public void onCreate() {
        super.onCreate();


        FirebaseApp.initializeApp(this);
        firebaseApp = FirebaseFirestore.getInstance();
        userManager = UserManagerFirebase.getInstance();
        db = ShelterDB.getInstance(this);

        firebaseStorage = FirebaseStorage.getInstance();
        storage = ShelterStorage.getInstance(this);


        MyFirebaseMessagingService.initialiseMessaging(this);
        setupNotificationChannels();
        setupFCMService();
    }

    public FirebaseFirestore getFirebaseApp() {
        return firebaseApp;
    }

    public ShelterDB getDb() {
        return db;
    }

    private void setupNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = NOTIFICATION_ALERTS_CHANNEL_NAME;
            String description = NOTIFICATION_ALERTS_CHANNEL_DESC;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_ALERTS_CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void setupFCMService() {
        Intent intent = new Intent(this, MyFirebaseMessagingService.class);
        startService(intent);
    }

    public UserManagerFirebase getUserManager() {
        return userManager;
    }

    public FirebaseStorage getStorageRef() {
        return firebaseStorage;
    }


    public ShelterStorage getShelterStorage() {
        return storage;
    }
}
