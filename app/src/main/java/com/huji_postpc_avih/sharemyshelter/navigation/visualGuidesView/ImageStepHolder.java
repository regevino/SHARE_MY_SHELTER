package com.huji_postpc_avih.sharemyshelter.navigation.visualGuidesView;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.huji_postpc_avih.sharemyshelter.R;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ImageStepHolder extends RecyclerView.ViewHolder{

    private ImageView image, fullScreenImage;
    private TextView desc;
    private boolean isImageFullScreen;


    public ImageStepHolder(@NonNull @NotNull View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.visual_step_image);
        isImageFullScreen = false;

        desc = itemView.findViewById(R.id.navigation_visual_step_description);
    }
    public void setFullScreenImageView(ImageView v)
    {
        fullScreenImage = v;
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isImageFullScreen) {
                    isImageFullScreen =false;
                    ScaleAnimation animation = new ScaleAnimation(1.0f, 0.001f, 1.0f, 0.001f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.1f);
                    animation.setDuration(300);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            fullScreenImage.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    fullScreenImage.startAnimation(animation);
                }else{
                    isImageFullScreen =true;
                    fullScreenImage.setOnClickListener((View vv) -> image.callOnClick());
                    fullScreenImage.setImageDrawable(image.getDrawable());
                    ScaleAnimation animation = new ScaleAnimation(0.001f, 1.0f, 0.001f, 1.0f, Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.1f);
                    animation.setDuration(300);
                    animation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            fullScreenImage.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    fullScreenImage.startAnimation(animation);
                }
            }
        });
    }
    public void setImage(Context c, Bitmap image) {
        this.image.setImageBitmap(image);
    }
    public void setDescription(Context c, String desc) {
        this.desc.setText(desc);
    }
}
