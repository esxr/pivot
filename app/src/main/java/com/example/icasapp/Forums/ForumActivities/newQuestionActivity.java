package com.example.icasapp.Forums.ForumActivities;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.example.icasapp.Forums.ForumFragment.Category;
import static com.example.icasapp.Forums.ForumFragment.collectionReference;
import static com.example.icasapp.Forums.ForumFragment.i_d;

public class newQuestionActivity extends AppCompatActivity {
    private Button addQuestion;
    private EditText Content;
    private EditText Title;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private String name;
    private FirebaseFirestore firebaseFirestore;
    private CheckBox checkBox;
    String option;

    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        addQuestion = findViewById(R.id.Add);
        Content     = findViewById(R.id.Content);
        Title       = findViewById(R.id.Title);
        checkBox     = findViewById(R.id.anonymous);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();


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

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=Title.getText().toString();
                String content=Content.getText().toString();
                if ((title.trim().length() > 0) && (title.length() < 200) &&(content.trim().length() > 0) && (content.length() < 200)){

                    progressBar.setProgress(0);
                    progressBar.show();

                    Map<String, Object> postMap = new HashMap<>();
                    postMap.put("topic", title);
                    // postMap.put("image_thumb",ls);
                    postMap.put("content", content);
                    postMap.put("timestamp", FieldValue.serverTimestamp());
                    postMap.put("user_id", current_user_id);
                    postMap.put("answers","0");
                    if(!option.equals("anonymous")){
                        postMap.put("name",name);
                    }
                    else{
                        postMap.put("name","empty");
                    }
                    postMap.put("best_answer","");
                    ForumFragment.setFirestoreReference(firebaseFirestore, ForumFragment.i_d, "c");
                    collectionReference.document(docId).collection("Questions").add(postMap)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    collectionReference.document(docId).update("question", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                              @Override
                                                                                                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                  progressBar.setProgress(100);
                                                                                                                                                  progressBar.hide();
                                                                                                                                                  Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                                                                                                                                                  intent.putExtra("Category",Category);
                                                                                                                                                  intent.putExtra("ID",i_d);
                                                                                                                                                  intent.putExtra("post_id",docId);
                                                                                                                                                  startActivity(intent);
                                                                                                                                                  Toast.makeText(newQuestionActivity.this,"Success",Toast.LENGTH_LONG).show();
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
                if((title.trim().length() == 0) || (content.trim().length() == 0)){
                    Toast.makeText(getApplicationContext(), "Please fill all the fields.", Toast.LENGTH_LONG).show();
                }
                if((title.length() > 200) || (content.length() > 200))
                {
                    Toast.makeText(getApplicationContext(), "No field can be more than 200 characters.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
