package com.huji_postpc_avih.sharemyshelter.ui.dashboard.add_shelter;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.data.ShelterVisualGuide;
import com.huji_postpc_avih.sharemyshelter.ui.dashboard.add_shelter.add_shelter_visual_guides.VisualGuidesAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddShelterVisualGuides#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddShelterVisualGuides extends Fragment {
    private View root;
    private ArrayList<ShelterVisualGuide> visualGuideList;


    public AddShelterVisualGuides() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment AddShelterVisualGuides.
     */
    // TODO: Rename and change types and number of parameters
    public static AddShelterVisualGuides newInstance() {
        AddShelterVisualGuides fragment = new AddShelterVisualGuides();
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
        root = inflater.inflate(R.layout.fragment_add_shelter_visual_guides, container, false);

        visualGuideList = new ArrayList<>();
        ShelterVisualGuide example = new ShelterVisualGuide();
        example.setDescription("Example Description");
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(200, 300, conf);
        example.setId(UUID.randomUUID());
        example.setImage(bmp);
        example.setStepNumber(1);
        visualGuideList.add(example);

        RecyclerView recyclerView = root.findViewById(R.id.AddVisualGuidesRecycler);
        VisualGuidesAdapter adapter = new VisualGuidesAdapter();
        adapter.setList(visualGuideList);
        adapter.setParentFragmentManager(getParentFragmentManager());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext(), RecyclerView.VERTICAL, false));

        return root;
    }


    public List<ShelterVisualGuide> getVisualGuidelines() {
        return visualGuideList;
    }

}