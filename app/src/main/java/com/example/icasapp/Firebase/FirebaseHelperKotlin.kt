package com.example.icasapp.Firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseHelperKotlin {
    val TAG = "Kotlin FirebaseHelper"
    private val db = FirebaseFirestore.getInstance()

    // getters and setters


    // document manipulation
    fun addDocumentToCollection(document: HashMap<String, Object>, collection: String) {
        db.collection(collection).document("LA")
                .set(document)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }


}