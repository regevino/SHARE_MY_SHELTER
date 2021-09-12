package com.huji_postpc_avih.sharemyshelter.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;

import java.util.LinkedList;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;

public class Shelter {

    public enum ShelterType {PRIVATE, PUBLIC}

    public Shelter(Context c, Location loc, String name, ShelterType type) {
        this.location = loc;
        this.name = name;
        this.shelterType = type;
        this.id = UUID.randomUUID();

        // Get user's id from shared preferences:
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
        String userId = sp.getString("ownerId", null);
        Gson gson = new Gson();
        if (userId != null) {
            this.ownerId = gson.fromJson(userId, UUID.class);
        }
        else
        {
            this.ownerId = UUID.randomUUID();
            String ownerIdToJson = gson.toJson(this.ownerId, UUID.class);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("ownerId", ownerIdToJson);
            editor.apply();
        }
    }

    private final Location location;
    private String name;
    private final UUID id, ownerId;
    private ShelterType shelterType;
    private LinkedList<ShelterVisualGuide> visualSteps;
    private MutableLiveData<LinkedList<ShelterVisualGuide>> _visualStepsLiveData;

    public LiveData<LinkedList<ShelterVisualGuide>> visualStepsLiveData;

    public void retrieveVisuals() {

    }

    private boolean validate() {
        return false;
    }

    public Location getLocation() {
        return location;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public UUID getOwnerId() {
        return ownerId;
    }

    public ShelterType getShelterType() {
        return shelterType;
    }
}
