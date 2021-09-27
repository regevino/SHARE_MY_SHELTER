package com.huji_postpc_avih.sharemyshelter.ui;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;

public class AllSheltersHolder extends RecyclerView.ViewHolder{
    private final TextView name;
    private final TextView range;
    private final TextView type;

    public AllSheltersHolder(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        range = itemView.findViewById(R.id.range);
        type = itemView.findViewById(R.id.type);
    }

    public TextView getName() {
        return name;
    }

    public TextView getRange() {
        return range;
    }

    public TextView getType() {
        return type;
    }
}
