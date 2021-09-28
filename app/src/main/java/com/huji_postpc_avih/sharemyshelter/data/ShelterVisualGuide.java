package com.huji_postpc_avih.sharemyshelter.data;

import android.graphics.Bitmap;

public class ShelterVisualGuide {

    private String description;
    private Bitmap image;
    private int stepNumber;


    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {

        return image;
    }

    public int getStepNumber() {
        return stepNumber;
    }
}