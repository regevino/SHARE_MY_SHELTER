package com.huji_postpc_avih.sharemyshelter.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.huji_postpc_avih.sharemyshelter.AddShelterActivity;
import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.databinding.FragmentDashboardBinding;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private BroadcastReceiver receiver;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Get list of current user's shelters:
        SheltersApp app = (SheltersApp) root.getContext().getApplicationContext();
        ArrayList<Shelter> userShelters = app.getDb().getUserShelters();

        // Set RecyclerView of shelters, adapter etc.
        RecyclerView sheltersList = root.findViewById(R.id.privateShelters);
        SheltersAdapter adapter = new SheltersAdapter();
        adapter.setUserShelters(userShelters);
        adapter.setContext(root.getContext());
        sheltersList.setAdapter(adapter);
        sheltersList.setLayoutManager(new LinearLayoutManager(root.getContext(), RecyclerView.VERTICAL, false));

        // Add shelter button:
        FloatingActionButton addShelterButton = root.findViewById(R.id.floatingActionButton2);
        addShelterButton.setOnClickListener(v -> {
            startActivity(new Intent(root.getContext(), AddShelterActivity.class));
        });

        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent != null && intent.getAction().equals("Added")) {
                    adapter.notifyDataSetChanged();
                }
            }
        };
        app.registerReceiver(receiver, new IntentFilter("Added"));

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SheltersApp app = (SheltersApp) binding.getRoot().getContext().getApplicationContext();
        app.unregisterReceiver(receiver);
        binding = null;
    }
}