package com.huji_postpc_avih.sharemyshelter.data;

import android.content.Context;
import android.location.Location;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Shelter {

    public enum ShelterType {PRIVATE, PUBLIC}

    public Shelter() {
        location = null;
        id = null;
        ownerId = null;
        name = null;
        shelterType = ShelterType.PUBLIC;
        this.isOpen = false;
    }

    public Shelter(Context c, Location loc, String name, ShelterType type, String ownerId) {
        this.location = loc;
        this.name = name;
        this.shelterType = type;
        this.id = UUID.randomUUID();
        this.ownerId = ownerId;
        this.isOpen = true;
        _visualStepsLiveData = new MutableLiveData<>();
        visualStepsLiveData = _visualStepsLiveData;

        // Get user's id from shared preferences:
//        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(c);
//        String userId = sp.getString("ownerId", null);
//        Gson gson = new Gson();
//        if (userId != null) {
//            this.ownerId = gson.fromJson(userId, UUID.class);
//        }
//        else
//        {
//            this.ownerId = UUID.randomUUID();
//            String ownerIdToJson = gson.toJson(this.ownerId, UUID.class);
//            SharedPreferences.Editor editor = sp.edit();
//            editor.putString("ownerId", ownerIdToJson);
//            editor.apply();
//        }
    }

    private boolean isOpen;
    private Location location;
    private String name, ownerId;
    private UUID id;
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

    public String getOwnerId() {
        return ownerId;
    }

    public ShelterType getShelterType() {
        return shelterType;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public void setShelterType(ShelterType shelterType) {
        this.shelterType = shelterType;
    }

    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this, Shelter.class);
    }

    public static Shelter fromJson(String jsonRepresentation) {
        Gson gson = new Gson();
        return gson.fromJson(jsonRepresentation, Shelter.class);
    }

    public HashMap<String, Object> getData() {
        HashMap<String, Object> shelterData = new HashMap<>();
        shelterData.put("location", this.location);
        shelterData.put("id", this.id);
        shelterData.put("ownerId", this.ownerId);
        shelterData.put("name", this.name);
        shelterData.put("type", this.shelterType);
        return shelterData;
    }

    public Shelter(ShelterWrapper wrapper) {
        Gson gson = new Gson();
        this.location = gson.fromJson(wrapper.getLocation(), Location.class);
        this.id = gson.fromJson(wrapper.getId(), UUID.class);
        this.ownerId = wrapper.getOwnerId();
        this.name = wrapper.getName();
        this.shelterType = wrapper.getType();
        this.isOpen = wrapper.isOpen();
    }
}
