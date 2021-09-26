package com.huji_postpc_avih.sharemyshelter.ui.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.ui.AllSheltersAdapter;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllSheltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllSheltersFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AllSheltersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllSheltersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AllSheltersFragment newInstance(String param1, String param2) {
        AllSheltersFragment fragment = new AllSheltersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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
        AllSheltersAdapter adapter = new AllSheltersAdapter();
        adapter.setShelters(allShelters);
        adapter.setShelterDB(app.getDb());
        sheltersList.setAdapter(adapter);
        sheltersList.setLayoutManager(new LinearLayoutManager(activity, RecyclerView.VERTICAL, false));

        // Inflate the layout for this fragment
        return root;
    }
}