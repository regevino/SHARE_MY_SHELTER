package com.huji_postpc_avih.sharemyshelter.ui.dashboard.add_shelter.add_shelter_visual_guides;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.data.ShelterVisualGuide;

public class VisualGuidesHolder extends RecyclerView.ViewHolder {

    public ImageView addImage;
    public TextView stepNumber;
    public ImageView addVisualGuide;
    public EditText imageDescription;

    public VisualGuidesHolder(@NonNull View itemView) {
        super(itemView);
        addImage = itemView.findViewById(R.id.add_image);
        stepNumber = itemView.findViewById(R.id.step_number);
        addVisualGuide = itemView.findViewById(R.id.add_visual_guide);
        imageDescription = itemView.findViewById(R.id.description_edit_text);
    }
}
