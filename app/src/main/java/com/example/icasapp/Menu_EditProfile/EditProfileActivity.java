package com.example.icasapp.Menu_EditProfile;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.Notes.NotesForm;
import com.example.icasapp.Notes.NotesFragment;
import com.example.icasapp.R;
import com.example.icasapp.User.TestUser;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    Spinner semesterSpinner;
    Spinner streamSpinner;
    EditText inputDisplayName;
    ImageView profileView;
    Uri image;
    String stream, semester;
    private Uri postImageUri = null;

    String DOWNLOAD_URL;

    ProgressDialog progressBar;


    FirebaseUser user;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        semesterSpinner = findViewById(R.id.spinnerSemester);
        streamSpinner = findViewById(R.id.spinnerStream);
        profileView = findViewById(R.id.profileView);

        user = FirebaseAuth.getInstance().getCurrentUser();

        db = FirebaseFirestore.getInstance();


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.semester_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        semesterSpinner.setAdapter(adapter);

        inputDisplayName = findViewById(R.id.inputDisplayName);


        ArrayAdapter<CharSequence> adapterStream = ArrayAdapter.createFromResource(this,
                R.array.stream_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterStream.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        streamSpinner.setAdapter(adapterStream);

        semesterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int position, long id) {
                Log.v("SpinnerSelected Item",
                        "" + semesterSpinner.getSelectedItem());
                Log.v("Clicked position", "" + position);
                setSemester(semesterSpinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Log.v("NothingSelected Item",
                        "" + semesterSpinner.getSelectedItem());
                setSemester(semesterSpinner.getSelectedItem().toString());
            }
        });

        streamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View v,
                                       int position, long id) {
                Log.v("SpinnerSelected Item",
                        "" + streamSpinner.getSelectedItem());
                Log.v("Clicked position", "" + position);
                setStream(streamSpinner.getSelectedItem().toString());

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                Log.v("NothingSelected Item",
                        "" + semesterSpinner.getSelectedItem().toString());
                setStream(streamSpinner.getSelectedItem().toString());

            }
        });

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("File UPLOADING ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.setProgress(0);//initially progress is 0

    }

    public void uploadImage(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);

    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case 1:
                if (resultCode == RESULT_OK) {
                    try {
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                        profileView.setImageBitmap(selectedImage);
                        setImage(imageUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
        }
    }*/

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //ACTIONS TO DO IF THE FILE HAS BEEN SUCCESSFULLY SELECTED.
        if (requestCode == 1 && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            setImage(data.getData());
            imgFirebase();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void setImage(Uri image) {
        this.image = image;
    }

    public Uri getImage() {
        return this.image;
    }

    public String getUsername() {
        return inputDisplayName.getText().toString();
    }

    public void setStream(String stream) {
        this.stream = stream;
    }

    public String getStream() {
        return this.stream;

    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getSemester() {
        return this.semester;

    }

    public void setDownloadURL(String DOWNLOAD_URL){
        this.DOWNLOAD_URL = DOWNLOAD_URL;

    }

    public String getDownloadURL(){
        return this.DOWNLOAD_URL;
    }

    public void updateProfile() {
        // [START update_profile]
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(getUsername())
                .setPhotoUri(Uri.parse(getDownloadURL()))
                .build();


        Map<String, Object> USER = new HashMap<>();
        USER.put("name", getUsername());
        USER.put("semester", getSemester());
        USER.put("stream", getStream());
        USER.put("downloadURL", getDownloadURL());

        Log.i("msg", "USER ID" + user.getUid());
        Log.i("msg", "SEMESTER" + getSemester());
        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("USER").document(user.getUid())
                .set(USER, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Changes applied", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void approveChanges(View view) {
        updateProfile();
    }

    public void imgFirebase() {
        final StorageReference ref = FirebaseStorage.getInstance().getReference().child("PROFILE_PICTURES/" + user.getUid());
        UploadTask uploadTask = ref.putFile(getImage());

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
                    setDownloadURL(downloadUri.toString());

                    Log.i("msg", downloadUri.toString());
                    Log.i("msg", "UPLOAD COMPLETED.");
                    //----------------------------------EXIT POINT OF UPLOAD --------------------------------------------------------Toast.makeText(getApplicationContext(), "SUCCESSFULLY UPLOADED TO THE DATABASE.", Toast.LENGTH_LONG).show();


                } else {
                    // Handle failures
                    // ...
                    Log.i("msg", "UPLOAD FAILURE.");

                }
            }


        });
    }
}

