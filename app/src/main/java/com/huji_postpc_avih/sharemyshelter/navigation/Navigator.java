package com.huji_postpc_avih.sharemyshelter.navigation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class Navigator {
    private Activity activity;
    private Location location;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    /**
     * Finds the location of the user.
     * Asks for permission to access the user's location if needed.
     */
    public Location getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this.activity);

        if (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            fusedLocationClient.getLastLocation().addOnCompleteListener(task -> location = task.getResult());
        }
        return location;
    }
}
