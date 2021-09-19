package com.huji_postpc_avih.sharemyshelter.users;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public interface UserManager {


    Task<AuthResult> createNewUser(String username, String password);

    Task<AuthResult> authenticate(String username, String password);

    String getCurrentUser();

    void signOut();

    void deleteAccount();
}
