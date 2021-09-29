package com.huji_postpc_avih.sharemyshelter.data;

public class ShelterVisualGuideNoImage {

    private String description;
    private String id;
    private int stepNumber;

//    private Bitmap image;

    public ShelterVisualGuideNoImage()
    {}

    public ShelterVisualGuideNoImage(ShelterVisualGuide shelterVisualGuide) {
        this.id = shelterVisualGuide.getId().toString();
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


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}