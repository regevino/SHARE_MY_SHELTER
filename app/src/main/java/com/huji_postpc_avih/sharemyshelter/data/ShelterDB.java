package com.huji_postpc_avih.sharemyshelter.data;

import android.content.Context;
import android.location.Location;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.huji_postpc_avih.sharemyshelter.users.UserManager;
import com.huji_postpc_avih.sharemyshelter.users.UserManager.*;

public class ShelterDB {
    public static final String SHELTERS = "shelters";
    public static final String USERS = "users";
    private ArrayList<Shelter> allShelters;
    private ArrayList<Shelter> userShelters;
    private UserManager manager; //todo init in constructor
    private static ShelterDB me = null;
    private FirebaseFirestore firebase;


    private ShelterDB(Context c) {
        SheltersApp app = (SheltersApp) c.getApplicationContext();
        firebase = app.getFirebaseApp();
        allShelters = new ArrayList<>();
        userShelters = new ArrayList<>();
        firebase.collection(SHELTERS).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                Shelter shelter = document.toObject(Shelter.class);
                if (shelter.getOwnerId().equals(/*manager.getCurrentUser()*/UUID.fromString("Brian May")))
                {
                    userShelters.add(shelter);
                }
                allShelters.add(shelter);
            }

        });
    }

    public static ShelterDB getInstance(Context c) {
        if (me == null) {
            me = new ShelterDB(c);
        }
        return me;
    }

    public boolean addPrivateShelter(Shelter shelterToAdd, UUID userId){
        firebase.collection(SHELTERS).document(shelterToAdd.getId().toString())
                .set(shelterToAdd, SetOptions.merge()).addOnSuccessListener(command ->
                firebase.collection(USERS).document(userId.toString())
                        .set(shelterToAdd.getId(), SetOptions.merge())
                        .addOnFailureListener(e -> {/*TODO*/}))
                .addOnFailureListener(command -> {/*TODO*/});
        return true;
    }

    Shelter getShelterById(UUID shelterId)
    {
        for (Shelter shelter :allShelters) {
            if (shelter.getId().equals(shelterId)) {
                return shelter;
            }
        }
        return null;
    }

    List<Shelter> getSheltersByUserId(UUID userId)
    {

        return null;
    }

    boolean removePrivateShelter(UUID shelterId)
    {
        return false;
    }
//    UUID addUser(UUID userId)
//    {
//
//    }

}
