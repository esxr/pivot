package com.example.icasapp.Notes;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.icasapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

/**

 * A simple {@link Fragment} subclass.

 */

public class NotesFragment extends Fragment {

    EditText inputFileName;
    Button uploadButton;
    Button viewButton;

    String NAME;
    String URL;


    DatabaseReference databaseReference;
    StorageReference storageReference;

    View notesView;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        NAME = URL = "";

        notesView = inflater.inflate(R.layout.fragment_notes, container, false);


        inputFileName = notesView.findViewById(R.id.inputFileName);
        uploadButton = notesView.findViewById(R.id.uploadButton);
        viewButton = notesView.findViewById(R.id.viewButton);

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("uploads");

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectDocument();
            }
        });

        viewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ViewNotesActivity.class);
                startActivity(intent);
            }
        });

        // Inflate the layout for this fragment
        return notesView;
    }


    //SELECT DOCUMENT FROM DEVIC FROM LOCAL DIRECTORY.
    private void selectDocument() {
        Intent intent = new Intent();
        //FOR PDF FILES
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "SELECT PDF FILE"), 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uploadPDFFile(data.getData());
        }
    }

    private void uploadPDFFile(Uri data) {
        //MANAGING PROGRESS AND STORAGE OF UPLOADED FILE INTO CLOUD

        StorageReference reference = storageReference.child("uploads/" + System.currentTimeMillis() + ".pdf");
        NAME = inputFileName.getText().toString();

        if (NAME.equals("")) {
            Toast.makeText(getContext(), "GIVE A FILE NAME TO THE PDF", Toast.LENGTH_LONG).show();
            inputFileName.requestFocus();
        } else {

            final ProgressDialog progressDialog = new ProgressDialog(this.getContext());
            progressDialog.setTitle("UPLOADING.....");
            progressDialog.show();

            reference.putFile(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uri = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uri.isComplete()) ;
                            Uri url = uri.getResult(); //URL OF FILE STORED IN DATABASE
                            URL = url.toString();

                            DocumentHelper documentHelper = new DocumentHelper(NAME, URL);
                            databaseReference.child(databaseReference.push().getKey()).setValue(documentHelper);

                            Toast.makeText(getContext(), "UPLOADED SUCCESSFULLY", Toast.LENGTH_LONG).show();
                            progressDialog.dismiss();

                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                            double progress = (100 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                            progressDialog.setMessage("UPLOADED :" + (int) progress + "%");
                        }
                    })
            ;

        }


    }
}