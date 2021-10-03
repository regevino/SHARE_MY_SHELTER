package com.huji_postpc_avih.sharemyshelter.ui.home;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;
import com.huji_postpc_avih.sharemyshelter.databinding.FragmentHomeBinding;
import com.huji_postpc_avih.sharemyshelter.ui.ShelterPreviewActivity;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.UUID;

public class HomeFragment extends Fragment implements OnMapReadyCallback {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;
    private SimpleCursorAdapter mAdapter;
    SheltersApp app;
    private static final String[] SUGGESTIONS = {
            "Bauru", "Sao Paulo", "Rio de Janeiro",
            "Bahia", "Mato Grosso", "Minas Gerais",
            "Tocantins", "Rio Grande do Sul"
    };

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        homeViewModel =
//                new ViewModelProvider(this).get(HomeViewModel.class);

        FragmentManager fm = getChildFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment) fm.findFragmentByTag("mapFragment");

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        app = (SheltersApp) binding.getRoot().getContext().getApplicationContext();

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
                    //                    fm.executePendingTransactions();
                } else {
                    ft.replace(R.id.fragmentContainer, finalMapFragment, "mapFragment");
                    finalMapFragment.getMapAsync(HomeFragment.this);
                    //                    fm.executePendingTransactions();
                }
                ft.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SearchView searchView = root.findViewById(R.id.search_bar);
        final String[] from = new String[]{"shelter", "id"};
        final int[] to = new int[]{R.id.suggestionName, 0};
        mAdapter = new SimpleCursorAdapter(getContext(), R.layout.simple_list_item_1, null, from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        searchView.setSuggestionsAdapter(mAdapter);
        searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
            @Override
            public boolean onSuggestionSelect(int position) {
                Cursor cursor = (Cursor) mAdapter.getItem(position);
                String txt = cursor.getString(cursor.getColumnIndex("shelter"));
                searchView.setQuery(txt, true);
                return true;
            }

            @Override
            public boolean onSuggestionClick(int position) {
                // Your code here
                Cursor cursor = (Cursor) mAdapter.getItem(position);

                String id = cursor.getString(cursor.getColumnIndex("id"));

                Shelter shelter = app.getDb().getShelterById(UUID.fromString(id));


                Intent intent = new Intent(getActivity(), ShelterPreviewActivity.class);
                intent.putExtra("name", shelter.getName());
                intent.putExtra("description", shelter.getDescription());
                intent.putExtra("id", shelter.getId());
                startActivity(intent);
                return true;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                populateAdapter(s);
                return false;
            }
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
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {

            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Shelter shelter = (Shelter) (marker.getTag());
                if (shelter == null) {
                    return false;
                }
                Intent intent = new Intent(getActivity(), ShelterPreviewActivity.class);
                intent.putExtra("name", shelter.getName());
                intent.putExtra("description", shelter.getDescription());
                intent.putExtra("id", shelter.getId());
                startActivity(intent);
                return false;
            }
        });
        showShelters(googleMap);
        googleMap.setIndoorEnabled(false);
        if (ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        googleMap.setMyLocationEnabled(true);

        View locationButton = ((View) binding.fragmentContainer.findViewById(Integer.parseInt("1")).getParent()).findViewById(Integer.parseInt("2"));
//        locationButton.setVisibility(View.GONE);
        RelativeLayout.LayoutParams rlp = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
        // position on right bottom
        // Align it to - parent BOTTOM|RIGHT
        rlp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, 0);
        rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

        // Update margins, set to 80dp
        final int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 80,
                getResources().getDisplayMetrics());
        rlp.setMargins(margin, margin, margin, margin);

    }

    private void showShelters(GoogleMap googleMap) {
        ShelterDB db = app.getDb();
        LatLng latlng = new LatLng(31.771959, 35.217018); //default...
        for (Shelter shelter : db.getAllShelters()) {
            double latitude = shelter.getLat();
            double longitude = shelter.getLng();
            latlng = new LatLng(latitude, longitude);
            googleMap.addMarker(new MarkerOptions()
                    .position(latlng)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker))
                    .title(shelter.getName()))
                    .setTag(shelter);

        }
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 12.0f));

    }

    // You must implements your logic to get data using OrmLite
    private void populateAdapter(String query) {
        final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "shelter","id"});
        ArrayList<Shelter> allShelters = app.getDb().getAllShelters();
        for (int i = 0; i < allShelters.size(); i++) {
            String shelterName = allShelters.get(i).getName();
            if (Substr(query.toLowerCase(), shelterName.toLowerCase()) != -1)
                c.addRow(new Object[]{i, shelterName,allShelters.get(i).getId().toString()});
        }
        mAdapter.changeCursor(c);
    }

    private int Substr(String s2, String s1) {
        int counter = 0; //pointing s2
        int i = 0;
        for (; i < s1.length(); i++) {
            if (counter == s2.length())
                break;
            if (s2.charAt(counter) == s1.charAt(i)) {
                counter++;
            } else {
                //Special case where character preceding the i'th character is duplicate
                if (counter > 0) {
                    i -= counter;
                }
                counter = 0;
            }
        }
        return counter < s2.length() ? -1 : i - counter;
    }
}