package com.example.icasapp.Menu_EditProfile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

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
    }

    public void uploadImage(View view) {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, 1);

    }

    @Override
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
        String displayName = inputDisplayName.getText().toString();
        return displayName;
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

    public void updateProfile() {
        // [START update_profile]
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(getUsername())
                .setPhotoUri(getImage())
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {

                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("msg", "User profile updated.");
                        }
                    }
                });
        // [END update_profile]

        Map<String, Object> USER = new HashMap<>();
        USER.put("name", getUsername());
        USER.put("semester", getSemester());
        USER.put("stream", getStream());

        db.collection("USER").document(user.getUid())
                .set(USER)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("msg", "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("msg", "Error writing document", e);
                    }
                });
    }

    public void approveChanges(View view) {
        updateProfile();
    }


}
