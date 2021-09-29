package com.huji_postpc_avih.sharemyshelter.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.navigation.Navigator;
import com.huji_postpc_avih.sharemyshelter.navigation.visualGuidesView.ImageStepAdapter;
import com.huji_postpc_avih.sharemyshelter.navigation.visualGuidesView.LinePagerIndicatorDecoration;

import java.util.UUID;

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
        Navigator navigator = new Navigator(this);
        String address = navigator.getAddress(shelter.getLng(), shelter.getLat());
        Log.i("testAddress", address);

    }
}