package com.huji_postpc_avih.sharemyshelter.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.navigation.Navigator;
import com.huji_postpc_avih.sharemyshelter.navigation.visualGuidesView.ImageStepAdapter;
import com.huji_postpc_avih.sharemyshelter.navigation.visualGuidesView.LinePagerIndicatorDecoration;

import java.util.UUID;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

public class ShelterPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_preview);

        TextView title = findViewById(R.id.Title);
        TextView description = findViewById(R.id.description);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String desc = intent.getStringExtra("description");
        UUID id = (UUID) intent.getSerializableExtra("id");

        title.setText(String.format("Name: %s", name));
        description.setText(String.format("Description: %s", desc));


        SheltersApp app = (SheltersApp) getApplicationContext();
        Shelter shelter = app.getDb().getShelterById(id);
        shelter.retrieveVisuals(this);
        ImageStepAdapter adapter = new ImageStepAdapter(shelter, this, this);
        RecyclerView rv = findViewById(R.id.visualsRecycler);
        rv.setNestedScrollingEnabled(false);
        rv.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rv.setHasFixedSize(true);
        rv.setAdapter(adapter);
        rv.addItemDecoration(new LinePagerIndicatorDecoration());
        new PagerSnapHelper().attachToRecyclerView(rv);
        Navigator navigator = new Navigator(this);
        String address = navigator.getAddress(shelter.getLng(), shelter.getLat());
        if (address != null) {
            TextView addressTextView = findViewById(R.id.addressTextView);
            addressTextView.setText("Address: ".concat(address));
            Log.i("testAddress", address);
////        Address address1 = navigator.getLatLng("20 hatayasim jerusalem");
//            Address address1 = navigator.getLatLng("הטייסים 20 ירושלים");
//            Log.i("testAddress", String.valueOf(address1.getLatitude()));
//            Log.i("testAddress", String.valueOf(address1.getLongitude()));
        }
    }
}