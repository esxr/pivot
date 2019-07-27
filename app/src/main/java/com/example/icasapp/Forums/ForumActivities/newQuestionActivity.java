package com.example.icasapp.Forums.ForumActivities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static com.example.icasapp.Forums.ForumFragment.collectionReference;

public class newQuestionActivity extends AppCompatActivity {
    private Button addQuestion;
    private EditText Content;
    private EditText Title;
    private FirebaseAuth firebaseAuth;
    private String current_user_id;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_question);
        final String docId = getIntent().getStringExtra("post_id");
        addQuestion=findViewById(R.id.Add);
        Content=findViewById(R.id.Content);
        Title=findViewById(R.id.Title);

        firebaseAuth = FirebaseAuth.getInstance();
        current_user_id = firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore=firebaseFirestore.getInstance();


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

                    ForumFragment.setFirestoreReference(firebaseFirestore, ForumFragment.i_d,"c");
                    collectionReference.document(docId).collection("Questions").add(postMap)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
                                    collectionReference.document(docId).update("questions", FieldValue.increment(1));
                                    Toast.makeText(newQuestionActivity.this,"Success",Toast.LENGTH_LONG).show();
                                }
                            });
                }
            }
        });
    }
}
