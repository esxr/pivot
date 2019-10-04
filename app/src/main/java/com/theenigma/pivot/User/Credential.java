package com.theenigma.pivot.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;

public class Credential {
    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private static HashMap<String, Object> credential;

    public static HashMap<String, Object> getCredential() {
        credential = new HashMap<String, Object>();
        credential.put("auth", Credential.mAuth);
        credential.put("user", Credential.user);
        return credential;
    }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static FirebaseUser getUser() {
        return user;
    }
}
