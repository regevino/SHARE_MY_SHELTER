package com.huji_postpc_avih.sharemyshelter.alerts;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.data.Shelter;
import com.huji_postpc_avih.sharemyshelter.data.ShelterDB;
import com.huji_postpc_avih.sharemyshelter.navigation.NavigateToShelterActivity;
import com.huji_postpc_avih.sharemyshelter.navigation.Navigator;

import java.util.Date;

import androidx.appcompat.app.AppCompatActivity;

import static com.huji_postpc_avih.sharemyshelter.navigation.NavigateToShelterActivity.EXTRA_KEY_SELF_LOCATION;
import static com.huji_postpc_avih.sharemyshelter.navigation.NavigateToShelterActivity.EXTRA_KEY_SHELTER_ID;

public class AlertRecievedActivity extends AppCompatActivity {

    private Date arrivalDeadline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_recieved);

        boolean isTest = getIntent().getBooleanExtra(AlertRecievedService.EXTRA_IS_TEST, false);
        arrivalDeadline = new Date(getIntent().getLongExtra(AlertRecievedService.EXTRA_KEY_DEADLINE, new Date().getTime()));

        if (isTest)
        {
            TextView titleText = findViewById(R.id.alert_recieved_title_text);
            titleText.setText("<TEST> Red Alert at your location demo\n" +
                    "THIS IS ONLY A TEST AND DOES NOT INDICATE A ROCKET ATTACK");
        }
        // Important: have to do the following in order to show without unlocking
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        Navigator n = new Navigator(this);
        ShelterDB db = ((SheltersApp) getApplication()).getDb();
        n.getCurrentLocation().addOnCompleteListener(task -> {
            double longitude = task.getResult().getLongitude();
            double latitude = task.getResult().getLatitude();
            db.getNearbyShelters(longitude, latitude, getRadiousForDeadline(arrivalDeadline.getTime())).addOnCompleteListener(task1 ->
            {
                Shelter nearestShelter = n.getNearestShelter(longitude, latitude, task1.getResult(), arrivalDeadline.getTime());
                onShelterFound(nearestShelter, new double[]{longitude, latitude}, arrivalDeadline);
            });
        });
    }

    private void onShelterFound(Shelter sh, double[] currentLocation, Date arrivalDeadline) {
        if (sh == null) {
            Toast.makeText(this, "Could not fund nearby shelter.", Toast.LENGTH_LONG).show();
            findViewById(R.id.alert_recieved_waiting_layout).setVisibility(View.GONE);
            findViewById(R.id.no_shelter_instructions).setVisibility(View.VISIBLE);
//            finish();

        } else {
            findViewById(R.id.no_shelter_instructions).setVisibility(View.GONE);

            findViewById(R.id.alert_recieved_waiting_layout).setVisibility(View.GONE);
            findViewById(R.id.alert_recieved_found_layout).setVisibility(View.VISIBLE);
            Button button = findViewById(R.id.alert_recieved_navigate_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(AlertRecievedActivity.this, NavigateToShelterActivity.class);
                    intent.putExtra(EXTRA_KEY_SHELTER_ID, sh.getId().toString());
                    intent.putExtra(EXTRA_KEY_SELF_LOCATION, currentLocation);
                    intent.putExtra(AlertRecievedService.EXTRA_KEY_DEADLINE, arrivalDeadline.getTime());
                    intent.putExtra(AlertRecievedService.EXTRA_IS_FOREGROUND_NOTIFICATION,
                            AlertRecievedActivity.this.getIntent()
                                    .getBooleanExtra(AlertRecievedService.EXTRA_IS_FOREGROUND_NOTIFICATION, false));
                    startActivity(intent);
                }
            });
        }
    }

    private static int getRadiousForDeadline(long deadline) {
        return 500;
    }
}