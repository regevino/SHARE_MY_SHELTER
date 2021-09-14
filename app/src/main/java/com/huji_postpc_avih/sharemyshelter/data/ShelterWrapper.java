package com.huji_postpc_avih.sharemyshelter.data;

import com.google.gson.Gson;

public class ShelterWrapper {
    private String location;
    private String id;
    private String ownerId;
    private String name;
    private Shelter.ShelterType type;

    public ShelterWrapper() {}

    public ShelterWrapper(Shelter shelter) {
        Gson gson = new Gson();
        location = gson.toJson(shelter.getLocation());
        id = gson.toJson(shelter.getId());
        ownerId = gson.toJson(shelter.getOwnerId());
        name = shelter.getName();
        type = shelter.getShelterType();
    }

    public String getLocation() {
        return location;
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
}
