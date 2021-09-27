package com.huji_postpc_avih.sharemyshelter.data;

import android.location.Location;

import com.firebase.geofire.GeoFireUtils;
import com.firebase.geofire.GeoLocation;
import com.google.gson.Gson;

import java.util.List;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Shelter {

    public enum ShelterType {PRIVATE, PUBLIC}

    public Shelter() {
    }

    public Shelter(Location loc, String name, ShelterType type, String ownerId) {
        if (loc != null) {
            this.lat = loc.getLatitude();
            this.lng = loc.getLongitude();
        }
        this.name = name;
        this.shelterType = type;
        this.id = UUID.randomUUID();
        this.ownerId = ownerId;
        this.isOpen = true;
        _visualStepsLiveData = new MutableLiveData<>();
        visualStepsLiveData = _visualStepsLiveData;

        geoHashForLocation = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));

    }

    private boolean isOpen;
    private double lat;
    private double lng;
    private String geoHashForLocation;
    private String name, ownerId;
    private UUID id;
    private ShelterType shelterType;

    //    private LinkedList<ShelterVisualGuide> visualSteps;

    private MutableLiveData<List<ShelterVisualGuide>> _visualStepsLiveData;
    public LiveData<List<ShelterVisualGuide>> visualStepsLiveData;

    public void set_visualStepsLiveData(List<ShelterVisualGuide> visualSteps) {
        this._visualStepsLiveData.setValue(visualSteps);
    }

    public void retrieveVisuals() {
        if (_visualStepsLiveData == null) {
            _visualStepsLiveData = new MutableLiveData<>();
            visualStepsLiveData = _visualStepsLiveData;
        }

    }

    private boolean validate() {
        return false;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
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

    public String getGeoHashForLocation() {
        return geoHashForLocation;
    }

    public void setOpen(boolean open) {
        isOpen = open;
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

    public void setLat(double lat) {
        geoHashForLocation = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));
        this.lat = lat;
    }

    public void setLng(double lng) {
        geoHashForLocation = GeoFireUtils.getGeoHashForLocation(new GeoLocation(lat, lng));
        this.lng = lng;
    }

    public void setGeoHashForLocation(String geoHashForLocation) {
        this.geoHashForLocation = geoHashForLocation;
    }

    public String toJson() {
        Gson gson = new Gson();
        return gson.toJson(this, Shelter.class);
    }

    public static Shelter fromJson(String jsonRepresentation) {
        Gson gson = new Gson();
        return gson.fromJson(jsonRepresentation, Shelter.class);
    }

//    public HashMap<String, Object> getData() {
//        HashMap<String, Object> shelterData = new HashMap<>();
//        shelterData.put("id", this.id);
//        shelterData.put("ownerId", this.ownerId);
//        shelterData.put("name", this.name);
//        shelterData.put("type", this.shelterType);
//        shelterData.put("geohash", geoHashForLocation);
//        shelterData.put("lat", lat);
//        shelterData.put("lng", lng);
//        return shelterData;
//    }

    public Shelter(ShelterWrapper wrapper) {
        Gson gson = new Gson();
        this.lat = wrapper.getLat();
        this.lng = wrapper.getLng();
        this.id = gson.fromJson(wrapper.getId(), UUID.class);
        this.ownerId = wrapper.getOwnerId();
        this.name = wrapper.getName();
        this.shelterType = wrapper.getType();
        this.isOpen = wrapper.isOpen();
        this.geoHashForLocation = wrapper.getGeoHashForLocation();
    }
}
