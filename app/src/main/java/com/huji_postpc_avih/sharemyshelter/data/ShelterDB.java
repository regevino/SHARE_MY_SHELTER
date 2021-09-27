package com.huji_postpc_avih.sharemyshelter.data;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryBounds;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.users.UserManager;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;

public class ShelterDB {
    public static final String SHELTERS = "shelters";
    public static final String USERS = "users";
    public static final String VISUAL_GUIDELINES_OBJECTS = "visual guidelines objects";
    public static final String VISUAL_GUIDELINES = "visual guidelines";
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
        //TODO: maybe we need to add a listener on the db to do stuff when 3rd party user delete
        // shelter or god erases public shelter directly from the db site.
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
                            addVisualGuides(shelterToAdd);
                        })
                        .addOnFailureListener(e -> {/*TODO*/}))
                .addOnFailureListener(command -> {/*TODO*/});

    }

    private void addVisualGuides(Shelter shelter) {
        for (ShelterVisualGuide shelterVisualGuide : shelter.visualStepsLiveData.getValue()) {

            firebase.collection(VISUAL_GUIDELINES).document(shelter.getId().toString()).
                    collection(VISUAL_GUIDELINES_OBJECTS).document(Integer.toString(shelterVisualGuide.getStepNumber())).set(shelterVisualGuide);
        }
//
//        firebase.collection(VISUAL_GUIDELINES).document(shelter.getId().toString()).set().addOnSuccessListener(new OnSuccessListener<Void>() {
//            @Override
//            public void onSuccess(Void unused) {
//                Log.i("test", "onSuccess");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.i("test", "fail");
//
//            }
//        });
//                collection(VISUAL_GUIDELINES_OBJECTS).document(Integer.toString(shelter.visualStepsLiveData.getValue().get(0).getStepNumber())).set(shelter.visualStepsLiveData.getValue().get(0));
    }

    public void addPublicShelter(Shelter shelterToAdd) {
        firebase.collection(SHELTERS).document(shelterToAdd.getId().toString())
                .set(new ShelterWrapper(shelterToAdd));
        addVisualGuides(shelterToAdd);
    }

    public void updateShelter(Shelter shelter) {
        //now its like addPublicShelter, we probably need to change it later.
        firebase.collection(SHELTERS).document(shelter.getId().toString())
                .set(new ShelterWrapper(shelter));


    }


    public Shelter getShelterById(UUID shelterId) {
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

    private void deleteShelterRefFromUser(UUID shelterId) {
        firebase.collection(USERS).document(manager.getCurrentUser()).collection("User's Shelters").document(shelterId.toString()).delete();
    }


    public boolean deletePrivateShelter(UUID shelterId) {
        firebase.collection(SHELTERS).document(shelterId.toString()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                userShelters.deleteShelter(shelterId);
                app.sendBroadcast(new Intent("Added"));
                deleteShelterRefFromUser(shelterId);
            }
        });
        return false;
    }

    public Task<List<Shelter>> getNearbyShelters(double longitude, double latitude, int radius) {

        final GeoLocation center = new GeoLocation(latitude, longitude);
        List<GeoQueryBounds> bounds = GeoFireUtils.getGeoHashQueryBounds(center, radius);
        final List<Task<QuerySnapshot>> tasks = new ArrayList<>();
        for (GeoQueryBounds b : bounds) {
            Query q = firebase.collection(SHELTERS)
                    .orderBy("geoHashForLocation")
                    .startAt(b.startHash)
                    .endAt(b.endHash);

            tasks.add(q.get());
        }
        // Collect all the query results together into a single list
        TaskCompletionSource<List<Shelter>> tcs = new TaskCompletionSource<>();
        return Tasks.whenAllComplete(tasks).continueWithTask(new Continuation<List<Task<?>>, Task<List<Shelter>>>() {
            @Override
            public Task<List<Shelter>> then(@NonNull @NotNull Task<List<Task<?>>> t) throws Exception {
                List<Shelter> matchingDocs = new ArrayList<>();

                for (Task<QuerySnapshot> task : tasks) {
                    QuerySnapshot snap = task.getResult();
                    for (DocumentSnapshot doc : snap.getDocuments()) {
                        double lat = doc.getDouble("lat");
                        double lng = doc.getDouble("lng");

                        // We have to filter out a few false positives due to GeoHash
                        // accuracy, but most will match
                        GeoLocation docLocation = new GeoLocation(lat, lng);
                        double distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center);
                        if (distanceInM <= radius) {
                            matchingDocs.add(new Shelter(doc.toObject(ShelterWrapper.class)));
                        }
                    }
                }
                tcs.setResult(matchingDocs);
                return tcs.getTask();
            }
        });


    }

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
