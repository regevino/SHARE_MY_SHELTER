package com.huji_postpc_avih.sharemyshelter.navigation.visualGuidesView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterVisualGuide;

import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

public class ImageStepAdapter extends RecyclerView.Adapter<ImageStepHolder> {


    private Shelter sh;
    private Context context;

    public ImageStepAdapter(Shelter shelter, LifecycleOwner owner, Context c)
    {
        context = c;
        sh = shelter;
        if (sh.visualStepsLiveData == null || sh.visualStepsLiveData.getValue() == null)
        {
            sh.retrieveVisuals();
            sh.visualStepsLiveData.observe(owner, new Observer<LinkedList<ShelterVisualGuide>>() {
                @Override
                public void onChanged(LinkedList<ShelterVisualGuide> shelterVisualGuides) {
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
        View contextView = inflater.inflate(R.layout.row_visual_step, parent, false);
        return new ImageStepHolder(contextView);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ImageStepHolder holder, int position) {

        if (sh.visualStepsLiveData.getValue() == null)
        {
            holder.setImage(context, null); //TODO default image
            return;
        }
        holder.setImage(context, sh.visualStepsLiveData.getValue().get(position).getImage());
    }

    @Override
    public int getItemCount() {
        if (sh.visualStepsLiveData.getValue() == null)
        {
            return 1;
        }
        return sh.visualStepsLiveData.getValue().size();
    }
}
