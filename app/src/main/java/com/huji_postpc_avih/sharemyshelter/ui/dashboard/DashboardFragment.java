package com.huji_postpc_avih.sharemyshelter.ui.dashboard;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.huji_postpc_avih.sharemyshelter.AddShelterActivity;
import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.databinding.FragmentDashboardBinding;
import com.huji_postpc_avih.sharemyshelter.users.SignInActivity;
import com.huji_postpc_avih.sharemyshelter.users.UserManagerFirebase;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;
    private BroadcastReceiver receiver;
    private UserManagerFirebase userManager;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        SheltersApp app = (SheltersApp) root.getContext().getApplicationContext();
        userManager = app.getUserManager();
        if(userManager.getCurrentUser() != null)
        {
            showUserShelters();
        }
        else
        {
            showAuthentication();
        }
        return root;
    }

    private void showAuthentication() {
        FloatingActionButton fab = binding.getRoot().findViewById(R.id.floatingActionButton2);
        fab.setVisibility(View.GONE);
        LinearLayout layout = binding.getRoot().findViewById(R.id.layout_not_signed_in);
        layout.setVisibility(View.VISIBLE);
        Button signinButton = binding.getRoot().findViewById(R.id.singin);
        DashboardFragment dashboardFragment = this;
        signinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(dashboardFragment.getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });
        Button registerButton = binding.getRoot().findViewById(R.id.register);
    }

    private void showUserShelters() {
        View root = binding.getRoot();
        LinearLayout l = root.findViewById(R.id.layout_not_signed_in);
        l.setVisibility(View.GONE);
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
        addShelterButton.setVisibility(View.VISIBLE);
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


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SheltersApp app = (SheltersApp) binding.getRoot().getContext().getApplicationContext();
        if (receiver != null) {
            app.unregisterReceiver(receiver);
        }
        binding = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(userManager.getCurrentUser() != null)
        {
            showUserShelters();
        }
        else
        {
            showAuthentication();
        }
    }
}