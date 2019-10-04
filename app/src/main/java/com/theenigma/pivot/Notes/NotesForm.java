package com.theenigma.pivot.Notes;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;

import android.net.Uri;
import android.provider.OpenableColumns;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.theenigma.pivot.Firebase.FirebaseHelper;

import com.theenigma.pivot.MainActivity;
import com.theenigma.pivot.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class NotesForm extends AppCompatActivity {

    String SEMESTER, SUBJECT, SUBJECT_ABR, SESSIONAL, FILE_NAME_BY_USER;

    EditText SubjectText;
    EditText SubjectAbrText;
    EditText FileName;

    Spinner semesterSpinner;
    Spinner sessionalSpinner;

    List<String> semesterArrayList;
    List<String> sessionalArrayList;


    ArrayAdapter<String> spinnerArrayAdapter;

    StorageReference storageRef;

    public String filename;
    Button uploadButton;

    String value;

    FirebaseFirestore db;

    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_form);

        db = FirebaseFirestore.getInstance();



        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("File UPLOADING ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.setProgress(0);//initially progress is 0

        String v = "";
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            v = bundle.getString("SELECTED_FILE_DATA");
        }

        value = v;

        uploadButton = findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpload();
            }
        });


        storageRef = FirebaseStorage.getInstance().getReference();

        SubjectText = findViewById(R.id.subject);
        SubjectAbrText = findViewById(R.id.subject_abbreviation);
        FileName = findViewById(R.id.fileName);

        semesterSpinner = findViewById(R.id.semesterSpinner);
        sessionalSpinner = findViewById(R.id.sessionalSpinner);

        semesterArrayList = new ArrayList<>(Arrays.asList("SELECT SEMESTER...", "1", "2", "3", "4", "None"));
        sessionalArrayList = new ArrayList<>(Arrays.asList("SELECT SESSIONAL...", "1", "2", "Make Up", "None"));

        // Get reference of widgets from XML layout

        // Initializing an ArrayAdapter
        spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, semesterArrayList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        semesterSpinner.setAdapter(spinnerArrayAdapter);

        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                Log.i("msg", parent.getItemAtPosition(position).toString());
                SEMESTER = selectedItemText;

                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //SESSIONAL SPINNER LISTENERS----------

        // Get reference of widgets from XML layout

        // Initializing an ArrayAdapter
        spinnerArrayAdapter = new ArrayAdapter<String>(
                this, R.layout.support_simple_spinner_dropdown_item, sessionalArrayList) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        sessionalSpinner.setAdapter(spinnerArrayAdapter);

        sessionalSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position).toString();
                Log.i("msg", parent.getItemAtPosition(position).toString());
                SESSIONAL = selectedItemText;

                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getApplicationContext(), "Selected : " + selectedItemText, Toast.LENGTH_SHORT)
                            .show();


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onUpload() {

        SUBJECT = SubjectText.getText().toString();
        SUBJECT_ABR = SubjectAbrText.getText().toString();
        FILE_NAME_BY_USER = FileName.getText().toString();

        Log.i("msg", "SUBJECT DATA:" + SUBJECT + SUBJECT_ABR + FILE_NAME_BY_USER);
        if (SUBJECT == null || SUBJECT == "" || SUBJECT == " ") {
            SubjectText.requestFocus();
            Toast.makeText(getApplicationContext(), "ENTER SUBJECT NAME.", Toast.LENGTH_LONG).show();
        } else if (SUBJECT_ABR == null || SUBJECT_ABR == "" || SUBJECT_ABR == " ") {
            SubjectAbrText.requestFocus();
            Toast.makeText(getApplicationContext(), "ENTER SUBJECT ABBREVIARTION.", Toast.LENGTH_LONG).show();
        } else if (FILE_NAME_BY_USER == null || FILE_NAME_BY_USER == "" || FILE_NAME_BY_USER == " ") {
            FileName.requestFocus();
            Toast.makeText(getApplicationContext(), "ENTER FILE NAME.", Toast.LENGTH_LONG).show();
        } else if (SEMESTER == null || SEMESTER == "" || SEMESTER == " " || SEMESTER == "SELECT SEMESTER...") {
            Toast.makeText(getApplicationContext(), "SELECT SEMESTER.", Toast.LENGTH_LONG).show();
        } else if (SESSIONAL == null || SESSIONAL == "" || SESSIONAL == " " || SESSIONAL == "SELECT SESSIONAL...") {
            Toast.makeText(getApplicationContext(), "SELECT SESSIONAL.", Toast.LENGTH_LONG).show();
        } else {

            Log.i("msg", "SEMESTER ON CLICK: " + SEMESTER);
            Log.i("msg", "SESSIONAL ON CLICK: " + SESSIONAL);
            Log.i("msg", "SUBJECT ON CLICK: " + SUBJECT);
            Log.i("msg", "SUBJECT_ABR ON CLICK: " + SUBJECT_ABR);
            Log.i("msg", "FILE NAME ON CLICK: " + FILE_NAME_BY_USER);
            uploadFile(FILE_NAME_BY_USER);

        }


    }

    //INSIDE THE INTENT AFTER CLICK ON UPLOAD BUTTON, HANDLING FILE TO BE UPLOADED AND PATH.
    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        Log.i("FILE NAME IN HELPER:", result);
        return result;
    }


    //INSIDE THE INTENT AFTER CLICK ON UPLOAD BUTTON, HANDLING FILE TO BE UPLOADED AND PATH.


    //FIREBASE FILE UPLOAD FUNCTIONALITY.
    public void uploadFile(final String FILE_NAME_BY_USER) {
        Log.i("msg", "REACHED UPLOAD FILE FUNCTION.");

        Uri DATA = Uri.parse(value);

        filename = getFileName(DATA);
        Log.i("msg", "FILE NAME IN MOBILE" + filename);
        Log.i("msg", "INITIAL FILE NAME:" + filename);


        final StorageReference ref = storageRef.child("NOTES/" + FILE_NAME_BY_USER);
        UploadTask uploadTask = ref.putFile(DATA);



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
                Log.i("msg", "Upload is " + progress + "% done");
                progressBar.show();
                progressBar.setProgress((int) progress);
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                Log.i("msg", "Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                progressBar.hide();
                Toast.makeText(getApplicationContext(), "FILE UPLOAD FAILED.SOMETHING WENT WRONG", Toast.LENGTH_LONG).show();
                Log.i("msg", "Upload has failed");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // Handle successful uploads on complete
                // ...
                progressBar.setProgress(100);
                Toast.makeText(getApplicationContext(), "FILE SUCCESSFULLY UPLOADED", Toast.LENGTH_LONG).show();
                progressBar.hide();
                Log.i("msg", "Upload has been successful.");
            }
        });

        urlTask.addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    Log.i("msg", downloadUri.toString());
                    Log.i("msg", "UPLOAD COMPLETED.");
                    //----------------------------------EXIT POINT OF UPLOAD --------------------------------------------------------
                    uploadMetaToFirebase(SEMESTER, SESSIONAL, SUBJECT, SUBJECT_ABR, FILE_NAME_BY_USER, downloadUri);
                    Toast.makeText(getApplicationContext(), "SUCCESSFULLY UPLOADED TO THE DATABASE.", Toast.LENGTH_LONG).show();
                    try {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    // Handle failures
                    // ...
                    Log.i("msg", "UPLOAD FAILURE.");

                }
            }
        });
    }//END OF uploadFile() FUNCTION.

    //UPLOADING METADATA TO FIREBASE FIRESTOREBACKEND
    public void uploadMetaToFirebase(String SEMESTER, String SESSIONAL, String SUBJECT, String SUBJECT_ABR, String FILE_NAME_BY_USER, Uri DOWNLOAD_URL) {
        // Update one field, creating the document if it does not already exist.
        Map<String, String> data = new HashMap<>();
        data.put("fileName", FILE_NAME_BY_USER);
        data.put("semester", SEMESTER);
        data.put("sessional", SESSIONAL);
        data.put("subject", SUBJECT);
        data.put("subject_abr", SUBJECT_ABR);
        data.put("downloadURL", DOWNLOAD_URL.toString());
        data.put("originalFileName", filename);
        data.put("username", FirebaseHelper.getUser().getDisplayName());

        db.collection("NOTES").document(FILE_NAME_BY_USER)
                .set(data, SetOptions.merge());

    }


}

