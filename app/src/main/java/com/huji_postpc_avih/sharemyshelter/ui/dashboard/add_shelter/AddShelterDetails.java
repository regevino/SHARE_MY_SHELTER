package com.huji_postpc_avih.sharemyshelter.ui.dashboard.add_shelter;

import android.app.Activity;
import android.location.Address;
import android.location.Location;
import android.os.Bundle;
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
import com.huji_postpc_avih.sharemyshelter.data.ShelterVisualGuide;
import com.huji_postpc_avih.sharemyshelter.navigation.Navigator;

import java.util.List;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddShelterDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddShelterDetails extends Fragment {
    private Location location;
    private View root;
    private boolean fromAddress;


    public AddShelterDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddShelterDetails.
     */
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

        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_add_shelter_details, container, false);
        Spinner dropdown = root.findViewById(R.id.locationSpinner);
        String[] items = new String[]{"Current Location", "Insert address"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);

        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = dropdown.getSelectedItem().toString();
                if (choice.equals("Current Location")) {
                    fromAddress = false;
                    hideInsertAddress();
                    if (location != null) {
                        Toast.makeText(getActivity(), location.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    fromAddress = true;
                    showInsertAddress();
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

    private void hideInsertAddress() {
        root.findViewById(R.id.addressEditText).setVisibility(View.INVISIBLE);

    }

    private void showInsertAddress() {
        root.findViewById(R.id.addressEditText).setVisibility(View.VISIBLE);
    }

    public void AddShelter(List<ShelterVisualGuide> visualGuides) {
        SheltersApp app = (SheltersApp) root.getContext().getApplicationContext();
        ShelterDB db = app.getDb();
        String currentUser = db.getManager().getCurrentUser();
        Shelter newShelter;
        EditText name = root.findViewById(R.id.step_number);
        Switch isPrivate = root.findViewById(R.id.shelterMode);
        EditText desc = root.findViewById(R.id.description);


        String name1 = name.getText().toString();
        String description = desc.getText().toString();

        if (isPrivate.isChecked()) {
            newShelter = new Shelter(null, name1, Shelter.ShelterType.PRIVATE, currentUser, description);
        } else {
            newShelter = new Shelter(null, name1, Shelter.ShelterType.PUBLIC, null, description);
        }
        if (!validate(newShelter)) {
            Toast.makeText(root.getContext(), "Invalid shelter :(", Toast.LENGTH_SHORT).show();
            return;
        }
        Navigator navigator = new Navigator((Activity) root.getContext());
        if (fromAddress) {
            EditText addressEditText = root.findViewById(R.id.addressEditText);
            Address address = navigator.getLatLng(addressEditText.getText().toString());

            addShelterHelper(newShelter, visualGuides, address.getLatitude(), address.getLongitude(), db);

        } else {
            navigator.getCurrentLocation().addOnSuccessListener(location -> {
                if (location == null) {
                    Toast.makeText(root.getContext(), "Failed to add new shelter :(", Toast.LENGTH_SHORT).show();
                    return;
                }
                addShelterHelper(newShelter, visualGuides, location.getLatitude(), location.getLongitude(), db);

            });


        }


    }

    private boolean validate(Shelter newShelter) {
        return newShelter.getName().length() > 0 && newShelter.getDescription().length() > 0;
    }

    private void addShelterHelper(Shelter shelter, List<ShelterVisualGuide> visualGuides, double lat, double lng, ShelterDB db) {
        shelter.setLat(lat);
        shelter.setLng(lng);
        shelter.set_visualStepsLiveData(visualGuides);
        if (shelter.getShelterType() == Shelter.ShelterType.PRIVATE) {
            db.addPrivateShelter(shelter);
        } else {
            db.addPublicShelter(shelter);
        }
        Toast.makeText(root.getContext(), "Shelter added successfully!", Toast.LENGTH_SHORT).show();

    }
}