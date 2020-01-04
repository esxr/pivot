package com.theenigma.pivot.Notes;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.theenigma.pivot.R;

public class NotesActivity extends AppCompatActivity {
    Query query;
    RecyclerView recyclerView;
    AutoCompleteTextView editText;
    RecyclerView.LayoutManager layoutManager;
    boolean isFilterActive;
    String message;
    //    //NotesAdapter notesAdapter;
    FirestoreRecyclerOptions<Notes> options;
    private FirebaseFirestore db;
    private CollectionReference notesRef;
    private NotesAdapter adapter;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        // Inflate the layout for this fragment
        db = FirebaseFirestore.getInstance();

        Bundle bundle = getIntent().getExtras();
        message = bundle.getString("subject");

        Log.i("msg", message);

        isFilterActive = false;

        notesRef = db.collection("NOTES");



        db
                .collection("NOTES")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                                Log.d("msg", document.getId() + " => " + document.getData());
                        }
                    }
                });


        setUpRecyclerView();
        editText = findViewById(R.id.edit);


        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_list_item_1);

        FirebaseFirestore.getInstance().collection("NOTES").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot documentSnapshot : task.getResult()) {

                    autoComplete.add(documentSnapshot.get("fileName").toString());
                }
            }
        });

        editText.setAdapter(autoComplete);


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
                isFilterActive = true;
                setFilter(s.toString().trim());

            }
        });
    }

    private void setFilter(String s) {
        query = notesRef.orderBy("fileName").startAt(s).endAt(s + "\uf8ff");

        if (!s.trim().isEmpty()) {
            setUpRecyclerView();
            adapter.startListening();
        } else {
            isFilterActive = false;
            setUpRecyclerView();
            adapter.startListening();
        }
    }






    private void setUpRecyclerView() {

        if (!isFilterActive) {
            query = notesRef.whereEqualTo("subject",message).orderBy("fileName", Query.Direction.ASCENDING);
        }
        Log.i("msg", "QUERY:" + query.toString());

        options = new FirestoreRecyclerOptions.Builder<Notes>()
                .setQuery(query, Notes.class)
                .build();


        adapter = new NotesAdapter(options);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();

        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }


}

/////
