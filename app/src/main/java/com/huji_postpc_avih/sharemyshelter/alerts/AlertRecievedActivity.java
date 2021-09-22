package com.huji_postpc_avih.sharemyshelter.alerts;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.navigation.NavigateToShelterActivity;

import androidx.appcompat.app.AppCompatActivity;

import static com.huji_postpc_avih.sharemyshelter.data.dummyData.Dummy.getDummyShelter;
import static com.huji_postpc_avih.sharemyshelter.navigation.NavigateToShelterActivity.EXTRA_KEY_SHELTER;

public class AlertRecievedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_recieved);

        // Important: have to do the following in order to show without unlocking
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        Activity thisActivity = this;
        Button button = findViewById(R.id.alert_recieved_navigate_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(thisActivity, NavigateToShelterActivity.class);
                intent.putExtra(EXTRA_KEY_SHELTER, getDummyShelter(thisActivity).toJson());
                startActivity(intent);
            }
        });

    }
}