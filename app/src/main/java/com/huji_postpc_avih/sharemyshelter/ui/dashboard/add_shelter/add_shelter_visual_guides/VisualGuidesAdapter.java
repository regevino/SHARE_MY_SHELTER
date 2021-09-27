package com.huji_postpc_avih.sharemyshelter.ui.dashboard.add_shelter.add_shelter_visual_guides;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.data.ShelterVisualGuide;
import com.huji_postpc_avih.sharemyshelter.ui.AllSheltersHolder;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.ArrayList;
import java.util.List;

public class VisualGuidesAdapter extends RecyclerView.Adapter<VisualGuidesHolder> {

    private List<ShelterVisualGuide> visualGuidesList;
    private FragmentManager parentFragmentManager;

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
        ShelterVisualGuide visualGuide = visualGuidesList.get(position);
        holder.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).show(parentFragmentManager);

            }
        });

        holder.addVisualGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmptyVisualGuide();
            }
        });

        holder.stepNumber.setText("step: ".concat(Integer.toString(visualGuide.getStepNumber())));


    }

    private void addEmptyVisualGuide() {

        ShelterVisualGuide example = new ShelterVisualGuide();
        example.setDescription("Example Description");
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(200, 300, conf);
        example.setImage(bmp);
        example.setStepNumber(visualGuidesList.size() + 1);
        visualGuidesList.add(example);
        notifyItemInserted(visualGuidesList.size());

    }

    @Override
    public int getItemCount() {
        return this.visualGuidesList.size();
    }

    public void setList(List<ShelterVisualGuide> list) {
        this.visualGuidesList = list;
    }

    public void setParentFragmentManager(FragmentManager manager) {
        this.parentFragmentManager = manager;
    }
}
