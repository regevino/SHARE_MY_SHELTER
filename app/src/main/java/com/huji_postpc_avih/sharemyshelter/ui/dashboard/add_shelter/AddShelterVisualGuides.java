package com.huji_postpc_avih.sharemyshelter.ui.dashboard.add_shelter;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.data.ShelterVisualGuide;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddShelterVisualGuides#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddShelterVisualGuides extends Fragment implements IPickResult {


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
//        if (getArguments() != null) {
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ImageView addPhotoButton = getActivity().findViewById(R.id.add_photo);

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE);
                PickImageDialog.build(new PickSetup()).show(getParentFragmentManager());

            }
        });
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_shelter_visual_guides, container, false);
    }

    public List<ShelterVisualGuide> getVisualGuidelines() {
        return null;
    }

    @Override
    public void onPickResult(PickResult r) {
        if (r.getError() == null) {
            Toast.makeText(getActivity(), "choose photo", Toast.LENGTH_SHORT).show();
            //If you want the Uri.
            //Mandatory to refresh image from Uri.
            //getImageView().setImageURI(null);

            //Setting the real returned image.
            //getImageView().setImageURI(r.getUri());

            //If you want the Bitmap.
//            getImageView().setImageBitmap(r.getBitmap());
            Bitmap image = r.getBitmap();


            //Image path
            //r.getPath();
        } else {
            //Handle possible errors
            //TODO: do what you have to do with r.getError();
            Toast.makeText(getActivity(), r.getError().getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}