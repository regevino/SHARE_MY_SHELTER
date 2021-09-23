package com.huji_postpc_avih.sharemyshelter.navigation;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;

public class Navigator {
    private Activity activity;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

    /**
     * Finds the location of the user.
     * Asks for permission to access the user's location if needed.
     */
    public void setLocationAndAddShelter(FusedLocationProviderClient fusedLocationClient, Shelter shelter) {
        while (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(location -> {
            if (location == null) {
                Toast.makeText(activity, "Failed to add new shelter :(", Toast.LENGTH_LONG).show();
                return;
            }
            SheltersApp app = (SheltersApp) activity.getApplicationContext();
            ShelterDB db = app.getDb();
            shelter.setLocation(location);
            if (shelter.getShelterType() == Shelter.ShelterType.PRIVATE) {
                db.addPrivateShelter(shelter);
            }
            else
            {
                db.addPublicShelter(shelter);
            }
            Toast.makeText(activity, "Shelter added successfully!", Toast.LENGTH_LONG).show();
        });
    }
}
