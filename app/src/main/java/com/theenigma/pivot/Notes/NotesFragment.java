package com.theenigma.pivot.Notes;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.theenigma.pivot.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.theenigma.pivot.R.drawable.grid;

public class NotesFragment extends Fragment {

    private FirebaseFirestore db;

    private static final int ROW_ITEMS = 1;

    private String SEMESTER = "", STREAM = "";

    private ArrayList<String> items;

    Uri DATA;
    GridView grid;

    View notesView;

    FloatingActionButton floatingActionButton;

    String  buffer;

    public NotesFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        notesView = inflater.inflate(R.layout.fragment_notes, container, false);

        db = FirebaseFirestore.getInstance();

        grid = notesView.findViewById(R.id.grid);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                            Log.i("msg", "CLICKED:" + items.get(i));
                                            Intent intent = new Intent(getContext(), NotesActivity.class);
                                            intent.putExtra("subject", items.get(i));
                                            startActivity(intent);


                                        }
                                    });

                items = new ArrayList<String>();

        floatingActionButton = notesView.findViewById(R.id.addNotes);
        floatingActionButton.hide();

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });


        db.collection("USER").document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                buffer = document.get("buffer").toString();
                                Log.i("msg", "BUFFER:" + buffer);
                                if (buffer.isEmpty() || buffer == null || buffer.equals("1.0") || buffer.equals("3.0"))
                                    floatingActionButton.hide();
                                else
                                    floatingActionButton.show();
                            }
                        }
                    }
                });



        db
                .collection("USER")
                .whereEqualTo("email", FirebaseAuth.getInstance().getCurrentUser().getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("msg", document.getId() + " => " + document.getData());
                                SEMESTER = document.get("semester").toString();
                                STREAM = document.get("stream").toString();
                                addItems();
                            }
                        }
                    }
                });




        return  notesView;
    }

    private void addItems() {
        db.collection("Specific")
                .whereEqualTo("semester", SEMESTER)
                .whereEqualTo("stream", STREAM)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("msg", document.getData().get("subjects").toString());
                                ArrayList subjects = (ArrayList) document.getData().get("subjects");
                                for(Object subject: subjects) {
                                    items.add(subject.toString());
                                }
                                items.add("Others");

                                grid.setAdapter(new GridAdapter(items));
                            }
                        } else {
                            Log.d("msg", "Error getting documents: ", task.getException());
                        }
                    }
                });
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
            intent.putExtra("SEMESTER", SEMESTER);
            intent.putExtra("STREAM", STREAM);
            intent.putExtra("SELECTED_FILE_DATA", DATA.toString());
            intent.putExtra("SUBJECTS", items);
            startActivity(intent);

        }
    }

    private class GridAdapter extends BaseAdapter {

        final int mCount;

        private GridAdapter(final ArrayList<String> items) {

            mCount = items.size() * ROW_ITEMS;

        }

        @Override
        public int getCount() {
            return mCount;
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View convertView, ViewGroup parent) {
            View view = convertView;



            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            }

            final TextView text = (TextView) view.findViewById(android.R.id.text1);

            text.setText(items.get(i));
            text.setAllCaps(true);
            text.setTextSize(15);
            text.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            text.setPadding(15,105,15,105);
            view.setElevation(80);
            view.setBackground(ContextCompat.getDrawable(getContext(),R.drawable.grid));
            view.setMinimumHeight(400);




            return view;
        }
    }
}










