package com.huji_postpc_avih.sharemyshelter.data;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.huji_postpc_avih.sharemyshelter.users.UserManager;

public class ShelterDB {
    public static final String SHELTERS = "shelters";
    public static final String USERS = "users";
    private SheltersHolder allShelters;
    private SheltersHolder userShelters;
    private UserManager manager;
    private static ShelterDB me = null;
    private FirebaseFirestore firebase;
    private ListenerRegistration listenerRegistration;
    private final SheltersApp app;


    private ShelterDB(Context c) {
        app = (SheltersApp) c.getApplicationContext();
        firebase = app.getFirebaseApp();
        manager = app.getUserManager();
        updateLocalShelterLists();
//        listenerRegistration = firebase.collection(SHELTERS).addSnapshotListener((value, error) -> {
//            for (QueryDocumentSnapshot doc : value) {
//                Shelter shelter = new Shelter(doc.toObject(ShelterWrapper.class));
//                if (!doc.exists()) {
//                    //TODO: Erase from local list
//                    userShelters.deleteShelter(shelter);
//                    continue;
//                }
//            }
//        });
    }

    public void updateLocalShelterLists() {
        allShelters = new SheltersHolder(new ArrayList<>());
        userShelters = new SheltersHolder(new ArrayList<>());
        firebase.collection(SHELTERS).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                if (document.getBoolean("dummy") != null) {
                    continue;
                }
                Shelter shelter = new Shelter(document.toObject(ShelterWrapper.class));
                String currentUser = manager.getCurrentUser();
                if (shelter.getOwnerId() != null && shelter.getOwnerId().equals(currentUser)) {
                    userShelters.addShelter(shelter);
                }
                allShelters.addShelter(shelter);
            }

            app.sendBroadcast(new Intent("Added"));
        });
    }

    public static ShelterDB getInstance(Context c) {
        if (me == null) {
            me = new ShelterDB(c);
        }
        return me;
    }

    public void addPrivateShelter(Shelter shelterToAdd) {
        firebase.collection(SHELTERS).document(shelterToAdd.getId().toString())
                .set(new ShelterWrapper(shelterToAdd)).addOnSuccessListener(command ->
                firebase.collection(USERS).document(shelterToAdd.getOwnerId())
                        .collection("User's Shelters").document(shelterToAdd.getId().toString()).set(shelterToAdd.getId())
                        .addOnSuccessListener(unused -> {
                            //when we upload new private shelter we need to update the *local* DB.
                            updateLocalShelterLists();
                        })
                        .addOnFailureListener(e -> {/*TODO*/}))
                .addOnFailureListener(command -> {/*TODO*/});

    }

    public void addPublicShelter(Shelter shelterToAdd) {
        firebase.collection(SHELTERS).document(shelterToAdd.getId().toString())
                .set(new ShelterWrapper(shelterToAdd));
    }

    public void updateShelter(Shelter shelter) {
        //now its like addPublicShelter, we probably need to change it later.
        firebase.collection(SHELTERS).document(shelter.getId().toString())
                .set(new ShelterWrapper(shelter));


    }


    Shelter getShelterById(UUID shelterId) {
        for (Shelter shelter : allShelters.getItemsList()) {
            if (shelter.getId().equals(shelterId)) {
                return shelter;
            }
        }
        return null;
    }

    List<Shelter> getSheltersByUserId(UUID userId) {

        return null;
    }

    public boolean deletePrivateShelter(UUID shelterId) {
        firebase.collection(SHELTERS).document(shelterId.toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                userShelters.deleteShelter(shelterId);
                //TODO: notify the adapter to display the changes
            }
        });
        return false;
    }
//    UUID addUser(UUID userId)
//    {
//
//    }


    public ArrayList<Shelter> getUserShelters() {
        return userShelters.shelters;
    }

    public ArrayList<Shelter> getAllShelters() {
        return allShelters.shelters;
    }

    public UserManager getManager() {
        return manager;
    }
}
