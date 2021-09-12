package com.huji_postpc_avih.sharemyshelter;

import android.app.Application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;

public class SheltersApp extends Application {
    private FirebaseFirestore firebaseApp;

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }

}
