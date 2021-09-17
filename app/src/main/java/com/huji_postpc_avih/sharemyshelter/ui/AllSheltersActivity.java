package com.huji_postpc_avih.sharemyshelter.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;

import java.util.ArrayList;

public class AllSheltersActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_shelters);

        // Get list of current user's shelters:
        SheltersApp app = (SheltersApp) getApplicationContext();
        ArrayList<Shelter> allShelters = app.getDb().getAllShelters();

        // Set RecyclerView of shelters, adapter etc.
        RecyclerView sheltersList = findViewById(R.id.privateShelters);
        AllSheltersAdapter adapter = new AllSheltersAdapter();
        adapter.setShelters(allShelters);
        sheltersList.setAdapter(adapter);
        sheltersList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

    }
}