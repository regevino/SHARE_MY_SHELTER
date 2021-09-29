package com.huji_postpc_avih.sharemyshelter.data;

import android.graphics.Bitmap;

import java.util.UUID;

public class ShelterVisualGuideNoImage {

    private String description;
    private UUID id;
    private int stepNumber;

//    private Bitmap image;


    public ShelterVisualGuideNoImage(ShelterVisualGuide shelterVisualGuide) {
        this.id = shelterVisualGuide.getId();
        this.description = shelterVisualGuide.getDescription();
        this.stepNumber = shelterVisualGuide.getStepNumber();
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getDescription() {
        return description;
    }


    public int getStepNumber() {
        return stepNumber;
    }


    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}