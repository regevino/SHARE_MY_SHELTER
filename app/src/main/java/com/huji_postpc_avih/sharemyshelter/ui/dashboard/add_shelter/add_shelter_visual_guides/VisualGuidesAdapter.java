package com.huji_postpc_avih.sharemyshelter.ui.dashboard.add_shelter.add_shelter_visual_guides;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.data.ShelterVisualGuide;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.List;
import java.util.UUID;

public class VisualGuidesAdapter extends RecyclerView.Adapter<VisualGuidesHolder> {

    private List<ShelterVisualGuide> visualGuidesList;
    private FragmentManager parentFragmentManager;

    @NonNull
    @Override
    public VisualGuidesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View contextView = inflater.inflate(R.layout.row_add_visual_step, parent, false);
        return new VisualGuidesHolder(contextView);
    }

    @Override
    public void onBindViewHolder(@NonNull VisualGuidesHolder holder, int position) {
        ShelterVisualGuide visualGuide = visualGuidesList.get(position);
        holder.addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickImageDialog.build(new PickSetup()).show(parentFragmentManager).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        if (r.getError() == null) {
                            Log.i("test", "photo success");
                            //If you want the Uri.
                            //Mandatory to refresh image from Uri.
                            //getImageView().setImageURI(null);

                            //Setting the real returned image.
                            //getImageView().setImageURI(r.getUri());

                            //If you want the Bitmap.
//            getImageView().setImageBitmap(r.getBitmap());
                            Bitmap image = r.getBitmap();
                            visualGuide.setImage(image);
                            holder.imageAdded.setVisibility(View.VISIBLE);


                            //Image path
                            //r.getPath();
                        } else {
                            //Handle possible errors
                            //TODO: do what you have to do with r.getError();
//                            Toast.makeText(getActivity(), r.getError().getMessage(), Toast.LENGTH_LONG).show();
                            Log.i("test", "photo failed\n" + r.getError());

                        }
                    }
                });

            }
        });

        holder.addVisualGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.addVisualGuide.setVisibility(View.GONE);
                addEmptyVisualGuide();
            }
        });

        holder.stepNumber.setText("step: ".concat(Integer.toString(visualGuide.getStepNumber())));

        holder.imageDescription.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                visualGuide.setDescription(holder.imageDescription.getText().toString());
            }
        });

    }

    private void addEmptyVisualGuide() {

        ShelterVisualGuide example = new ShelterVisualGuide();
        example.setDescription("Example Description");
        Bitmap.Config conf = Bitmap.Config.ARGB_8888; // see other conf types
        Bitmap bmp = Bitmap.createBitmap(200, 300, conf);
        example.setImage(bmp);
        example.setId(UUID.randomUUID());
        example.setStepNumber(visualGuidesList.size() + 1);
        visualGuidesList.add(example);
        notifyItemInserted(visualGuidesList.size());

    }

    @Override
    public int getItemCount() {
        return this.visualGuidesList.size();
    }

    public void setList(List<ShelterVisualGuide> list) {
        this.visualGuidesList = list;
    }

    public void setParentFragmentManager(FragmentManager manager) {
        this.parentFragmentManager = manager;
    }
}
