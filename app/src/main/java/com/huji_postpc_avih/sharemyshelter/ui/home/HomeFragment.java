package com.huji_postpc_avih.sharemyshelter.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;
import com.huji_postpc_avih.sharemyshelter.databinding.FragmentHomeBinding;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);
        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        if (mapFragment == null) {
            mapFragment = new SupportMapFragment();
            FragmentTransaction ft = fm.beginTransaction();
            ft.add(R.id.fragmentContainer, mapFragment, "mapFragment");
            ft.commit();
            fm.executePendingTransactions();
        }

        mapFragment.getMapAsync(this);

        Spinner modeSpinner = root.findViewById(R.id.modeSpinner);
        String[] items = new String[]{"Map", "List"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modeSpinner.setAdapter(adapter);
        SupportMapFragment finalMapFragment = mapFragment;
        modeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String choice = modeSpinner.getSelectedItem().toString();
                FragmentTransaction ft = fm.beginTransaction();
                if (choice.equals("List")) {
                    AllSheltersFragment allSheltersFragment = new AllSheltersFragment();
                    ft.replace(R.id.fragmentContainer, allSheltersFragment);
                }
                else
                {
                    ft.replace(R.id.fragmentContainer, finalMapFragment, "mapFragment");
                    finalMapFragment.getMapAsync(HomeFragment.this);
                }
                ft.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        showShelters(googleMap);
        googleMap.setIndoorEnabled(false);
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

    }

    private void showShelters(GoogleMap googleMap) {
        SheltersApp app = (SheltersApp) binding.getRoot().getContext().getApplicationContext();
        ShelterDB db = app.getDb();
        LatLng latlng = new LatLng(0, 0); //default...
        for (Shelter shelter : db.getAllShelters()) {
            double latitude = shelter.getLat();
            double longitude = shelter.getLng();
            latlng = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .title(shelter.getName()));
        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12.0f));

    }
}