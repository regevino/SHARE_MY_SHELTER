package com.huji_postpc_avih.sharemyshelter.ui.dashboard.add_shelter;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;
import com.huji_postpc_avih.sharemyshelter.data.ShelterStorage;
import com.huji_postpc_avih.sharemyshelter.data.ShelterVisualGuide;
import com.huji_postpc_avih.sharemyshelter.navigation.Navigator;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddShelterDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddShelterDetails extends Fragment {
    private Location location;
    private View root;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddShelterDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddShelterDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static AddShelterDetails newInstance() {
        AddShelterDetails fragment = new AddShelterDetails();
        Bundle args = new Bundle();
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
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_shelter_details, container, false);
        Spinner dropdown = root.findViewById(R.id.locationSpinner);
        String[] items = new String[]{"Current Location", "Choose from Map"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = dropdown.getSelectedItem().toString();
                if (choice.equals("Current Location")) {
                    if (location != null) {
                        Toast.makeText(getActivity(), location.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    //TODO let user enter address

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Switch isPrivate = root.findViewById(R.id.shelterMode);
        isPrivate.setOnClickListener(v -> {
            if (isPrivate.getText().toString().equals("Public")) {
                isPrivate.setText(R.string.Private);
            } else {
                isPrivate.setText(R.string.Public);
            }
        });

        return root;
    }

    public void AddShelter(List<ShelterVisualGuide> visualGuides) {
        SheltersApp app = (SheltersApp) root.getContext().getApplicationContext();
        ShelterDB db = app.getDb();
        ShelterStorage storage = app.getShelterStorage();
        String currentUser = db.getManager().getCurrentUser();
        Shelter newShelter;
        EditText name = root.findViewById(R.id.step_number);
        Switch isPrivate = root.findViewById(R.id.shelterMode);
        EditText desc = root.findViewById(R.id.description);


        if (isPrivate.isChecked()) {
            newShelter = new Shelter(null, name.getText().toString(), Shelter.ShelterType.PRIVATE, currentUser, desc.getText().toString());
        } else {
            newShelter = new Shelter(null, name.getText().toString(), Shelter.ShelterType.PUBLIC, null, desc.getText().toString());
        }
        Navigator navigator = new Navigator((Activity) root.getContext());
        navigator.getCurrentLocation().addOnSuccessListener(location -> {
            if (location == null) {
                Toast.makeText(root.getContext(), "Failed to add new shelter :(", Toast.LENGTH_LONG).show();
                return;
            }

            newShelter.setLat(location.getLatitude());
            newShelter.setLng(location.getLongitude());
            newShelter.set_visualStepsLiveData(visualGuides);
            if (newShelter.getShelterType() == Shelter.ShelterType.PRIVATE) {
                db.addPrivateShelter(newShelter);
            } else {
                db.addPublicShelter(newShelter);
            }

            Toast.makeText(root.getContext(), "Shelter added successfully!", Toast.LENGTH_LONG).show();
        });

    }
}