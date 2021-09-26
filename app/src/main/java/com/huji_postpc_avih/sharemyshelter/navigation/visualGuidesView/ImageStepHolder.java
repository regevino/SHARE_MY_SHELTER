package com.huji_postpc_avih.sharemyshelter.navigation.visualGuidesView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.huji_postpc_avih.sharemyshelter.R;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ImageStepHolder extends RecyclerView.ViewHolder{

    private ImageView image;
    private TextView desc;


    public ImageStepHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.visual_step_image);
        desc = itemView.findViewById(R.id.navigation_visual_step_description);
    }

    public void setImage(Context c, Bitmap image) {
        this.image.setImageBitmap(image);
    }
    public void setDescription(Context c, String desc) {
        this.desc.setText(desc);
    }
}
