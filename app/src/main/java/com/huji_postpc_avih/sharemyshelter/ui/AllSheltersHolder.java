package com.huji_postpc_avih.sharemyshelter.ui;

import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;

public class AllSheltersHolder extends RecyclerView.ViewHolder{
    private final TextView name;
    private final Switch openCloseSwitch;

    public AllSheltersHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.step_number);
        openCloseSwitch = itemView.findViewById(R.id.openClose);
    }

    public TextView getName() {
        return name;
    }

    public Switch getOpenCloseSwitch() {
        return openCloseSwitch;
    }
}
