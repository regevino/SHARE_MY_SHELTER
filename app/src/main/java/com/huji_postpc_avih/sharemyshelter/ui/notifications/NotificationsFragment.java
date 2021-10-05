package com.huji_postpc_avih.sharemyshelter.ui.notifications;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.alerts.MyFirebaseMessagingService;
import com.huji_postpc_avih.sharemyshelter.users.UserManagerFirebase;

import java.io.IOException;
import java.util.Map;

import androidx.preference.PreferenceFragmentCompat;
import io.grpc.internal.JsonParser;

public class NotificationsFragment extends PreferenceFragmentCompat {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        updateSignInInfo();
        getPreferenceManager().findPreference("preference_signout").setOnPreferenceClickListener(preference ->
                {
                    ((SheltersApp)(getActivity().getApplication())).getUserManager().signOut();
                    updateSignInInfo();
                    return false;
                }
        );
        getPreferenceManager().findPreference("preference_send_test_alert").setOnPreferenceClickListener(preference ->
                {
                    String topic = MyFirebaseMessagingService.SUBSCRIBE_TEST_MESSAGE_TOPIC;
                    FirebaseMessaging instance = FirebaseMessaging.getInstance();
                    instance.subscribeToTopic(topic);
                    requestTestAlert();
                    return false;
                }
        );

    }


    private void requestTestAlert()
    {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url ="https://us-central1-share-my-shelter.cloudfunctions.net/sendTestAlert";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Map<String, ?> parse = (Map<String, ?>) JsonParser.parse(response);
                            if (parse.get("result").toString().equals("success"))
                            {
                                Toast.makeText(getContext(), "Requested Test Alert", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Request test alert failed, try again later", Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Request test alert failed, try again later", Toast.LENGTH_LONG).show();
            }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    @Override
    public void onResume() {
        super.onResume();
        updateSignInInfo();
    }

    private void updateSignInInfo() {
        UserManagerFirebase userManager = ((SheltersApp) (getActivity().getApplication())).getUserManager();
        if (userManager.getCurrentUser() != null) {
            getPreferenceManager().findPreference("preference_signout").setVisible(true);
            getPreferenceManager().findPreference("preference_signin").setVisible(false);
            getPreferenceManager().findPreference("preference_account").setSummary("Signed in as " + userManager.authenticatedUser.getValue().getDisplayName());//TODO handle the case in which auth.getCurrentUser() == null
        } else {
            getPreferenceManager().findPreference("preference_signout").setVisible(false);
            getPreferenceManager().findPreference("preference_signin").setVisible(true);
            getPreferenceManager().findPreference("preference_account").setSummary("Signed out");
        }
    }
}