package com.example.icasapp.Firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseHelperKotlin {

    // getters and setters

    // static methods
    // document manipulation
    companion object {
        val TAG = "Kotlin FirebaseHelper" // DEBUG PURPOSES
        private val db = FirebaseFirestore.getInstance()

        fun getFirestore(): FirebaseFirestore {
            return db;
        }

        fun addDocumentToCollection(document: HashMap<String, String>, collection: String): Boolean {
            getFirestore().collection(collection).document("LA")
                    .set(document)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
            return true
        }
    }


}
