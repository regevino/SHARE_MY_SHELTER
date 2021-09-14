package com.huji_postpc_avih.sharemyshelter.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;

import java.util.ArrayList;

class SheltersAdapter extends RecyclerView.Adapter<ShelterHolder> {
    private ArrayList<Shelter> userShelters;

    public void setUserShelters(ArrayList<Shelter> userShelters) {
        this.userShelters = userShelters;
    }

    @NonNull
    @Override
    public ShelterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contextView = inflater.inflate(R.layout.row_shelter, parent, false);
        return new ShelterHolder(contextView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelterHolder holder, int position) {
        Shelter shelter = userShelters.get(position);
        holder.getName().setText(shelter.getName());
        Switch typeSwitch = holder.getTypeSwitch();
        typeSwitch.setChecked(shelter.getShelterType().equals(Shelter.ShelterType.PRIVATE));
        typeSwitch.setText(typeSwitch.isChecked() ? "Private" : "Public");
    }

    @Override
    public int getItemCount() {
        return userShelters.size();
    }
}

