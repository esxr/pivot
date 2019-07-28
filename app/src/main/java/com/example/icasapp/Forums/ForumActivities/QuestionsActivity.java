package com.example.icasapp.Forums.ForumActivities;

import android.content.Intent;

import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.icasapp.Forums.ForumAdapters.QuestionRecyclerAdapter;
import com.example.icasapp.Forums.OnBottomReachedListener;
import com.example.icasapp.MainActivity;
import com.example.icasapp.ObjectClasses.Questions;
import com.example.icasapp.R;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import static android.util.Log.i;
import static com.example.icasapp.Forums.ForumFragment.Category;
import static com.example.icasapp.Forums.ForumFragment.i_d;

public class QuestionsActivity extends AppCompatActivity {
    private FloatingActionButton addQuestion;
    private FirebaseFirestore firebaseFirestore;
    private Boolean isFirstPageFirstLoad=false;
    private RecyclerView question_list_view;
    private QuestionRecyclerAdapter questionRecyclerAdapter;
    public static String docId;
    public static String id;
    private CollectionReference collectionReference;
    private TextView parentTopic;
    private String topic;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Intent intent=getIntent();
        docId=intent.getStringExtra("post_id");
        Category= intent.getStringExtra("Category");
        i_d=intent.getStringExtra("ID");
        topic=intent.getStringExtra("Topic");

        addQuestion=findViewById(R.id.addQuestion);

        //Text that displays the topic you are in
        parentTopic = findViewById(R.id.parentTopic);
        parentTopic.setText(topic);

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

        //selecting the appropriate Category
        if (Category.equals("General") || Category.equals("Alumni")) {

            collectionReference = firebaseFirestore.collection("General").document(Category).collection("Posts");
        } else {
            Log.i("LOL","SUC");
            collectionReference = firebaseFirestore.collection("Specific").document(i_d).collection("Subjects").document(Category).collection("Posts");
        }

        collectionReference.document(docId).collection("Questions").orderBy("timestamp").addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    if(doc.getType() == DocumentChange.Type.REMOVED)
                    {
                        questionRecyclerAdapter.notifyDataSetChanged();
                    }
                }
                isFirstPageFirstLoad=false;
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                questionRecyclerAdapter.lol();
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(QuestionsActivity.this, MainActivity.class));
        finish();

    }



    }