package com.example.icasapp.Notes;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.icasapp.R;

import com.google.firebase.firestore.FirebaseFirestore;

import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class NotesFragment extends Fragment {

    Button uploadButton;
    Button viewButton;
    View notesView;
    StorageReference storageRef;
    FirebaseFirestore db ;
    Uri DATA;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        notesView = inflater.inflate(R.layout.fragment_notes, container, false);

        uploadButton = notesView.findViewById(R.id.uploadButton);
        viewButton = notesView.findViewById(R.id.viewButton);

        storageRef = FirebaseStorage.getInstance().getReference();

        db = FirebaseFirestore.getInstance();



        //FUNCTIONALITY WHEN UPLOAD BUTTON CLICKED.
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });
        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return notesView;
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
            intent.putExtra("SELECTED_FILE_DATA" , DATA.toString());
            startActivity(intent);

        }
    }







}






