package com.huji_postpc_avih.sharemyshelter.navigation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.Task;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;

public class Navigator {
    private Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    /**
     * Finds the location of the user.
     * Asks for permission to access the user's location if needed.
     * @return Task that gets the location.
     */
    public Task<Location> getCurrentLocation(FusedLocationProviderClient fusedLocationClient) {
        while (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        return fusedLocationClient.getLastLocation();
    }
}
