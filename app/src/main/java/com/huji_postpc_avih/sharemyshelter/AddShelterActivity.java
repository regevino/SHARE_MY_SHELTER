package com.huji_postpc_avih.sharemyshelter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class AddShelterActivity extends AppCompatActivity {
    private FusedLocationProviderClient fusedLocationClient;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shelter);

        Spinner dropdown = findViewById(R.id.locationSpinner);
        String[] items = new String[]{"Current Location", "Choose from Map"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = dropdown.getSelectedItem().toString();
                if (choice.equals("Current Location")) {
                    getCurrentLocation();
                    if (location != null) {
                        Toast.makeText(AddShelterActivity.this, location.toString(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    //TODO let user enter address

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        EditText name = findViewById(R.id.name);
        Switch isPrivate = findViewById(R.id.shelterMode);
        isPrivate.setOnClickListener(v -> {
            if (isPrivate.getText().toString().equals("Public")) {
                isPrivate.setText("Private");
            } else {
                isPrivate.setText("Public");
            }
        });

        ImageButton addShelterButton = findViewById(R.id.addShelter);
        addShelterButton.setOnClickListener(v -> {
            if (location != null) {
                if (isPrivate.isChecked()) {
                    Shelter newShelter = new Shelter(this, location, name.getText().toString(), Shelter.ShelterType.PRIVATE);
                    SheltersApp app = (SheltersApp) getApplicationContext();
                    ShelterDB db = app.getDb();
                    String aString="JUST_A_TEST_STRING";
                    String ownerId = UUID.nameUUIDFromBytes(aString.getBytes()).toString();
                    db.addPrivateShelter(newShelter, UUID.fromString(ownerId));
                }
                else
                {
                    Shelter newShelter = new Shelter(this, location, name.getText().toString(), Shelter.ShelterType.PUBLIC);
                }
            }
        });
    }

    /**
     * Finds the location of the user.
     * Asks for permission to access the user's location if needed.
     */
    private void getCurrentLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddShelterActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        else
        {
            fusedLocationClient.getLastLocation().addOnCompleteListener(task -> location = task.getResult());
        }
    }
}