package com.example.icasapp.Notes;

import android.net.Uri;
import androidx.annotation.NonNull;
import android.util.Log;

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
import java.util.List;

public class NotesAbstraction {

    FirebaseFirestore db;
    StorageReference storageRef;
    List<String> notesList;
    String TAG;
    DocumentSnapshot doc;


    public NotesAbstraction() {
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        notesList = new ArrayList<>();
        TAG = "msg";
    }




    public List<String> getNotesNameList() {
        db.collection("NOTES")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                                notesList.add(document.getId());
                        }
                    }

                });
        return notesList;

    }

    public void setNotesName(String notesName){
        doc = getDocumentRef(notesName);
    }

    public int returnNotesNamePosition(String notesName){
        return notesList.indexOf(notesName);
    }


    public Uri getNotesDownloadURL() {
        return Uri.parse(doc.getData().get("downloadURL").toString());
    }

    public String getNotesAuthor( ) {
        return doc.getData().get("username").toString();

    }

    public String getNotesSemester() {
        return doc.getData().get("semester").toString();

    }

    public String getNotesSessional() {
        return doc.getData().get("sessional").toString();

    }

    public String getNotesOriginalFileName() {
        return doc.getData().get("originalFileName").toString();

    }

    public String getNotesSubjectName(){
        return doc.getData().get("subject").toString();
    }

    public String getSubjectAbbreviation(){
        return doc.getData().get("subject_abr").toString();
    }

    public int totalNumberOfNotes() {
        return notesList.size();
    }




     DocumentSnapshot getDocumentRef(String notesName) {
        DocumentReference docRef = db.collection("NOTES").document(notesName);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        doc = document;
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
        return doc;
    }
}
