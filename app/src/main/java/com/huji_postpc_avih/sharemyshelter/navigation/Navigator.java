package com.huji_postpc_avih.sharemyshelter.navigation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.Task;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import com.google.maps.model.Unit;
import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;

public class Navigator {
    private static final String TAG = "NAVIGATOR";
    private Activity activity;
    private Context context;

    public Navigator(Activity activity) {
        this.activity = activity;
    }

//    public Navigator(Context c) {
//        this.context = c;
//    }

    /**
     * Finds the location of the user.
     * Asks for permission to access the user's location if needed.
     *
     * @return Task that gets the location.
     */
    public Task<Location> getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        while (ActivityCompat.checkSelfPermission(this.activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        return fusedLocationClient.getLastLocation();
    }
//    public Task<Location> getCurrentLocation() {
//        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
//        while (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
//        }
//        return fusedLocationClient.getLastLocation();
//    }

    public DirectionsResult getDirections(LatLng start, LatLng end) {
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(activity.getString(R.string.maps_API_key))
                .build();
        DirectionsApiRequest req = DirectionsApi.newRequest(context)
                .destination(end)
                .origin(start)
                .alternatives(false)
                .units(Unit.METRIC)
                .mode(TravelMode.WALKING)
                .departureTimeNow();
        try {
            return req.await();
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }
        return null;
    }

    public Shelter getNearestShelter(double startLng, double startLat, List<Shelter> shelters, long deadline) {

        for (Shelter sh : shelters) {
            DirectionsResult directions = getDirections(new LatLng(startLat, startLng), new LatLng(sh.getLat(), sh.getLng()));
            DirectionsRoute[] routes = directions.routes;
            if (routes == null) {
                break;
            }
            DirectionsLeg[] legs = routes[0].legs;
            long totalDurationMs = 0;
            for (DirectionsLeg leg : legs) {
                totalDurationMs += 1000 * leg.duration.inSeconds;
            }
            if (new Date().getTime() + totalDurationMs < deadline) {
                return sh;
            }

        }
        return null;
    }

    public String getAddress(double lng, double lat) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(activity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            return address + '\n' + city + '\n' + state;
        } catch (IOException e) {
            return null;
        }
    }
}
