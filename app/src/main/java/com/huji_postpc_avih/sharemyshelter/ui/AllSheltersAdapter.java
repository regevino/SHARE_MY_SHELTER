package com.huji_postpc_avih.sharemyshelter.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;
import com.huji_postpc_avih.sharemyshelter.ui.dashboard.ShelterHolder;

import java.util.ArrayList;

public class AllSheltersAdapter extends RecyclerView.Adapter<AllSheltersHolder>{
    private ArrayList<Shelter> shelters;
    private ShelterDB shelterDB;

    public void setShelters(ArrayList<Shelter> shelters) {
        this.shelters = shelters;
    }

    public void setShelterDB(ShelterDB shelterDB) {
        this.shelterDB = shelterDB;
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
        Shelter shelter = shelterDB.getUserShelters().get(position);TextView name = holder.getName();
        Switch openCloseSwitch = holder.getOpenCloseSwitch();

        holder.getName().setText(shelter.getName());
        openCloseSwitch.setChecked(shelter.isOpen());
        openCloseSwitch.setText(openCloseSwitch.isChecked() ? "Open" : "Closed");
        openCloseSwitch.setOnClickListener(view -> {
            Switch typeSwitch = holder.getOpenCloseSwitch();
            shelter.setOpen(typeSwitch.isChecked());
            typeSwitch.setText(typeSwitch.isChecked() ? "Open" : "Closed");
            shelterDB.updateShelter(shelter);
        });
    }

    @Override
    public int getItemCount() {
        return shelters.size();
    }
}
