package com.example.icasapp.Forums.ForumActivities;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);

        addQuestion=findViewById(R.id.Add);
        Content=findViewById(R.id.Content);
        Title=findViewById(R.id.Title);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore=firebaseFirestore.getInstance();

        current_user_id = firebaseAuth.getCurrentUser().getUid();
        //getting name of the user
        firebaseFirestore.collection("USER").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                       @Override
                                                                                                       public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                                           DocumentSnapshot doc=task.getResult();
                                                                                                           name= doc.get("name").toString();
                                                                                                       }
                                                                                                   });


        Intent intent=getIntent();
        final String docId = intent.getStringExtra("post_id");
        Category=intent.getStringExtra("Category");
        i_d=intent.getStringExtra("ID");
        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title=Title.getText().toString();
                String content=Content.getText().toString();
                if (!TextUtils.isEmpty(content) &&!TextUtils.isEmpty(title)) {

                    Map<String, Object> postMap = new HashMap<>();
                    postMap.put("topic", title);
                    // postMap.put("image_thumb",ls);
                    postMap.put("content", content);
                    postMap.put("user_id", current_user_id);
                    postMap.put("timestamp", FieldValue.serverTimestamp());
                    postMap.put("name",name);
                    postMap.put("best_answer","");

                    ForumFragment.setFirestoreReference(firebaseFirestore, ForumFragment.i_d, "c");
                    collectionReference.document(docId).collection("Questions").add(postMap)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    collectionReference.document(docId).update("question", FieldValue.increment(1)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                                                                              @Override
                                                                                                                                              public void onComplete(@NonNull Task<Void> task) {
                                                                                                                                                  Intent intent = new Intent(getApplicationContext(), QuestionsActivity.class);
                                                                                                                                                  intent.putExtra("Category",Category);
                                                                                                                                                  intent.putExtra("ID",i_d);
                                                                                                                                                  intent.putExtra("post_id",docId);
                                                                                                                                                  startActivity(intent);
                                                                                                                                                  Toast.makeText(newQuestionActivity.this,"Success",Toast.LENGTH_LONG).show();
                                                                                                                                              }
                                                                                                                                          });
                                }
                            });
                }
            }
        });
    }
}
