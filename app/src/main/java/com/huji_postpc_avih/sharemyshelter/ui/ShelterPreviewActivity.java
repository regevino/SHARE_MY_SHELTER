package com.huji_postpc_avih.sharemyshelter.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.huji_postpc_avih.sharemyshelter.R;

import java.util.UUID;

public class ShelterPreviewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shelter_preview);

        TextView title = findViewById(R.id.Title);
        TextView description = findViewById(R.id.description);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String desc = intent.getStringExtra("description");
        UUID id = (UUID) intent.getSerializableExtra("id");

        title.setText(String.format("Name:\n%s", name));
        description.setText(String.format("Description: %s", desc));

    }
}