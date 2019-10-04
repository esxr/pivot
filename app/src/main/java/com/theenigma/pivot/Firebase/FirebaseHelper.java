package com.theenigma.pivot.Firebase;

import androidx.annotation.NonNull;

import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FirebaseHelper {

    static String TAG = "mfc";

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
        return FirebaseAuth.getInstance().getCurrentUser();
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

    public interface CallBackList<T> {
        void callbackCall(List<T> list);
    }

    public interface CallbackObject<T> {
        void callbackCall(T object);
    }

    public static void getUserDetails(final String UID, final CallbackObject<Map<String, Object>> callback) {
        Log.d(TAG, "getUserDetails() called");
        db.collection("USER").document(UID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    Log.e(TAG, "My UID: "+FirebaseHelper.getUser().getUid());
                    try { Log.e(TAG, "result: "+task.getResult().getData().toString()); } catch(Exception e) { e.printStackTrace(); }
                    callback.callbackCall(task.getResult().getData());
                }
            }
        });
    }

    public static void getDocumentFromCollectionWhere(String collection, String value, final CallbackObject<List<Map<String, Object>>> callback) {
        CollectionReference colRef = db.collection(collection);
        final List<Map<String, Object>> matches = new ArrayList<>();

        String[] properties = {"name", "regNo", "stream", "semester", "UID"};
        for (String property : properties) {
            colRef
                    .whereEqualTo(property, value)
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