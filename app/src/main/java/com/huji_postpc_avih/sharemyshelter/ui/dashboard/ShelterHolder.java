package com.huji_postpc_avih.sharemyshelter.ui.dashboard;

import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;

public class ShelterHolder extends RecyclerView.ViewHolder {
    private Switch typeSwitch;
    private TextView name;

    private ImageView deleteButton;

    public ShelterHolder(@NonNull View itemView) {
        super(itemView);
        typeSwitch = itemView.findViewById(R.id.typeSwitch);
        name = itemView.findViewById(R.id.step_number);
        deleteButton = itemView.findViewById(R.id.deleteShelterImage);
    }

    public Switch getTypeSwitch() {
        return typeSwitch;
    }

    public TextView getName() {
        return name;
    }

    public ImageView getDeleteButton() {
        return deleteButton;
    }
}
