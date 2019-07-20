package com.example.icasapp.Notes;



import android.app.ProgressDialog;

import android.net.Uri;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;



import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class NotesViewActivity extends AppCompatActivity {

    ListView lv;

    public ArrayList<String> arrayList;
    FirebaseFirestore db;

    StorageReference storageRef;

    Uri DOWNLOAD_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        getSupportActionBar().hide(); //HIDING ACTION BAR



        arrayList = new ArrayList<>();
        getdocumentList("NOTES");

        lv = findViewById(R.id.listView);



        Log.i("msg" , "ARRAYLIST: "+ arrayList.toString());


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("msg" , "ARRAYLIST POSITION:"+position+ "DATA:"+ arrayList.get(position));
                DocumentReference docRef = db.collection("NOTES").document(arrayList.get(position));
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d("msg", "DocumentSnapshot data: " + document.getData().get("downloadURL"));
                                DOWNLOAD_URL = Uri.parse(document.getData().get("downloadURL").toString());
                                String fileName = document.getData().get("originalFileName").toString();
                                downloadFile(DOWNLOAD_URL , fileName);
                            } else {
                                Log.d("msg", "No such document");
                            }
                        } else {
                            Log.d("msg", "get failed with ", task.getException());
                        }
                    }
                });



            }
        });
    }

    public void getdocumentList(String collection){
        db.collection(collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        arrayList.add(document.getId());
                    }
                    Log.d("msg", arrayList.toString());

                    // This is the array adapter, it takes the context of the activity as a
                    // first parameter, the type of list view as a second parameter and your
                    // array as a third parameter.

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                            getApplicationContext(),
                            android.R.layout.simple_list_item_1,
                            arrayList );

                    lv.setAdapter(arrayAdapter);
                } else {
                    Log.d("msg", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void downloadFile(Uri url, String fileName){

    }

}

