package com.example.icasapp.Notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import com.example.icasapp.MainActivity;
import com.example.icasapp.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import java.util.ArrayList;

public class NotesViewActivity extends AppCompatActivity {


    public ArrayList<String> arrayList, semesterList, sessionalList, subjectList, subjectAbr;


    FirebaseFirestore db;
    String TAG = "msg";
    StorageReference storageRef;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    NotesAdapter notesAdapter;

    EditText editText;

    ArrayList<Uri> DOWNLOAD_URL_LIST;

    boolean isFirstPageLoad = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_view);

        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        getSupportActionBar().hide();

        arrayList = new ArrayList<>();
        semesterList = new ArrayList<>();
        sessionalList = new ArrayList<>();
        subjectList = new ArrayList<>();
        subjectAbr = new ArrayList<>();
        DOWNLOAD_URL_LIST = new ArrayList<>();
        getdocumentList("NOTES");

        recyclerView = findViewById(R.id.recycler_view);

        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        editText = findViewById(R.id.editText);

        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                notesAdapter.setFilter(editText.getText().toString());


            }
        });


    }

    public void getdocumentList(final String collection) {

        db = FirebaseFirestore.getInstance();

        db.collection("NOTES")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "listen:error", e);
                            return;
                        }

                        for (DocumentChange dc : snapshots.getDocumentChanges()) {
                            DocumentSnapshot document = dc.getDocument();
                            arrayList.add(dc.getDocument().getId());
                            switch (dc.getType()) {
                                case ADDED:
                                    if (isFirstPageLoad) {
                                        semesterList.add(document.get("semester").toString());
                                        sessionalList.add(document.getData().get("sessional").toString());
                                        subjectAbr.add(document.getData().get("subject_abr").toString());
                                        subjectList.add(document.getData().get("subject").toString());
                                        DOWNLOAD_URL_LIST.add(Uri.parse(document.getData().get("downloadURL").toString()));

                                        addData(semesterList, sessionalList, arrayList, subjectAbr, subjectList, DOWNLOAD_URL_LIST);
                                    }
                                    break;
                                case MODIFIED:
                                    Log.d(TAG, "Modified : " + dc.getDocument().getData());
                                    notesAdapter.notifyDataSetChanged();
                                    break;
                                case REMOVED:
                                    Log.d(TAG, "Removed : " + dc.getDocument().getData());
                                    Intent intent = new Intent(getApplicationContext(),NotesViewActivity.class);
                                    startActivity(intent);
                                    finish();
                                    notesAdapter.notifyDataSetChanged();
                                    break;
                            }
                        }

                    }
                });


    }


    public void addData(
            ArrayList<String> semesterList,
            ArrayList<String> sessionalList,
            ArrayList<String> arrayList,
            ArrayList<String> subjectAbr,
            ArrayList<String> subjectList,
            ArrayList<Uri> DOWNLOAD_URL_LIST
    ) {

        notesAdapter = new NotesAdapter(
                arrayList,
                semesterList,
                sessionalList,
                subjectList,
                subjectAbr,
                DOWNLOAD_URL_LIST
        );
        Log.i("msg", "USER GIVEN DOWNLOAD LIST:" + DOWNLOAD_URL_LIST.toString());

        recyclerView.setAdapter(notesAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));


    }
}







