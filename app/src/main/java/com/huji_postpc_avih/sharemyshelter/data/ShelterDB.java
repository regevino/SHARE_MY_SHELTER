package com.huji_postpc_avih.sharemyshelter.data;

import android.content.Context;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.huji_postpc_avih.sharemyshelter.users.UserManager;

public class ShelterDB {
    public static final String SHELTERS = "shelters";
    public static final String USERS = "users";
    private ArrayList<Shelter> allShelters;
    private ArrayList<Shelter> userShelters;
    private UserManager manager; //todo init in constructor
    private static ShelterDB me = null;
    private FirebaseFirestore firebase;
    private ListenerRegistration listenerRegistration;


    private ShelterDB(Context c) {
        SheltersApp app = (SheltersApp) c.getApplicationContext();
        firebase = app.getFirebaseApp();
        allShelters = new ArrayList<>();
        userShelters = new ArrayList<>();
        updateLocalShelterLists();
        firebase.collection(SHELTERS).addSnapshotListener((value, error) -> {
            for (QueryDocumentSnapshot doc : value) {
                if (!doc.exists())
                {
                    //TODO Erase from local list
                    continue;
                }
                Shelter shelter = doc.toObject(Shelter.class);
            }
        });
    }

    private void updateLocalShelterLists() {
        firebase.collection(SHELTERS).get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot document : task.getResult()) {
                if (document.getBoolean("dummy")) {
                    continue;
                }
                Shelter shelter = Shelter.fromJson(document.toObject(String.class));
                String aString="JUST_A_TEST_STRING";
                String result = UUID.nameUUIDFromBytes(aString.getBytes()).toString();
                if (shelter.getOwnerId().equals(/*manager.getCurrentUser()*/UUID.fromString(result)))
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

    public void addPrivateShelter(Shelter shelterToAdd, UUID userId){
        firebase.collection(SHELTERS).document(shelterToAdd.getId().toString())
                .set(shelterToAdd, SetOptions.merge()).addOnSuccessListener(command ->
                firebase.collection(USERS).document(userId.toString())
                        .collection("User's Shelters").document(shelterToAdd.getId().toString()).set(shelterToAdd.getId())
                        .addOnFailureListener(e -> {/*TODO*/}))
                .addOnFailureListener(command -> {/*TODO*/});
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
