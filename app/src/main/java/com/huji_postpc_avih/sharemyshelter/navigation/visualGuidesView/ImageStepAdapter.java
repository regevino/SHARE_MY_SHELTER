package com.huji_postpc_avih.sharemyshelter.navigation.visualGuidesView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterVisualGuide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

public class ImageStepAdapter extends RecyclerView.Adapter<ImageStepHolder> {


    private final Shelter sh;
    private final Context context;

    public ImageStepAdapter(Shelter shelter, LifecycleOwner owner, Context c) {
        context = c;
        sh = shelter;
        if (sh.visualStepsLiveData == null || sh.visualStepsLiveData.getValue() == null) {
            sh.retrieveVisuals(context);
            sh.visualStepsLiveData.observe(owner, new Observer<List<ShelterVisualGuide>>() {
                @Override
                public void onChanged(List<ShelterVisualGuide> shelterVisualGuides) {
                    notifyDataSetChanged();
                }
            });
        }
    }

    @NonNull
    @NotNull
    @Override
    public ImageStepHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contextView = inflater.inflate(R.layout.row_visual_step_navigation, parent, false);
        return new ImageStepHolder(contextView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageStepHolder holder, int position) {

        if (sh.visualStepsLiveData.getValue() == null) {
            holder.setImage(context, null); //TODO default image
            return;
        }
        holder.setImage(context, sh.visualStepsLiveData.getValue().get(position).getImage());
        holder.setDescription(context, sh.visualStepsLiveData.getValue().get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        if (sh.visualStepsLiveData.getValue() == null) {
            return 1;
        }
        return sh.visualStepsLiveData.getValue().size();
    }
}
