package com.huji_postpc_avih.sharemyshelter.ui.dashboard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;
import com.huji_postpc_avih.sharemyshelter.ui.ShelterPreviewActivity;

import java.util.ArrayList;

class SheltersAdapter extends RecyclerView.Adapter<ShelterHolder> {
    private ShelterDB shelterDB;
    private Context context;

    public void setShelterDB(ShelterDB shelterDB) {
        this.shelterDB = shelterDB;
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
        Shelter shelter = shelterDB.getUserShelters().get(position);
        Switch typeSwitch = holder.getTypeSwitch();
        ImageView deleteButton = holder.getDeleteButton();

        if (getItemCount() == 0) {
            ((Activity) context).findViewById(R.id.youHaveNoShelters).setVisibility(View.VISIBLE);
        }else{
            ((Activity) context).findViewById(R.id.youHaveNoShelters).setVisibility(View.INVISIBLE);
        }


        TextView name = holder.getName();
        name.setText(shelter.getName());
        name.setOnClickListener(v -> {
            Intent intent = new Intent(context, ShelterPreviewActivity.class);
            intent.putExtra("name", shelter.getName());
            intent.putExtra("description", shelter.getDescription());
            intent.putExtra("id", shelter.getId());
            context.startActivity(intent);
        });

        typeSwitch.setChecked(shelter.isOpen());
        typeSwitch.setText(typeSwitch.isChecked() ? "Open" : "Closed");
        typeSwitch.setOnClickListener(view -> {
            Switch typeSwitch1 = holder.getTypeSwitch();
            shelter.setOpen(typeSwitch1.isChecked());
            typeSwitch1.setText(typeSwitch1.isChecked() ? "Open" : "Closed");
            SheltersApp app = (SheltersApp) context.getApplicationContext();
            ShelterDB db = app.getDb();
            db.updateShelter(shelter);

        });

        deleteButton.setOnClickListener(view -> {
            SheltersApp app = (SheltersApp) context.getApplicationContext();
            ShelterDB db = app.getDb();
            db.deletePrivateShelter(shelter.getId());
            if (getItemCount() == 1) {
                ((Activity) context).findViewById(R.id.youHaveNoShelters).setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return shelterDB.getUserShelters().size();
    }
}

