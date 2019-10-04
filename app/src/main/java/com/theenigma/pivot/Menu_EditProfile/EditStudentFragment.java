package com.theenigma.pivot.Menu_EditProfile;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.theenigma.pivot.MainActivity;
import com.theenigma.pivot.R;
import com.theenigma.pivot.Student;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import io.reactivex.annotations.NonNull;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditStudentFragment extends Fragment {

    private static final int SELECT_PICTURE = 100;


    TextView profileName;
    CircleImageView circleImageView;
    Spinner streamSpinner;
    String email;
    AppCompatButton resetPasswordButton, updateButton;
    FirebaseAuth auth;
    ImageView editPencil;
    Uri localImageUri, downloadUrl;
    Bitmap compressedImageFile;
    StorageReference storageRef;
    byte[] compressedImageData;
    FirebaseFirestore db;
    private View view;
    private Context mContext;


    public EditStudentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_edit_student, container, false);
        mContext = getActivity();


        profileName = view.findViewById(R.id.profileName);
        profileName.setText(Student.student.getName());

        circleImageView = view.findViewById(R.id.profilePhoto);
        streamSpinner = view.findViewById(R.id.streamSpinner);

        editPencil = view.findViewById(R.id.editPencil);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        email = auth.getCurrentUser().getEmail();

        resetPasswordButton = view.findViewById(R.id.resetPasswordButton);
        updateButton = view.findViewById(R.id.updateButton);

        storageRef = FirebaseStorage.getInstance().getReference();


        resetPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new AlertDialog.Builder(getContext())
                        .setTitle("A password reset form will been sent to your mail.")
                        .setCancelable(false)
                        .setPositiveButton("SEND RESET MAIL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                auth.sendPasswordResetEmail(email)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("msg", "Email sent.");
                                                }
                                            }
                                        });
                            }
                        })
                        .setNegativeButton("CANCEL RESET", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();


            }
        });

        Glide.with(getContext()).load(Student.student.getDownloadURL()).into(circleImageView);

        String[] streams = new String[]{
                "Select an item...",
                "CSE",
                "Mechanical",
                "Chem",
                "Mechatronics",
                "Electrical",
                "Aero",
                "Civil",
                "None"
        };

        final List<String> streamList = new ArrayList<>(Arrays.asList(streams));

        // Initializing an ArrayAdapter
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getContext(), R.layout.support_simple_spinner_dropdown_item, streamList) {
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
        streamSpinner.setAdapter(spinnerArrayAdapter);

        streamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItemText = (String) parent.getItemAtPosition(position);
                // If user change the default selection
                // First item is disable and it is used for hint
                if (position > 0) {
                    // Notify the selected item text
                    Toast.makeText
                            (getContext(), "Selected : " + selectedItemText, Toast.LENGTH_LONG)
                            .show();
                    Student.student.setStream(selectedItemText);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        editPencil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageChooser();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                compressImage();
                upload();
                updateFirestore();

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
            if (result != null) {
                Uri selectedImage = result.getUri();

                Glide
                        .with(getContext())
                        .load(new File(selectedImage.getPath())) // Uri of the picture
                        .into(circleImageView);

                setLocalImageUri(selectedImage);

            } else
                Toast.makeText(getContext(), "TRY AGAIN!", Toast.LENGTH_LONG).show();
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

        if (getLocalImageUri() != null) {

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
                    .into(circleImageView);
        }
    }


    private void upload() {
        if (getCompressedImageData() != null) {
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
                        setDownloadUrl(downloadUri);
                        Student.student.setDownloadURL(downloadUrl.toString());
                        updateFirestore();
                    } else {
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }

    public void updateFirestore() {

        db
                .collection("USER")
                .document(auth.getCurrentUser().getUid())
                .set(Student.student)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            if (EditStudentFragment.this.isVisible() & mContext != null)
                                Toast.makeText(mContext, "UPDATED", Toast.LENGTH_LONG).show();
                       //     Toast.makeText(getContext(), "UPDATED.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(mContext, MainActivity.class));
                        } else {
                            Toast.makeText(getActivity(), "FAILED. RETRY.", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getContext(), MainActivity.class));
                            getActivity().finish();
                        }
                    }
                });
    }


}
