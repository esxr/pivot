package com.example.icasapp.Forums.ForumActivities;

import android.content.Intent;

import com.example.icasapp.Forums.ForumAdapters.FirebaseQuestionRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.icasapp.MainActivity;
import com.example.icasapp.ObjectClasses.Questions;
import com.example.icasapp.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static android.util.Log.i;
import static com.example.icasapp.Forums.ForumFragment.Category;
import static com.example.icasapp.Forums.ForumFragment.i_d;

public class QuestionsActivity extends AppCompatActivity {
    private FloatingActionButton addQuestion;
    private FirebaseFirestore firebaseFirestore;
    public static String docId;
    public static String id;
    private CollectionReference collectionReference;
    private TextView parentTopic;
    private String topic;
    private FirebaseQuestionRecyclerAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        firebaseFirestore = FirebaseFirestore.getInstance();

        Intent intent=getIntent();
        docId=intent.getStringExtra("post_id");
        Category= intent.getStringExtra("Category");
        i_d=intent.getStringExtra("ID");

        addQuestion=findViewById(R.id.addNotes);

        //selecting the appropriate Category
        if (Category.equals("General") || Category.equals("Alumni")) {

            collectionReference = firebaseFirestore.collection("General").document(Category).collection("Posts");
        } else {
            Log.i("LOL","SUC");
            collectionReference = firebaseFirestore.collection("Specific").document(i_d).collection("Subjects").document(Category).collection("Posts");
        }

        //Text that displays the topic you are in
        parentTopic = findViewById(R.id.parentTopic);
        collectionReference.document(docId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                                                                DocumentSnapshot documentSnapshot = task.getResult();
                                                                                topic = documentSnapshot.get("content").toString();
                                                                                parentTopic.setText("TOPIC -" + topic);
                                                                            }
                                                                        });


        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuestionsActivity.this, newQuestionActivity.class);
                intent.putExtra("post_id", docId);
                intent.putExtra("Category",Category);
                intent.putExtra("ID",i_d);
                startActivity(intent);
            }
        });


        Query query = collectionReference.document(docId).collection("Questions").orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Questions> options = new FirestoreRecyclerOptions.Builder<Questions>()
                .setQuery(query,Questions.class)
                .build();

        adapter = new FirebaseQuestionRecyclerAdapter(options);

        RecyclerView recyclerView = findViewById(R.id.questionView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);

    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(QuestionsActivity.this, MainActivity.class));
        finish();

    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}