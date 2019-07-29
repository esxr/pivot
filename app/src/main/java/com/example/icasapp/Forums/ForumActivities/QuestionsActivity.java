package com.example.icasapp.Forums.ForumActivities;

import android.content.Intent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.icasapp.Forums.ForumAdapters.QuestionRecyclerAdapter;
import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.Forums.OnBottomReachedListener;
import com.example.icasapp.ObjectClasses.Questions;
import com.example.icasapp.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static com.example.icasapp.Forums.ForumFragment.collectionReference;

public class QuestionsActivity extends AppCompatActivity {
    private FloatingActionButton addQuestion;
    private FirebaseFirestore firebaseFirestore;
    private Boolean isFirstPageFirstLoad=false;
    private Boolean FirstLoad=true;
    private RecyclerView question_list_view;
    private QuestionRecyclerAdapter questionRecyclerAdapter;
    public static String docId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        addQuestion=findViewById(R.id.addQuestion);

        firebaseFirestore = FirebaseFirestore.getInstance();

       question_list_view = findViewById(R.id.question_list_view);

        final ArrayList question_list= new ArrayList<Questions>();
        //initialising the adapter
        questionRecyclerAdapter = new QuestionRecyclerAdapter(question_list);

        question_list_view.setLayoutManager(new LinearLayoutManager(QuestionsActivity.this));
        question_list_view.setAdapter(questionRecyclerAdapter);

        questionRecyclerAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void OnBottomReached(int position) {

            }
        });


        docId = getIntent().getStringExtra("post_id");

        Toast.makeText(QuestionsActivity.this,docId,Toast.LENGTH_LONG).show();

        addQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(QuestionsActivity.this, newQuestionActivity.class);
                intent.putExtra("post_id", docId);
                startActivity(intent);
            }
        });

        ForumFragment.setFirestoreReference(firebaseFirestore, ForumFragment.i_d,"c");
        collectionReference.document(docId).collection("Questions").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                //check for changes in document if data is added

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        //this is the part where every item in the firebase document gets stored in DiscussionTopic list
                        String questionId=doc.getDocument().getId();
                        Questions questions = doc.getDocument().toObject(Questions.class).withId(questionId);
                        if (isFirstPageFirstLoad) {

                            question_list.add(questions);


                        } else {

                            question_list.add(0, questions);


                        }

                       questionRecyclerAdapter.notifyDataSetChanged();
                    }
                }
                isFirstPageFirstLoad=false;
            }
        });
    }

    }