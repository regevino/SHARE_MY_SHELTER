package com.huji_postpc_avih.sharemyshelter.navigation.visualGuidesView;

import android.content.Context;
import android.media.Image;
import android.view.View;
import android.widget.ImageView;

import com.huji_postpc_avih.sharemyshelter.R;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.appcompat.content.res.AppCompatResources.getDrawable;


public class ImageStepHolder extends RecyclerView.ViewHolder{

    private ImageView image;


    public ImageStepHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.visual_step_image);
    }

    public void setImage(Context c, Image image) {
        if(image == null)
        {
            this.image.setImageDrawable(getDrawable(c, R.drawable.common_full_open_on_phone));
        }
//        image.
//        this.image.setImageDrawable(.\BitmapFactory.);
    }
}
