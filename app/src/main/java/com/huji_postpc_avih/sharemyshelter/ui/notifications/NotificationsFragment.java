package com.huji_postpc_avih.sharemyshelter.ui.notifications;

import android.os.Bundle;

import com.huji_postpc_avih.sharemyshelter.R;
import com.huji_postpc_avih.sharemyshelter.SheltersApp;
import com.huji_postpc_avih.sharemyshelter.users.UserManagerFirebase;

import androidx.preference.PreferenceFragmentCompat;

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
            getPreferenceManager().findPreference("preference_account").setSummary("Signed in as " + userManager.authenticatedUser.getValue().getDisplayName());
        } else {
            getPreferenceManager().findPreference("preference_signout").setVisible(false);
            getPreferenceManager().findPreference("preference_signin").setVisible(true);
            getPreferenceManager().findPreference("preference_account").setSummary("Signed out");
        }
    }
}