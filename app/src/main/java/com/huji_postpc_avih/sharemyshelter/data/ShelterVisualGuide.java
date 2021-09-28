package com.huji_postpc_avih.sharemyshelter.data;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.UUID;

public class ShelterVisualGuide {

    private String description;
    private UUID id;

//    public byte[] getImageByteArray() {
//        return imageByteArray;
//    }
//
//    public void setImageByteArray(byte[] imageByteArray) {
//        this.imageByteArray = imageByteArray;
//    }

    private Bitmap image;
    //    private byte[] imageByteArray;
    //    private ArrayList<Byte> imageByteList;
    private int stepNumber;
//    private int width;
//    private int height;
//    private String path;


    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Bitmap bitmap) {
//        this.image = image;
//        width = image.getWidth();
//        height = image.getHeight();
//
//        int size = image.getRowBytes() * image.getHeight();
//        ByteBuffer byteBuffer = ByteBuffer.allocate(size);
//        image.copyPixelsToBuffer(byteBuffer);
//        imageByteArray = byteBuffer.array();
        image = bitmap;
    }

    public void setStepNumber(int stepNumber) {
        this.stepNumber = stepNumber;
    }

    public String getDescription() {
        return description;
    }

    public Bitmap getImage() {
//        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        return image;
    }

    public int getStepNumber() {
        return stepNumber;
    }

//    public int getWidth() {
//        return width;
//    }
//
//    public void setWidth(int width) {
//        this.width = width;
//    }
//
//    public int getHeight() {
//        return height;
//    }
//
//    public void setHeight(int height) {
//        this.height = height;
//    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}