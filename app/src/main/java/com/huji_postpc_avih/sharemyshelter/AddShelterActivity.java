package com.huji_postpc_avih.sharemyshelter;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;
import com.huji_postpc_avih.sharemyshelter.navigation.Navigator;

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
                    if (location != null) {
                        Toast.makeText(AddShelterActivity.this, location.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //TODO let user enter address

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
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
//            //TODO: only for testing.
//            if (location == null) {
//                location = new Location("idan");
//                location.setLongitude(35.217018d);
//                location.setLatitude(31.771959d);
//                Toast.makeText(this, "location is null,\n so uploading FAKE shelter", Toast.LENGTH_SHORT).show();
//
//            }
            SheltersApp app = (SheltersApp) getApplicationContext();
            ShelterDB db = app.getDb();
            String currentUser = db.getManager().getCurrentUser();
            Shelter newShelter;
            if (isPrivate.isChecked()) {
                newShelter = new Shelter(this, null, name.getText().toString(), Shelter.ShelterType.PRIVATE, currentUser);
            } else {
                newShelter = new Shelter(this, null, name.getText().toString(), Shelter.ShelterType.PUBLIC, null);
            }
            Navigator navigator = new Navigator(AddShelterActivity.this);
            navigator.getCurrentLocation().addOnSuccessListener(location -> {
                if (location == null) {
                    Toast.makeText(this, "Failed to add new shelter :(", Toast.LENGTH_LONG).show();
                    return;
                }

                newShelter.setLocation(location);
                if (newShelter.getShelterType() == Shelter.ShelterType.PRIVATE) {
                    db.addPrivateShelter(newShelter);
                }
                else
                {
                    db.addPublicShelter(newShelter);
                }
                Toast.makeText(this, "Shelter added successfully!", Toast.LENGTH_LONG).show();
            });
            finish();
        });
    }
}