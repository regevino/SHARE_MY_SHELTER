package com.huji_postpc_avih.sharemyshelter.users;

public abstract class UserManager {


    abstract void createNewUser(String username, String password);

    abstract void authenticate(String username, String password);

    abstract String getCurrentUser();

    abstract void signOut();

    abstract void deleteAccount();
}
