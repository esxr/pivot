package com.theenigma.pivot.Notes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.theenigma.pivot.R;

import static android.app.Activity.RESULT_OK;

public class NotesFragment extends Fragment {

    View notesView;
//
//    Uri DATA;
//
//    Query query;
//    RecyclerView recyclerView;
//    AutoCompleteTextView editText;
//    RecyclerView.LayoutManager layoutManager;
//    boolean isFilterActive;
//    String buffer;
//    //    //NotesAdapter notesAdapter;
//    FirestoreRecyclerOptions<Notes> options;
//    private FirebaseFirestore db;
//    private CollectionReference notesRef;
//    private NotesAdapter adapter;
//    private FloatingActionButton floatingActionButton;

    public NotesFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        notesView = inflater.inflate(R.layout.fragment_notes, container, false);
        return notesView;


    }

    void first(View view){
      Log.i("msg", "BUTTON CLICKED 1");
    }

    void second(View view){
        Log.i("msg", "BUTTON CLICKED 2");
    }

    void third(View view){
        Log.i("msg", "BUTTON CLICKED 3");
    }

    void fourth(View view){
        Log.i("msg", "BUTTON CLICKED 4");
    }


}


        // Inflate the layout for this fragment
//        notesView = inflater.inflate(R.layout.fragment_notes, container, false);
//        db = FirebaseFirestore.getInstance();
//
//        isFilterActive = false;
//
//        notesRef = db.collection("NOTES");
//
//        floatingActionButton = notesView.findViewById(R.id.addNotes);
//
//        //UPLOAD BUTTON INITIALLY HIDDEN
//        floatingActionButton.hide();
//
//        //USER PERMS FETCHED TO DECIDE VISIBILITY OF UPLOAD BUTTON
//        db
//                .collection("USER")
//                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                        if (task.isSuccessful()) {
//                            DocumentSnapshot document = task.getResult();
//                            if (document.exists()) {
//                                buffer = document.get("buffer").toString();
//                                Log.i("msg", "BUFFER:" + buffer);
//                                if (buffer.isEmpty() || buffer == null || buffer.equals("1.0") || buffer.equals("3.0"))
//                                    floatingActionButton.hide();
//                                else
//                                    floatingActionButton.show();
//                            }
//                        }
//                    }
//                });
//
//
//        db
//                .collection("NOTES")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot document : task.getResult())
//                                Log.d("msg", document.getId() + " => " + document.getData());
//                        }
//                    }
//                });
//
//        floatingActionButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                showFileChooser();
//            }
//        });
//        setUpRecyclerView();
//        editText = notesView.findViewById(R.id.edit);
//
//
//        final ArrayAdapter<String> autoComplete = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1);
//
//        FirebaseFirestore.getInstance().collection("NOTES").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (DocumentSnapshot documentSnapshot : task.getResult()) {
//                    autoComplete.add(documentSnapshot.get("fileName").toString());
//                }
//            }
//        });
//
//        editText.setAdapter(autoComplete);
//        editText.addTextChangedListener(new TextWatcher() {
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start,
//                                          int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start,
//                                      int before, int count) {
//                isFilterActive = true;
//                setFilter(s.toString().trim());
//            }
//        });
//        return notesView;
//    }
//
//    private void setFilter(String s) {
//        query = notesRef.orderBy("fileName").startAt(s).endAt(s + "\uf8ff");
//
//        if (!s.trim().isEmpty()) {
//            setUpRecyclerView();
//            adapter.startListening();
//        } else {
//            isFilterActive = false;
//            setUpRecyclerView();
//            adapter.startListening();
//        }
//    }
//
//
//    //ON UPLOAD BUTTON CLICK , CHOOSE FILE
//    private void showFileChooser() {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("*/*");      //all files
//        //intent.setType("file/pdf");   //PDF file only
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        try {
//            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), 1);
//        } catch (android.content.ActivityNotFoundException ex) {
//            // Potentially direct the user to the Market with a Dialog
//            Toast.makeText(getContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    //RESULT AFTER USER SELECTED FILE
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        //ACTIONS TO DO IF THE FILE HAS BEEN SUCCESSFULLY SELECTED.
//        if (requestCode == 1 && resultCode == RESULT_OK
//                && data != null && data.getData() != null) {
//            DATA = data.getData();
//
//            Intent intent = new Intent(getContext(), NotesForm.class);
//            intent.putExtra("SELECTED_FILE_DATA", DATA.toString());
//            startActivity(intent);
//
//        }
//    }
//
//    private void setUpRecyclerView() {
//
//        if (!isFilterActive) {
//            query = notesRef.orderBy("fileName", Query.Direction.ASCENDING);
//        }
//        Log.i("msg", "QUERY:" + query.toString());
//
//        options = new FirestoreRecyclerOptions.Builder<Notes>()
//                .setQuery(query, Notes.class)
//                .build();
//
//
//        adapter = new NotesAdapter(options);
//        recyclerView = notesView.findViewById(R.id.recycler_view);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//        recyclerView.setAdapter(adapter);
//
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//
//        adapter.startListening();
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        adapter.stopListening();
//    }


//}






