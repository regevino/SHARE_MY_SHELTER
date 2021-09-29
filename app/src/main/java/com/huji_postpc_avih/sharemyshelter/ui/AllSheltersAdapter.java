package com.huji_postpc_avih.sharemyshelter.ui;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;
import com.huji_postpc_avih.sharemyshelter.navigation.Navigator;

import java.util.ArrayList;

public class AllSheltersAdapter extends RecyclerView.Adapter<AllSheltersHolder> {
    private ArrayList<Shelter> shelters;
    private ShelterDB shelterDB;
    private FragmentActivity activity;

    public void setShelters(ArrayList<Shelter> shelters) {
        this.shelters = shelters;
    }

    public void setShelterDB(ShelterDB shelterDB) {
        this.shelterDB = shelterDB;
    }

    public AllSheltersAdapter(FragmentActivity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public AllSheltersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contextView = inflater.inflate(R.layout.row_all_shelters, parent, false);
        return new AllSheltersHolder(contextView);
    }

    @Override
    public void onBindViewHolder(@NonNull AllSheltersHolder holder, int position) {
        Shelter shelter = shelterDB.getAllShelters().get(position);
        TextView name = holder.getName();
        TextView range = holder.getRange();
        TextView openCloseSwitch = holder.getType();

        name.setText(shelter.getName());
        name.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ShelterPreviewActivity.class);
            intent.putExtra("name", shelter.getName());
            intent.putExtra("description", shelter.getDescription());
            intent.putExtra("id", shelter.getId());
            activity.startActivity(intent);
        });

        Navigator navigator = new Navigator(activity);
        navigator.getCurrentLocation().addOnSuccessListener(location -> {
            if (location == null) {
                Toast.makeText(activity, "Failed to get current location :(", Toast.LENGTH_LONG).show();
            } else {
                Location sheltersLocation = new Location("");
                sheltersLocation.setLatitude(shelter.getLat());
                sheltersLocation.setLongitude(shelter.getLng());

                float distanceInMeters = sheltersLocation.distanceTo(location);
                String rangeText;
                if (distanceInMeters >= 1000) {
                    String s = Float.toString(distanceInMeters / 1000);
                    rangeText = String.format("%.1f", distanceInMeters / 1000) + " km";
                } else {
                    String s = Float.toString(distanceInMeters);
                    rangeText = String.format("%.1f", distanceInMeters) + " m";
                }
                range.setText(rangeText);
            }
        });

        openCloseSwitch.setText(shelter.getShelterType() == Shelter.ShelterType.PRIVATE ? "Private" : "Public");
    }

    @Override
    public int getItemCount() {
        return shelters.size();
    }
}
