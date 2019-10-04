package com.theenigma.pivot.Auth.AlumniAuth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.theenigma.pivot.Alumni;
import com.theenigma.pivot.Auth.FormHelper;
import com.theenigma.pivot.Auth.RegisterCredentialFragment;
import com.theenigma.pivot.MainActivity;
import com.theenigma.pivot.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import io.reactivex.annotations.NonNull;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class AlumniDescribeFragment extends Fragment {


    public AlumniDescribeFragment() {
        // Required empty public constructor
    }

    View view;

    private static final int SELECT_PICTURE = 100;

    AppCompatButton nextButton, uploadButton;
    AppCompatEditText inputInterests, inputEducationalBackground;
    AppCompatImageView imageView;

    Uri localImageUri, downloadUrl = null;
    Bitmap compressedImageFile;
    StorageReference storageRef;
    byte[] compressedImageData;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_alumni_describe, container, false);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);

        inputEducationalBackground = view.findViewById(R.id.inputEducation);
        inputInterests = view.findViewById(R.id.inputInterest);
        imageView = view.findViewById(R.id.profileView);
        nextButton = view.findViewById(R.id.nextButton);
        uploadButton = view.findViewById(R.id.uploadButton);

        uploadButton.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Alumni.alumni.getEmail();
                String password = RegisterCredentialFragment.password;
                FormHelper formHelper = new FormHelper();

                String interests = inputInterests.getText().toString().trim();
                String education = inputEducationalBackground.getText().toString().trim();
                if (formHelper.validateField(interests) && formHelper.validateField(education)) {
                    Alumni.alumni.setEducationParagraph(education);
                    Alumni.alumni.setInterests(interests);
                    createUser(email, password);
                } else
                    Toast.makeText(getContext(), "FIELDS EMPTY. PLEASE FILL.", Toast.LENGTH_LONG).show();
            }
            }
        );

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressImage();
                upload();
            }
        });

        return view;
    }

    private void openImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            //INITIATING CROPPING IMAGE INTENT.
            CropImage
                    .activity(imageUri)
                    .start(getContext(), this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            //SETTING CROPPED IMAGE INTO IMAGEVIEW
            if(result != null) {
                Uri selectedImage = result.getUri();

                Glide
                        .with(getContext())
                        .load(new File(selectedImage.getPath())) // Uri of the picture
                        .into(imageView);

                setLocalImageUri(selectedImage);

                uploadButton.setVisibility(View.VISIBLE);
            }else{
                Toast.makeText(getContext(), "TRY AGAIN!" , Toast.LENGTH_LONG).show();
            }
        }
    }

    public Uri getLocalImageUri() {
        return localImageUri;
    }

    public void setLocalImageUri(Uri uri) {
        localImageUri = uri;
    }

    public byte[] getCompressedImageData() {
        return compressedImageData;
    }

    public void setCompressedImageData(byte[] data) {
        compressedImageData = data;
    }

    public void setDownloadUrl(Uri uri) {
        downloadUrl = uri;
    }

    public void compressImage() {

        File newImageFile = new File(getLocalImageUri().getPath());
        try {

            compressedImageFile = new Compressor(getContext())
                    .setMaxHeight(96)
                    .setMaxWidth(96)
                    .compressToBitmap(newImageFile);

        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();
        setCompressedImageData(imageData);

        Glide
                .with(getContext())
                .load(compressedImageFile)
                .into(imageView);

    }

    private void upload() {
        final StorageReference ref = storageRef.child("PROFILE_PICTURES/" + Alumni.alumni.getEmail());
        UploadTask uploadTask = ref.putBytes(getCompressedImageData());

        progressDialog.show();


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return ref.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    setDownloadUrl(downloadUri);
                    progressDialog.hide();
                    Alumni.alumni.setDownloadURL(downloadUrl.toString());
                } else {
                    // Handle failures
                    // ...
                    progressDialog.hide();
                    Toast.makeText(getContext(), "UPLOAD FAILED.PLEASE TRY AGAIN OR RESTART APP.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void createUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("msg", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            createFirestore(user.getUid());
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(Alumni.alumni.getName())
                                    .setPhotoUri(downloadUrl)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("msg", "User profile updated.");
                                            }else{
                                                //HANDLE FAILURES
                                            }
                                        }
                                    });

                            Toast.makeText(getContext(), "Authentication SUCCESS.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("msg", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressDialog.hide();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    private void createFirestore(String UID) {

        Alumni.alumni.setUserType("ALUMNI");
        Alumni.alumni.setBuffer("3.0");

        db.collection("USER").document(UID)
                .set(Alumni.alumni)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("msg", "DocumentSnapshot successfully written!");
                        progressDialog.hide();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("msg", "Error writing document", e);
                        progressDialog.hide();
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("msg", "UPLOADED THE FACULTY.");
                            progressDialog.hide();
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }
                    }
                });
    }


}

