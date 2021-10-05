package com.huji_postpc_avih.sharemyshelter.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.ui.AllSheltersAdapter;

import java.util.ArrayList;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllSheltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllSheltersFragment extends Fragment {



    public AllSheltersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AllSheltersFragment.
     */
    public static AllSheltersFragment newInstance(String param1, String param2) {
        AllSheltersFragment fragment = new AllSheltersFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_all_shelters, container, false);
        // Get list of current user's shelters:
        FragmentActivity activity = getActivity();
        SheltersApp app = (SheltersApp) root.getContext().getApplicationContext();
        ArrayList<Shelter> allShelters = app.getDb().getAllShelters();

        // Set RecyclerView of shelters, adapter etc.
        RecyclerView sheltersList = root.findViewById(R.id.shelters);
        AllSheltersAdapter adapter = new AllSheltersAdapter(getActivity());
        adapter.setShelters(allShelters);
        adapter.setShelterDB(app.getDb());
        sheltersList.setAdapter(adapter);
        sheltersList.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

        // Inflate the layout for this fragment
        return root;
    }
}