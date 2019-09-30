package com.example.icasapp.Auth.FacultyAuth;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.icasapp.Auth.FormHelper;
import com.example.icasapp.Auth.RegisterCredentialFragment;
import com.example.icasapp.Faculty;
import com.example.icasapp.MainActivity;
import com.example.icasapp.R;
import com.example.icasapp.Student;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.annotations.NonNull;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FacultyDescribeFragment extends Fragment {


    private static final int SELECT_PICTURE = 100;
    View view;
    AppCompatButton nextButton, uploadButton;
    AppCompatEditText inputSubjects, inputInterests;
    CircleImageView imageView;
    private AlertDialog.Builder builder;
    private ArrayList selectedItemList;
    private Boolean firstClicked = true;

    Uri localImageUri, downloadUrl = null;
    String id;
    Bitmap compressedImageFile;
    StorageReference storageRef;
    byte[] compressedImageData;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    AppCompatButton Select_sub;
    FirebaseFirestore firebaseFirestore;
    ArrayList subs;

    public FacultyDescribeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_faculty_describe, container, false);

        inputInterests = view.findViewById(R.id.inputInterest);
        inputSubjects = view.findViewById(R.id.inputSubjects);
        imageView = view.findViewById(R.id.profileView);
        nextButton = view.findViewById(R.id.nextButton);
        uploadButton = view.findViewById(R.id.uploadButton);
        Select_sub = view.findViewById(R.id.Select_Sub);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setProgress(0);
        progressDialog.setMax(100);

        uploadButton.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        subs = new ArrayList();


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = RegisterCredentialFragment.email;
                String password = RegisterCredentialFragment.password;
                FormHelper formHelper = new FormHelper();

                String interests = inputInterests.getText().toString().trim();
              //  String subjects = inputSubjects.getText().toString().trim();
                if (formHelper.validateField(interests)||selectedItemList.isEmpty()) {
                //    Faculty.faculty.setSubjects(subjects);
                    Faculty.faculty.setInterests(interests);
                    Faculty.faculty.setSubjects(selectedItemList);
                    createUser(email, password);
                } else
                    Toast.makeText(getContext(), "FIELDS EMPTY. PLEASE FILL.", Toast.LENGTH_LONG).show();
            }
        });

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

        firebaseFirestore = FirebaseFirestore.getInstance();


        builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Choose your subjects");
        firebaseFirestore.collection("Specific").document("parent").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
               Map map = documentSnapshot.getData();
                Iterator iterator = map.entrySet().iterator();

                while (iterator.hasNext()) {
                    Map.Entry pair = (Map.Entry)iterator.next();
                    subs.add(pair.getKey());
                    iterator.remove(); // avoids a ConcurrentModificationException
                }
                alertDialogue();
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
                Toast.makeText(getContext(), "TRY AGAIN", Toast.LENGTH_LONG).show();
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
        final StorageReference ref = storageRef.child("PROFILE_PICTURES/" + Faculty.faculty.getEmail());
        UploadTask uploadTask = ref.putBytes(getCompressedImageData());

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    Log.i("msg", "FACULTY PFP UPLoad");

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
                    Faculty.faculty.setDownloadURL(downloadUrl.toString());
                    Log.i("msg", "FACULTY PFP UPLoad");

                    progressDialog.hide();

                } else {
                    // Handle failures
                    // ...
                    Log.i("msg", "FACULTY PFP UPLoad failure");

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
                                    .setDisplayName(Faculty.faculty.getName())
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

        Faculty.faculty.setUserType("FACULTY");
        Faculty.faculty.setBuffer("2.0");

        db.collection("USER").document(UID)
                .set(Faculty.faculty)
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
                        if (task.isSuccessful()) {
                            Log.i("msg", "UPLOADED THE FACULTY.");
                            startActivity(new Intent(getContext(), MainActivity.class));
                            progressDialog.hide();
                            getActivity().finish();
                        }else
                            progressDialog.hide();

                    }
                });
    }
    void alertDialogue()
    {
        String[] subjects = new String[subs.size()];
        subjects = (String[]) subs.toArray(subjects);

        boolean[] arr = new boolean[subs.size()];
        for(int i = 0;i < subs.size(); i ++){
            arr[i] = false;
        }

        selectedItemList = new ArrayList();


        builder.setMultiChoiceItems(subjects, null, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if(!firstClicked)
                        selectedItemList.clear();
                    if(isChecked)
                    {
                        selectedItemList.add(subs.get(which));
                    }
                    else if (selectedItemList.contains(subs.get(which)))
                    {
                        selectedItemList.remove(subs.get(which));
                    }
                    }
                });


                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Select_sub.setText("Subjects selected. Click again to change.");
                        Log.i("SUB",selectedItemList.toString());
                        firstClicked = false;
                    }
                });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedItemList.clear();
                        firstClicked = false;
                    }
                });

                Select_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
    }

}
