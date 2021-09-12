package com.huji_postpc_avih.sharemyshelter;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;

public class SheltersApp extends Application {
    private FirebaseFirestore firebaseApp;
    private ShelterDB db;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        firebaseApp = FirebaseFirestore.getInstance();
        db = ShelterDB.getInstance(this);
    }

    public FirebaseFirestore getFirebaseApp() {
        return firebaseApp;
    }

    public ShelterDB getDb() {
        return db;
    }
}
