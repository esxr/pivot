package com.theenigma.pivot.Firebase;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class SpiderDocument {

    public static void spider(CollectionReference collectionReference, int layers, final String option, final Boolean empty, final HashMap obj)
    {
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        collectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot documentSnapshot : task.getResult())
                {
                    Log.i("DEVOPS","Reached spider");
                    //deletes the documents that has no fields
                    if(option.equals("delete")&&empty) {
                        Map<String, Object> map = documentSnapshot.getData();
                        if(map.size()==0){
                            documentSnapshot.getReference().delete();
                        }

                        if(option.equals("delete")&&!empty){
                            documentSnapshot.getReference().delete();
                        }

                        if(option.equals("change"))
                        {
                            documentSnapshot.getReference().set(obj, SetOptions.merge());
                        }
                    }
                }
            }
        });
    }


}
