package com.huji_postpc_avih.sharemyshelter.data;

import com.google.gson.Gson;

public class ShelterWrapper {
    private boolean isOpen;
    private double lat;
    private double lng;
    private String id;
    private String ownerId;
    private String name;
    private String description;
    private Shelter.ShelterType type;
    private String geoHashForLocation;

    public ShelterWrapper() {}

    public ShelterWrapper(Shelter shelter) {
        Gson gson = new Gson();
        lat = shelter.getLat();
        lng = shelter.getLng();
        id = gson.toJson(shelter.getId());
        this.description = shelter.getDescription();
        ownerId = shelter.getOwnerId();
        name = shelter.getName();
        type = shelter.getShelterType();
        isOpen = shelter.isOpen();
        geoHashForLocation = shelter.getGeoHashForLocation();
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    public String getId() {
        return id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getName() {
        return name;
    }

    public Shelter.ShelterType getType() {
        return type;
    }

    public String getGeoHashForLocation() {
        return geoHashForLocation;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean isOpen)
    {
        this.isOpen = isOpen;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
