package com.example.icasapp.Notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.algolia.search.saas.Client;
import com.algolia.search.saas.Index;
import com.example.icasapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NotesFragment extends Fragment {

    View notesView;

    Uri DATA;

    Query query;

    private FirebaseFirestore db;
    private CollectionReference notesRef;
    private NotesAdapter adapter;

    FloatingActionButton floatingActionButton;

    RecyclerView recyclerView;
    EditText editText;
    RecyclerView.LayoutManager layoutManager;
    boolean isFilterActive;
    //NotesAdapter notesAdapter;


    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        notesView = inflater.inflate(R.layout.fragment_notes, container, false);
        db = FirebaseFirestore.getInstance();

        isFilterActive = false;

        notesRef = db.collection("NOTES");

        floatingActionButton = notesView.findViewById(R.id.addNotes);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });

        setUpRecyclerView();

        editText = notesView.findViewById(R.id.editText);

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
                setFilter(s.toString(), "fileName");

            }
        });
        return notesView;
    }

    public void setFilter(String s, String filterCategory) {
        query = notesRef.orderBy(filterCategory).startAt(s).endAt(s + "\uf8ff");

        if (!s.trim().isEmpty()) {
            setUpRecyclerView();
            adapter.startListening();
        } else {
            isFilterActive = false;
            setUpRecyclerView();
            adapter.startListening();
        }
    }


    //ON UPLOAD BUTTON CLICK , CHOOSE FILE
    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");      //all files
        //intent.setType("file/pdf");   //PDF file only
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    //RESULT AFTER USER SELECTED FILE
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ACTIONS TO DO IF THE FILE HAS BEEN SUCCESSFULLY SELECTED.
        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            DATA = data.getData();

            Intent intent = new Intent(getContext(), NotesForm.class);
            intent.putExtra("SELECTED_FILE_DATA", DATA.toString());
            startActivity(intent);

        }
    }

    private void setUpRecyclerView() {

        if (!isFilterActive) {
            query = notesRef.orderBy("fileName", Query.Direction.ASCENDING);
        }
        Log.i("msg", "QUERY:" + query.toString());

        FirestoreRecyclerOptions<Notes> options = new FirestoreRecyclerOptions.Builder<Notes>()
                .setQuery(query, Notes.class)
                .build();


        adapter = new NotesAdapter(options);
        recyclerView = notesView.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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






