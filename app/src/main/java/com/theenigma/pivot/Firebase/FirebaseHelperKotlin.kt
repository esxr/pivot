package com.theenigma.pivot.Firebase

import androidx.multidex.MultiDexApplication
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseHelperKotlin : MultiDexApplication() {

    // getters and setters

    // static methods
    // document manipulation

    companion object {
        val TAG = "Kotlin FirebaseHelper" // DEBUG PURPOSES
        val db: FirebaseFirestore = FirebaseFirestore.getInstance()

        fun testFunction(): Int { return 0 }

        fun getFirestore(): FirebaseFirestore {
            return db
        }

        fun addDocumentToCollection(document: HashMap<String, String>, collection: String) {
            getFirestore().collection(collection).document("LA")
                    .set(document)
                    .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                    .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
        }

        fun getDocumentFromCollection(document: String, collection: String): HashMap<String, Any?> {
            var data: HashMap<String, Any?> = hashMapOf("data" to "null")
            getFirestore().collection(collection).document(document).get()
                    .addOnSuccessListener { result ->
                        if (result != null) {
                            data = hashMapOf(
                                    "name" to result.data,
                                    "semester" to result.data,
                                    "stream" to result.data
                            )
                        } else {
                            Log.d(TAG, "No such document")
                        }
                    }
                    .addOnFailureListener { exception ->
                        Log.d(TAG, "get failed with ", exception)
                    }
            return data
        }
    }


}
