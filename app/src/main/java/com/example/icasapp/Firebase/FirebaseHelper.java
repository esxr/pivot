package com.example.icasapp.Firebase;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.icasapp.Auth.Hardcoded;
import com.example.icasapp.MainActivity;
import com.example.icasapp.User.TestUser;
import com.example.icasapp.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FirebaseHelper {

    static String TAG = "Firebase Helper";

    private static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void signOut() {
        mAuth.signOut();
    }

    public static boolean checkLoginStatus() {
        if (user == null)
            return false;
        else
            return true;
    }

    public static String userEmail() {
        return user.getEmail();
    }

    public static FirebaseAuth getmAuth() {
        return mAuth;
    }

    public static FirebaseUser getUser() {
        return user;
    }

    /*
     * Functions related to Firestore
     */

    // authentication
    public static void FirebaseLogin(String email, String password) {

        try {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("msg", "signInWithEmail:success");
                                System.out.println("Firebase Log In Success");
                                FirebaseUser user = mAuth.getCurrentUser();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("msg", "signInWithEmail:failure", task.getException());
                            }

                            // ...
                            FirebaseHelper.initiate();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            System.out.println("Firebase Login Failed");
                        }
                    });
        } catch (Exception e) {
            System.out.println("Firebase Login Failed \n" + e);
        }
    }


    //initiation
    public static void initiate() {
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance();
    }

    public static FirebaseFirestore getFirestore() {
        return db;
    }

    public static void addDocumentToCollection(Object doc, String collection) {
        db.collection(collection)
                .add(doc)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);

                    }
                });
    }

    public interface MyCallback {
        void callbackCall(TestUser user);
    }

    public static void getDocumentFromCollection(String documentName, String collectionName, final MyCallback callback) {
        DocumentReference docRef = db.collection(collectionName).document(documentName);
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            TestUser user;

            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(TestUser.class);

                callback.callbackCall(user);
            }
        });
    }

    public static void getCollection(String collection) {
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    List<Object> list = new ArrayList<>();

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document);
                                Log.d(TAG, document.getData() + " => " + document.getData());
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}
