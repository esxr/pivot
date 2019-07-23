package com.example.icasapp.Forums.ForumActivities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.MainActivity;
import com.example.icasapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import id.zelory.compressor.Compressor;

import static com.example.icasapp.Forums.ForumFragment.collectionReference;
import static com.example.icasapp.Forums.ForumFragment.i_d;
import static com.example.icasapp.Forums.ForumFragment.setFirestoreReference;


public class NewDiscussionActivity extends AppCompatActivity {
    private ImageView setUpImage;
    private Uri postImageUri = null;
    private Button newPostBtn;
    private EditText editText;
    private StorageReference storageReference;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private Bitmap compressedImageFile;
    private String downloadUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_discussion);

        setUpImage = findViewById(R.id.new_post_image);
        newPostBtn = findViewById(R.id.post_btn);
        editText = findViewById(R.id.topic);
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        firebaseAuth = FirebaseAuth.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();
        //if the build if it is greater than Marshmellow. If it is permission is asked from the user to access storage
        setUpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(NewDiscussionActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //if permission not there permission is asked
                        Toast.makeText(NewDiscussionActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(NewDiscussionActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }

            }

        });
        //data is stored in storage folder thread image
        // If the image is put successfully.
        // Details of the image. The upload uri, user uid, timestamp etc are stored in a collection thread in firestore
        //Compressed image is stored

        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String content = editText.getText().toString();

                if (!TextUtils.isEmpty(content) && postImageUri != null) {

                    final String randomName = UUID.randomUUID().toString();

                    // PHOTO UPLOAD
                    File newImageFile = new File(postImageUri.getPath());
                    try {

                        compressedImageFile = new Compressor(NewDiscussionActivity.this)
                                .setMaxHeight(720)
                                .setMaxWidth(720)
                                .setQuality(50)
                                .compressToBitmap(newImageFile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    compressedImageFile.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] imageData = baos.toByteArray();

                    // PHOTO UPLOAD
                    final StorageReference FilePath = storageReference.child("post_images").child(randomName + ".jpg");
                    FilePath.putBytes(imageData).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if(!task.isSuccessful())
                            {
                                Toast.makeText(NewDiscussionActivity.this,"Cant Upload",Toast.LENGTH_SHORT).show();
                            }
                            return FilePath.getDownloadUrl();
                        }
                    })
                            .addOnCompleteListener(new OnCompleteListener<Uri>() {
                                @Override
                                public void onComplete(@NonNull Task<Uri> task) {
                                    downloadUrl = task.getResult().toString();
                                    Map<String, Object> postMap = new HashMap<>();
                                    postMap.put("image_url", downloadUrl);
                                    postMap.put("content", content);
                                    postMap.put("user_id", current_user_id);
                                    postMap.put("timestamp", FieldValue.serverTimestamp());

                                    setFirestoreReference(firebaseFirestore,i_d,"c");

                                    collectionReference.add(postMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                        @Override

                                        public void onComplete(@NonNull Task<DocumentReference> task) {
                                            if (task.isSuccessful()) {

                                                Toast.makeText(NewDiscussionActivity.this, "Post was added", Toast.LENGTH_LONG).show();
                                                Intent mainIntent = new Intent(NewDiscussionActivity.this, ForumFragment.class);
                                                startActivity(mainIntent);
                                                finish();

                                            } else {
                                                Toast.makeText(NewDiscussionActivity.this, "Post was not added", Toast.LENGTH_LONG).show();

                                            }

                                        }
                                    });
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(NewDiscussionActivity.this,"Cant Uplaod",Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
    }



    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512, 512)
                .setAspectRatio(1, 1)
                .start(NewDiscussionActivity.this);
    }

    //used to get data from another app's activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
            //ones the image is returned the URI is stored in the memory.
                postImageUri = result.getUri();
                setUpImage.setImageURI(postImageUri);
               //setting the uri to our image
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }
    }
}
