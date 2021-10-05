package com.huji_postpc_avih.sharemyshelter.users;

import android.app.Activity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class UserManagerFirebase implements UserManager {
    final static String TAG = "USER_MANAGER";
    private static UserManagerFirebase instance = null;
    private FirebaseAuth auth;
    private MutableLiveData<FirebaseUser> _authenticatedUser;
    public LiveData<FirebaseUser> authenticatedUser;



    public static UserManagerFirebase getInstance()
    {
        if (instance == null)
        {
            instance = new UserManagerFirebase();
        }
        return instance;
    }

    private UserManagerFirebase()
    {
        auth = FirebaseAuth.getInstance();
        _authenticatedUser = new MutableLiveData<>(null);
        authenticatedUser = _authenticatedUser;
        _authenticatedUser.setValue(auth.getCurrentUser());
    }




    @Override
    public Task<AuthResult> createNewUser(String username, String password) {

        return auth.createUserWithEmailAndPassword(username, password);/*.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success");
                    FirebaseUser user = auth.getCurrentUser();
                    _authenticatedUser.setValue(user);

                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    _authenticatedUser.setValue(null);
                }

            }
        });*/
    }

    @Override
    public Task<AuthResult> authenticate(String username, String password) {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null)
        {
            return auth.signInWithEmailAndPassword(username, password);/*.addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success");
                        FirebaseUser user = auth.getCurrentUser();
                        _authenticatedUser.setValue(user);

                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        _authenticatedUser.setValue(null);
                    }
                }
            });*/
        }
        return new Task<AuthResult>() {
            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public boolean isSuccessful() {
                return true;
            }

            @Override
            public boolean isCanceled() {
                return false;
            }

            @Override
            public AuthResult getResult() {
                return null;
            }

            @Override
            public <X extends Throwable> AuthResult getResult(@NonNull @NotNull Class<X> aClass) throws X {
                return null;
            }

            @Nullable
            @org.jetbrains.annotations.Nullable
            @Override
            public Exception getException() {
                return null;
            }

            @NonNull
            @NotNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull @NotNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return this;
            }

            @NonNull
            @NotNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return this;
            }

            @NonNull
            @NotNull
            @Override
            public Task<AuthResult> addOnSuccessListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnSuccessListener<? super AuthResult> onSuccessListener) {
                return this;
            }

            @NonNull
            @NotNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull @NotNull OnFailureListener onFailureListener) {
                return this;
            }

            @NonNull
            @NotNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull @NotNull Executor executor, @NonNull @NotNull OnFailureListener onFailureListener) {
                return this;
            }

            @NonNull
            @NotNull
            @Override
            public Task<AuthResult> addOnFailureListener(@NonNull @NotNull Activity activity, @NonNull @NotNull OnFailureListener onFailureListener) {
                return this;
            }
        };
    }

    @Override
    public String getCurrentUser() {
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            _authenticatedUser.setValue(currentUser);
            return currentUser.getUid();
        }
        return null;
    }

    @Override
    public void signOut() {
        auth.signOut();
    }

    @Override
    public void deleteAccount() {

    }
}
