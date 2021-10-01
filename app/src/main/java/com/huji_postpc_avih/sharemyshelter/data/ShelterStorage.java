package com.huji_postpc_avih.sharemyshelter.data;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class ShelterStorage {
    private static final int MB = 1024 * 1024;
    private static ShelterStorage me = null;
    private FirebaseStorage firebaseStorage = null;
    private SheltersApp app;


    private ShelterStorage(Context c) {
        app = (SheltersApp) c.getApplicationContext();
        firebaseStorage = app.getStorageRef();
//        firebase = app.getFirebaseApp();
//        manager = app.getUserManager();


    }

    public static ShelterStorage getInstance(Context c) {
        if (me == null) {
            me = new ShelterStorage(c);
        }
        return me;
    }

    public void uploadBitmapToImages(Bitmap bitmap, String path) {
//        byte[] bytes = bitmapToBytesArray(bitmap);
        StorageReference reference = firebaseStorage.getReference();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();

        reference.child("images/".concat(path)).putBytes(bytes);
    }

    public Task<byte[]> downloadBitmapFromImages(String path) {
        StorageReference reference = firebaseStorage.getReference();
        Task<byte[]> task = reference.child("images/".concat(path)).getBytes(10 * MB);
        return task;
    }

    public Task<Void> deleteImage(String path) {
        StorageReference reference = firebaseStorage.getReference();
        Task<Void> task = reference.child("images/".concat(path)).delete();
        return task;
    }

    private byte[] bitmapToBytesArray(Bitmap bitmap) {
        byte[] bitmapdata = {};
        try {
            File f = new File("path");
            f.createNewFile();


//Convert bitmap to byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {


        }
        return bitmapdata;
    }
}
