package com.example.icasapp.Firebase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;

import com.example.icasapp.Annonations.Hardcoded;
import com.example.icasapp.User.TestUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public static FirebaseUser getUser() {
        return user;
    }

    /*
     * Functions related to Firestore
     */
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

    @Hardcoded
    public static void generateFakeFirebaseUsers(int no_of_users) {
        for (int i = 0; i < no_of_users; i++) {
            db.collection("USER")
                    .add(TestUser.getFirebaseDocumentHARDCODED())
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

    }

    public interface CallBackList<T> {
        void callbackCall(List<T> list);
    }

    public interface CallbackObject<T> {
        void callbackCall(T object);
    }

    @Deprecated
    public static void getDocumentFromCollectionWhere(@Nullable final Query query, String collectionName, final CallbackObject<List<HashMap<String, String>>> callback) {
        FirebaseHelper.getCollection(collectionName, new CallBackList<Map<String, Object>>() {
            @Override
            public void callbackCall(List<Map<String, Object>> list) {
                List<HashMap<String, String>> matches = new ArrayList<>();
                for (Map<String, Object> obj : list) {
                    Map<String, String> convertedObj = (Map) obj;
                    HashMap<String, String> userData = new HashMap<>(convertedObj);

                    try {
                        if (userData.get(query.getProperty()).equals(query.getValue())) {
                            matches.add(userData);
                        }
                    } catch (Exception e) {
                    }
                    callback.callbackCall(matches);
                }
            }
        });
    }

    public static void getDocumentFromCollectionWhere(String collection, Query query, final CallbackObject<List<Map<String, Object>>> callback) {
        CollectionReference colRef = db.collection(collection);
        final List<Map<String, Object>> matches = new ArrayList<>();

        String[] properties = {"name", "regNo", "stream", "semester", "UID"};
        for (String property : properties) {
            colRef
                    .whereEqualTo(property, query.getValue())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    matches.add(document.getData());
                                }
                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
//                            Map<String, Object> nullMap = new HashMap<String, Object>();
//                            nullMap.put("null", "null");
//                            matches.add(nullMap);
                            }
                            callback.callbackCall(matches);
                        }
                    });
        }
    }

    public static void getCollection(String collection, final CallBackList<Map<String, Object>> callback) {
        db.collection(collection)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    List<Map<String, Object>> list = new ArrayList<>();

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.getData());
                                Log.d(TAG, document.getData() + " => " + document.getData());
                                callback.callbackCall(list);
                            }
                        } else {
                            Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });

    }

}