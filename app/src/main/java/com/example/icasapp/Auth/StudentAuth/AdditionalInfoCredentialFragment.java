package com.example.icasapp.Auth.StudentAuth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.icasapp.MainActivity;
import com.example.icasapp.Student;
import com.example.icasapp.R;
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

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.annotations.NonNull;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdditionalInfoCredentialFragment extends Fragment {

    private static final int SELECT_PICTURE = 100;
    View view;
    CircleImageView imageView;
    AppCompatButton uploadButton, skipButton;
    Uri localImageUri, downloadUrl = null;
    Bitmap compressedImageFile;
    StorageReference storageRef;
    byte[] compressedImageData;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

    ProgressDialog progressDialog;

    public AdditionalInfoCredentialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_subject_semester_credential, container, false);

        imageView = view.findViewById(R.id.profileView);
        uploadButton = view.findViewById(R.id.uploadButton);
        uploadButton.setVisibility(View.INVISIBLE);
        storageRef = FirebaseStorage.getInstance().getReference();
        skipButton = view.findViewById(R.id.nextButton);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Student.student.getEmail();
                String password = UserRegCredentialFragment.password;
                createUser(email, password);

            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressImage();
                upload();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
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
            }else
                Toast.makeText(getContext(), "TRY AGAIN!" , Toast.LENGTH_LONG).show();
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
        progressDialog.show();
        final StorageReference ref = storageRef.child("PROFILE_PICTURES/" + Student.student.getRegNo());
        UploadTask uploadTask = ref.putBytes(getCompressedImageData());

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
                    progressDialog.hide();
                    setDownloadUrl(downloadUri);
                    Student.student.setDownloadURL(downloadUrl.toString());
                } else {
                    // Handle failures
                    // ...
                    progressDialog.hide();
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
                            Student.student.setBuffer("1.0");
                            createFirestore(user.getUid());
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(Student.student.getName())
                                    .setPhotoUri(downloadUrl)
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

    private void createFirestore(String UID){

        Student.student.setUserType("STUDENT");
        String substring = Student.student.getRegNo().substring(4, 6);
        switch (substring){
            case "23":
                Student.student.setStream("Aero");
                break;
            case "25":
                Student.student.setStream("Chem");
                break;
            case "26":
                Student.student.setStream("Civil");
                break;
            case "27":
                Student.student.setStream("CSE");
                break;
            case "29":
                Student.student.setStream("Electrical");
                break;
            case "33":
                Student.student.setStream("Mechanical");
                break;
            case "34":
                Student.student.setStream("Mechatronics");
                break;
            default:
                Student.student.setStream("None");
        }

        Student.student.setSemester(Student.student.getRegNo().substring(0 , 2).contains("19")? "1" : "3");
        if(Student.student.getSemester().equals("1"))
            Student.student.setStream("None");

        Log.i("msg", Student.student.getSemester() + Student.student.getStream());

        db.collection("USER").document(UID)
                .set(Student.student)
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
                        progressDialog.hide();

                    }
                })
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Log.i("msg", "UPLOADED THE STUDENT.");
                            progressDialog.hide();
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }else{
                            progressDialog.hide();
                        }
                    }
                });
    }
}
