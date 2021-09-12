package com.huji_postpc_avih.sharemyshelter.users;

import java.util.UUID;

public interface UserManager {


    void createNewUser(String username, String password);

    void authenticate(String username, String password);

    UUID getCurrentUser();

    void signOut();

    void deleteAccount();
}
