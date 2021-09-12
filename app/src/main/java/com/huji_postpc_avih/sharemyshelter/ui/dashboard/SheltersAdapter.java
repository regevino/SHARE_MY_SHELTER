package com.huji_postpc_avih.sharemyshelter.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;

class SheltersAdapter extends RecyclerView.Adapter<ShelterHolder> {

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

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}

