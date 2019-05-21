package com.example.icasapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseHelper {

    private static FirebaseAuth mAuth;
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    public static void signOut(){
        mAuth.signOut();
    }

    public static boolean checkLoginStatus(){
        if(user == null)
            return false;
        else
            return true;
    }

    public static String userEmail(){
        return user.getEmail();
    }


}
