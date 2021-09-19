package com.huji_postpc_avih.sharemyshelter.alerts;

import android.os.Bundle;
import android.view.WindowManager;

import com.huji_postpc_avih.sharemyshelter.R;

import androidx.appcompat.app.AppCompatActivity;

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
    }
}