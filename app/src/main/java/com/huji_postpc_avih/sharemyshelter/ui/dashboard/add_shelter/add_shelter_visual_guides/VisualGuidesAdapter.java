package com.huji_postpc_avih.sharemyshelter.ui.dashboard.add_shelter.add_shelter_visual_guides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.data.ShelterVisualGuide;
import com.huji_postpc_avih.sharemyshelter.ui.AllSheltersHolder;

public class VisualGuidesAdapter extends RecyclerView.Adapter<VisualGuidesHolder> {

    @NonNull
    @Override
    public VisualGuidesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contextView = inflater.inflate(R.layout.row_add_visual_step, parent, false);
        return new VisualGuidesHolder(contextView);
    }

    @Override
    public void onBindViewHolder(@NonNull VisualGuidesHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
