package com.example.icasapp.Notes;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


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


    public ArrayList<String> arrayList, semesterList, sessionalList, subjectList, subjectAbr;
    FirebaseFirestore db;
    String TAG = "msg";
    StorageReference storageRef;

    static RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    static NotesAdapter notesAdapter;

    Uri DOWNLOAD_URL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        getSupportActionBar().hide(); //HIDING ACTION BAR

        arrayList = new ArrayList<>();
        semesterList = new ArrayList<>();
        sessionalList = new ArrayList<>();
        subjectList = new ArrayList<>();
        subjectAbr = new ArrayList<>();
        getdocumentList("NOTES");

        recyclerView = findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

    }

    public void getdocumentList(final String collection) {

        db = FirebaseFirestore.getInstance();
        //FETCHED DOCUMENT LIST FROM COLLECTION
        db.collection(collection).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //NAME OF EACH DOCUMENT
                        arrayList.add(document.getId());

                        //HASHMAP ELEMENT OF EACH DOCUMENT.
                        DocumentReference docRef = db.collection(collection).document(document.getId());
                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {

                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    if (document.exists()) {
                                        Log.d(TAG, document.getId() + " ===========> " + document.getData().get("semester").toString());
                                        Log.d(TAG, document.getId() + " ===========> " + document.getData().get("sessional").toString());
                                        semesterList.add(document.getData().get("semester").toString());
                                        sessionalList.add(document.getData().get("sessional").toString());
                                        subjectAbr.add(document.getData().get("subject_abr").toString());
                                        subjectList.add(document.getData().get("subject").toString());
                                        DOWNLOAD_URL = Uri.parse(document.getData().get("downloadURL").toString());

                                        addData(semesterList,sessionalList,arrayList, subjectAbr, subjectList, DOWNLOAD_URL);
                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                } else {
                                    Log.d(TAG, "get failed with ", task.getException());
                                }
                            }


                        });
                        //

                    }




                } else {
                    Log.d("msg", "Error getting documents: ", task.getException());
                }

                Log.d("msg", arrayList.toString());

                // This is the array adapter, it takes the context of the activity as a
                // first parameter, the type of list view as a second parameter and your
                // array as a third parameter.

            }
        });
    }
    public static void addData(ArrayList<String> semesterList,ArrayList<String> sessionalList,ArrayList<String> arrayList, ArrayList<String> subjectAbr, ArrayList<String> subjectList , Uri DOWNLOAD_URL){
        notesAdapter = new NotesAdapter(arrayList, semesterList, sessionalList, subjectList, subjectAbr, DOWNLOAD_URL);
        recyclerView.setAdapter(notesAdapter);
        Log.d("msg", "inside lol"+ semesterList.toString());
        Log.d("msg", "inside lol"+ sessionalList.toString());

    }


}









/*




    /*
    //FILE DOWNLOAD FUNCTION ON CLICK
    public void downloadFile(Uri url, String fileName) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(url);
        startActivity(i);
    }

}

/*
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            //ON ITEM CLICK,DOWNLOAD FILE -- CARDVIEW BUTTON
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("msg", "ARRAYLIST POSITION:" + position + "DATA:" + arrayList.get(position));
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
                                downloadFile(DOWNLOAD_URL, fileName);
                            } else {
                                Log.d("msg", "No such document");
                            }
                        } else {
                            Log.d("msg", "get failed with ", task.getException());
                        }
                    }
                });*/