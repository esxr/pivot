package com.example.icasapp.Notes;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import com.example.icasapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;

public class NotesFragment extends Fragment {

    Button uploadButton;
    Button viewButton;
    View notesView;
    UploadTask uploadTask;
    StorageReference storageRef;
    String SEMESTER , SUBJECT , SESSIONAL, SUBJECT_ABBREVIATION , FILE_NAME_BY_USER;
    FirebaseFirestore db ;

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ACTIONS TO DO IF THE FILE HAS BEEN SUCCESSFULLY SELECTED.
        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            startActivity(new Intent(getContext() , NotesForm.class));
        }
    }



   //INSIDE THE INTENT AFTER CLICK ON UPLOAD BUTTON, HANDLING FILE TO BE UPLOADED AND PATH.


    //FIREBASE FILE UPLOAD FUNCTIONALITY.
    private void uploadFile(Uri DATA) {
        Log.i("msg" , "REACHED UPLOAD FILE FUNCTION.");

        String filename =  new FileHelper().getFileName(DATA);
        Log.i("msg" , "INITIAL FILE NAME:"+ filename);
        if(FILE_NAME_BY_USER == null || FILE_NAME_BY_USER.equals("") || FILE_NAME_BY_USER.equals(" "))
            FILE_NAME_BY_USER = filename;

        final StorageReference ref = storageRef.child("NOTES/"+ DATA.toString());
        uploadTask = ref.putFile(DATA);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                Log.i("msg", "successful");
                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        });
        // Listen for state changes, errors, and completion of the upload
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                Log.i("msg" ,"Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("msg" ,"Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.i("msg" ,"Upload has failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
                Log.i("msg" ,"Upload has been successful.");
            }
        });


        urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.i("msg" , downloadUri.toString());
                    Log.i("msg" , "UPLOAD COMPLETED.");
                } else {
                    // Handle failures
                    // ...
                    Log.i("msg" , "UPLOAD FAILURE.");

                }
            }
        });
    }//END OF uploadFile() FUNCTION.

}






