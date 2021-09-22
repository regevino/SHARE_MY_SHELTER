package com.huji_postpc_avih.sharemyshelter.ui.dashboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;

import java.util.ArrayList;

class SheltersAdapter extends RecyclerView.Adapter<ShelterHolder> {
    private ArrayList<Shelter> userShelters;
    private Context context;

    public void setUserShelters(ArrayList<Shelter> userShelters) {
        this.userShelters = userShelters;
    }

    public void setContext(Context c) {
        context = c;
    }

    @NonNull
    @Override
    public ShelterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contextView = inflater.inflate(R.layout.row_shelter, parent, false);
        return new ShelterHolder(contextView);
    }

    @Override
    public void onBindViewHolder(@NonNull ShelterHolder holder, int position) {
        Shelter shelter = userShelters.get(position);
        Switch typeSwitch = holder.getTypeSwitch();
        ImageView deleteButton = holder.getDeleteButton();


        holder.getName().setText(shelter.getName());
        typeSwitch.setChecked(shelter.isOpen());
        typeSwitch.setText(typeSwitch.isChecked() ? "Open" : "Closed");
        typeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Switch typeSwitch = holder.getTypeSwitch();
                shelter.setOpen(typeSwitch.isChecked());
                typeSwitch.setText(typeSwitch.isChecked() ? "Open" : "Closed");
                SheltersApp app = (SheltersApp) context.getApplicationContext();
                ShelterDB db = app.getDb();
                db.updateShelter(shelter);

            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SheltersApp app = (SheltersApp) context.getApplicationContext();
                ShelterDB db = app.getDb();
                db.deletePrivateShelter(shelter.getId());
            }
        });


    }

    @Override
    public int getItemCount() {
        return userShelters.size();
    }
}

