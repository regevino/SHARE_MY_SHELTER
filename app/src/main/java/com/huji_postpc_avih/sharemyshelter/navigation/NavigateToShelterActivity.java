package com.huji_postpc_avih.sharemyshelter.navigation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Dash;
import com.google.android.gms.maps.model.Gap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;
import com.huji_postpc_avih.sharemyshelter.navigation.visualGuidesView.ImageStepAdapter;
import com.huji_postpc_avih.sharemyshelter.navigation.visualGuidesView.LinePagerIndicatorDecoration;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class NavigateToShelterActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String EXTRA_KEY_SHELTER_ID = "target_shelter";
    public static final String EXTRA_KEY_SELF_LOCATION = "start_position";
    public static final String EXTRA_KEY_END_ALERT_TIME = "end_alert_time";
    private Shelter targetShelter;
    private double startLong;
    private double startLat;
    private Date arrivalDeadline;
    private ShelterDB db;
    private static final String TAG = "NavigateToShelter";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_navigate_to_shelter);

        db = ((SheltersApp) getApplication()).getDb();

        targetShelter = db.getShelterById(UUID.fromString(getIntent().getStringExtra(EXTRA_KEY_SHELTER_ID)));
        double[] loc = getIntent().getDoubleArrayExtra(EXTRA_KEY_SELF_LOCATION);
        startLong = loc[0];
        startLat = loc[1];
        arrivalDeadline = new Date(getIntent().getLongExtra(EXTRA_KEY_END_ALERT_TIME, new Date().getTime()));

        RecyclerView imagesRecycler = findViewById(R.id.visual_guides_recycler);
        ImageStepAdapter imageStepAdapter = new ImageStepAdapter(targetShelter, this, this);

        imagesRecycler.setNestedScrollingEnabled(false);
        imagesRecycler.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imagesRecycler.setHasFixedSize(true);
        imagesRecycler.setAdapter(imageStepAdapter);
        imagesRecycler.addItemDecoration(new LinePagerIndicatorDecoration());
        new PagerSnapHelper().attachToRecyclerView(imagesRecycler);

        TextView timerText = findViewById(R.id.navigate_to_shelter_timer_text);

        new CountDownTimer(arrivalDeadline.getTime() - new Date().getTime(), 1000) {

            public void onTick(long millisUntilFinished) {
                timerText.setText("Alert Time: " + millisUntilFinished / 1000 + "s");
            }

            public void onFinish() {
                timerText.setText("Find Cover!");
            }
        }.start();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.navigate_to_shelter_map);
        mapFragment.getMapAsync(this);


    }

    @Override
    public void onMapReady(@NonNull @NotNull GoogleMap googleMap) {
        GoogleMap mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                {
                    finish();
                }
            }
        }
        mMap.setMyLocationEnabled(true);
        mMap.setBuildingsEnabled(true);
        mMap.setIndoorEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.setTrafficEnabled(false);

        LatLng start = new LatLng(startLat, startLong);
//        mMap.addMarker(new MarkerOptions().position(start).title("Nati HaMefanek"));

        LatLng target = new LatLng(targetShelter.getLat(), targetShelter.getLng());
        mMap.addMarker(new MarkerOptions()
                .position(target)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                .title(targetShelter.getName()));

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList<>();

        DirectionsResult res = new Navigator(this).getDirections(
                new com.google.maps.model.LatLng(start.latitude, start.longitude),
                new com.google.maps.model.LatLng(target.latitude, target.longitude));

        //Loop through legs and steps to get encoded polylines of each step
        if (res.routes != null && res.routes.length > 0) {
            DirectionsRoute route = res.routes[0];

            if (route.legs != null) {
                for (int i = 0; i < route.legs.length; i++) {
                    DirectionsLeg leg = route.legs[i];
                    if (leg.steps != null) {
                        for (int j = 0; j < leg.steps.length; j++) {
                            DirectionsStep step = leg.steps[j];
                            if (step.steps != null && step.steps.length > 0) {
                                for (int k = 0; k < step.steps.length; k++) {
                                    DirectionsStep step1 = step.steps[k];
                                    EncodedPolyline points1 = step1.polyline;
                                    if (points1 != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                        for (com.google.maps.model.LatLng coord1 : coords1) {
                                            path.add(new LatLng(coord1.lat, coord1.lng));
                                        }
                                    }
                                }
                            } else {
                                EncodedPolyline points = step.polyline;
                                if (points != null) {
                                    //Decode polyline and add points to list of route coordinates
                                    List<com.google.maps.model.LatLng> coords = points.decodePath();
                                    for (com.google.maps.model.LatLng coord : coords) {
                                        path.add(new LatLng(coord.lat, coord.lng));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().
                    addAll(path).
                    endCap(new RoundCap()).
                    color(Color.RED).
                    width(5).
                    pattern(Arrays.asList(new Gap(2F), new Dash(2F)));
            mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(start));
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(new LatLngBounds.Builder().include(start).include(target).build(), 50));
    }
}