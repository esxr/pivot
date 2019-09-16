package com.example.icasapp.Forums.ForumActivities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.ObjectClasses.Answers;
import com.example.icasapp.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
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

import static com.example.icasapp.Forums.ForumActivities.AnswersActivity.ans_id;
import static com.example.icasapp.Forums.ForumFragment.Category;
import static com.example.icasapp.Forums.ForumFragment.collectionReference;
import static com.example.icasapp.Forums.ForumFragment.i_d;

public class newAnswerActivity extends AppCompatActivity {
    private Button addQuestion;
    private EditText Content;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private String name;
    private FirebaseFirestore firebaseFirestore;
    private CheckBox checkBox;
    private ImageView answerImage;
    private Uri postImageUri = null;
    private String option;
    private Bitmap compressedImageFile;
    private StorageReference storageReference;

    private ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_answer);
        addQuestion = findViewById(R.id.Add);
        Content     = findViewById(R.id.Content);
        checkBox     = findViewById(R.id.anonymous);
        answerImage = findViewById(R.id.answerImage);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();


        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("File UPLOADING ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMax(100);//sets the maximum value 100



        current_user_id = firebaseAuth.getCurrentUser().getUid();
        //getting name of the user
        firebaseFirestore.collection("USER").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot doc=task.getResult();
                name = doc.get("name").toString();
            }
        });
        option = "";
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CompoundButton) v).isChecked())
                {
                    option = "anonymous";

                }
                else
                {
                    option = "";
                }
            }
        });


        Intent intent = getIntent();
        final String docId = intent.getStringExtra("post_id");
        Category = intent.getStringExtra("Category");
        i_d = intent.getStringExtra("ID");

        answerImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                    if (ContextCompat.checkSelfPermission(newAnswerActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //if permission not there permission is asked
                        Toast.makeText(newAnswerActivity.this, "Permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(newAnswerActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                    } else {

                        BringImagePicker();

                    }

                } else {

                    BringImagePicker();

                }

            }

        });

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String content=Content.getText().toString();
                if ((content.trim().length() > 0) && (content.length() < 200)) {

                    progressBar.setProgress(0);
                    progressBar.show();

                    if (postImageUri == null) {

                        Map<String, Object> postMap = new HashMap<>();
                        // postMap.put("image_thumb",ls);
                        postMap.put("answer", content);
                        postMap.put("timestamp", FieldValue.serverTimestamp());
                        postMap.put("user_id", current_user_id);
                        postMap.put("image_url","");
                        postMap.put("upvotes",0);
                        postMap.put("dirtybit",0);
                        if (!option.equals("anonymous")) {
                            postMap.put("name", name);
                        } else {
                            postMap.put("name", "empty");
                        }
                        ForumFragment.setFirestoreReference(firebaseFirestore, ForumFragment.i_d, "c");
                        collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").add(postMap)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        collectionReference.document(docId).collection("Questions").document(ans_id).update("answers", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                progressBar.setProgress(100);
                                                progressBar.hide();
                                                Intent intent = new Intent(getApplicationContext(), AnswersActivity.class);
                                                intent.putExtra("id",ans_id);
                                                intent.putExtra("Category", Category);
                                                intent.putExtra("ID", i_d);
                                                intent.putExtra("post_id", docId);
                                                startActivity(intent);
                                                Toast.makeText(newAnswerActivity.this, "Success", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "SOMETHING WENT WRONG.", Toast.LENGTH_LONG).show();
                                        progressBar.hide();
                                    }
                                });
                    } else {

                        final String randomName = UUID.randomUUID().toString();
                        // PHOTO UPLOAD
                        File newImageFile = new File(postImageUri.getPath());
                        try {
                            compressedImageFile = new Compressor(newAnswerActivity.this)
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
                            public Task<Uri> then(@io.reactivex.annotations.NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(newAnswerActivity.this, "Cant Upload", Toast.LENGTH_SHORT).show();
                                }

                                return FilePath.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                String downloadUrl = task.getResult().toString();
                                Map<String, Object> postMap = new HashMap<>();
                                // postMap.put("image_thumb",ls);
                                postMap.put("answer", content);
                                postMap.put("timestamp", FieldValue.serverTimestamp());
                                postMap.put("user_id", current_user_id);
                                postMap.put("upvotes",0);
                                postMap.put("dirtybit",0);
                                postMap.put("image_url", downloadUrl);
                                if (!option.equals("anonymous")) {
                                    postMap.put("name", name);
                                } else {
                                    postMap.put("name", "empty");
                                }
                                ForumFragment.setFirestoreReference(firebaseFirestore, ForumFragment.i_d, "c");
                                collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").add(postMap)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                collectionReference.document(docId).collection("Questions").document(ans_id).update("answers", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressBar.setProgress(100);
                                                        progressBar.hide();
                                                        Intent intent = new Intent(getApplicationContext(), AnswersActivity.class);
                                                        intent.putExtra("Category", Category);
                                                        intent.putExtra("id",ans_id);
                                                        intent.putExtra("ID", i_d);
                                                        intent.putExtra("post_id", docId);
                                                        startActivity(intent);
                                                        Toast.makeText(newAnswerActivity.this, "Success", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "SOMETHING WENT WRONG.", Toast.LENGTH_LONG).show();
                                                progressBar.hide();
                                            }
                                        });

                            }
                        });

                    }
                }
                if( (content.trim().length() == 0)){
                    Toast.makeText(getApplicationContext(), "Please fill all the fields.", Toast.LENGTH_LONG).show();
                }
                if((content.length() > 200))
                {
                    Toast.makeText(getApplicationContext(), "No field can be more than 200 characters.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void BringImagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMinCropResultSize(512, 512)
                .setAspectRatio(5, 3)
                .start(newAnswerActivity.this);
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
                answerImage.setImageURI(postImageUri);
                //setting the uri to our image
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            }
        }
    }
    }
