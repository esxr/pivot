package com.example.icasapp.Firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseHelperKotlin {
    val TAG = "Kotlin FirebaseHelper"
    private val db = FirebaseFirestore.getInstance()


    fun addDocumenttoCollection(document: Do, collection: Collection) {
        db.collection("cities").document("LA")
                .set(city)
                .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }
}