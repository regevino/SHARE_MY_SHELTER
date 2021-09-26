package com.huji_postpc_avih.sharemyshelter.ui.dashboard.add_shelter;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.huji_postpc_avih.sharemyshelter.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

public class AddShelterActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private Button nextButton;
    private Button backButton;
    private Button doneButton;
    private AddShelterDetails detailsFragment;
    private AddShelterVisualGuides visualGuidesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shelter);

        this.fragmentManager = getSupportFragmentManager();

        nextButton = findViewById(R.id.add_shelter_next);
        backButton = findViewById(R.id.add_shelter_back);
        doneButton = findViewById(R.id.add_shelter_done);

        if (detailsFragment == null) {
            detailsFragment = AddShelterDetails.newInstance();
        }
        if (visualGuidesFragment == null) {
            visualGuidesFragment = AddShelterVisualGuides.newInstance();
        }

        fragmentManager.beginTransaction()
                .add(R.id.add_shelter_fragment, detailsFragment)
                .commit();
        fragmentManager.executePendingTransactions();

        nextButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
        doneButton.setVisibility(View.GONE);

        nextButton.setOnClickListener(this::onClickNext);
        backButton.setOnClickListener(this::onClickBack);
        doneButton.setOnClickListener(this::onClickDone);

    }

    private void onClickNext(View b) {
        nextButton.setVisibility(View.GONE);
        backButton.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.VISIBLE);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_start, R.anim.anim_slide_out_end);
        ft.replace(R.id.add_shelter_fragment, visualGuidesFragment);
        ft.commit();
        fragmentManager.executePendingTransactions();
    }

    private void onClickBack(View b) {
        nextButton.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
        doneButton.setVisibility(View.GONE);

        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.setCustomAnimations(R.anim.anim_slide_in_end, R.anim.anim_slide_out_start);
        ft.replace(R.id.add_shelter_fragment, (Fragment) detailsFragment);
        ft.commit();
        fragmentManager.executePendingTransactions();
    }

    private void onClickDone(View b) {
//        detailsFragment.onCLickDone();
        //todo end
    }
}