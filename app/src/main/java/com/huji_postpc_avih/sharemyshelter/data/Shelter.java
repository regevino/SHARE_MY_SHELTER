package com.huji_postpc_avih.sharemyshelter.data;

import android.content.Context;
import android.location.Location;

import java.util.LinkedList;
import java.util.UUID;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Shelter {

    enum ShelterType {PRIVATE, PUBLIC}

    Shelter(Context c, Location loc, String name, ShelterType type) {

    }

    private Location location;
    private String name;
    private UUID id, ownerId;
    private ShelterType shelterType;
    private LinkedList<ShelterVisualGuide> visualSteps;
    private MutableLiveData<LinkedList<ShelterVisualGuide>> _visualStepsLiveData;

    public LiveData<LinkedList<ShelterVisualGuide>> visualStepsLiveData;

    public void retrieveVisuals() {

    }
}
