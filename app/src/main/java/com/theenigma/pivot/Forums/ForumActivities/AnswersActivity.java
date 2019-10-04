package com.theenigma.pivot.Forums.ForumActivities;

import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.theenigma.pivot.Forums.ForumAdapters.FirebaseAnswerAdapter;
import com.theenigma.pivot.ObjectClasses.Answers;
import com.theenigma.pivot.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import io.reactivex.annotations.NonNull;

import static com.theenigma.pivot.Forums.ForumActivities.QuestionsActivity.docId;
import static com.theenigma.pivot.Forums.ForumFragment.Category;
import static com.theenigma.pivot.Forums.ForumFragment.collectionReference;
import static com.theenigma.pivot.Forums.ForumFragment.i_d;


public class AnswersActivity extends AppCompatActivity {

    private FloatingActionButton addAnswer;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String answer;
    private String currentUserId;
    public static String ans_id;
    private String sort;

    boolean isFirstPageLoad=false;

    private List<Answers> answersList;
    public FirestoreRecyclerAdapter adapter;

    private Button recent;
    private Button votes;
    private String Topic;
    private String Content;

    private TextView topic;
    private TextView content;

    String buffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        addAnswer=findViewById(R.id.add);
        recent=findViewById(R.id.recent);
        votes=findViewById(R.id.topVoted);

        topic=findViewById(R.id.title);
        content=findViewById(R.id.question);

        firebaseAuth=FirebaseAuth.getInstance();
        currentUserId=firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore=FirebaseFirestore.getInstance();


        Intent intent=getIntent();
        ans_id = intent.getStringExtra("id"); //question document id
       // Topic = intent.getStringExtra("topic");
       // Content=intent.getStringExtra("content");
        Category=intent.getStringExtra("Category");
        i_d=intent.getStringExtra("ID"); //specific category id
        docId=intent.getStringExtra("post_id"); //topic id

        //in the starting sort is according to timestamp
        sort = "timestamp";

        addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //values sent so that they are preserved
                Intent intent=new Intent(AnswersActivity.this, newAnswerActivity.class);
                intent.putExtra("post_id", docId);
                intent.putExtra("Category",Category);
                intent.putExtra("ID",i_d);
                intent.putExtra("id",ans_id);
                startActivity(intent);
                //  Dialog();
            }
        });


        //selecting the appropriate Category
        if (Category.equals("General") || Category.equals("Alumni")) {
            collectionReference = firebaseFirestore.collection("General").document(Category).collection("Posts");
        } else {
            collectionReference = firebaseFirestore.collection("Specific").document(i_d).collection("Subjects").document(Category).collection("Posts");
        }


        collectionReference.document(docId).collection("Questions").document(ans_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {

                DocumentSnapshot documentSnapshot = task.getResult();
                Topic = documentSnapshot.get("topic").toString();
                Content = documentSnapshot.get("content").toString();
                topic.setText("Question:" + Topic);
                content.setText("Description:" + Content );

            }
        });

         firebaseFirestore.collection("USER").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                buffer = (String) task.getResult().get("buffer");
            }
        });

        //menu items to select what to sort and how to sort
        recent.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          isFirstPageLoad=false;
                                          sort="timestamp";
                                          setQuery(sort);
                                          recent.setBackgroundColor(getResources().getColor(android.R.color.black));
                                          recent.setTextColor(getResources().getColor(android.R.color.white));
                                          votes.setBackgroundColor(getResources().getColor(android.R.color.white));
                                          votes.setTextColor(getResources().getColor(android.R.color.black));
                                      }
                                  });

        votes.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         //when another menu is selected the documents are loaded again
                                        sortUpvotes();
                                         votes.setBackgroundColor(getResources().getColor(android.R.color.black));
                                         votes.setTextColor(getResources().getColor(android.R.color.white));
                                         recent.setBackgroundColor(getResources().getColor(android.R.color.white));
                                         recent.setTextColor(getResources().getColor(android.R.color.black));
                                     }
                                 });



    }


    public void setQuery(final String sort)
    {

      if(!isFirstPageLoad) {
          adapter.stopListening();
      }

      Query query = collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").orderBy(sort, Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Answers> options = new FirestoreRecyclerOptions.Builder<Answers>()
                .setQuery(query,Answers.class)
                .build();

        adapter = new FirebaseAnswerAdapter(options);



        RecyclerView recyclerView = findViewById(R.id.uniView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);
        adapter.startListening();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                        ((FirebaseAnswerAdapter) adapter).deleteItem(viewHolder.getAdapterPosition());
            }
        });


        if(!isFirstPageLoad ){
        adapter.startListening();
    }
    }


    //When pressing back goes to Question activity
    @Override
    public void onBackPressed()
    {
        //to preserve the values
        super.onBackPressed();
        Intent intent=new Intent(AnswersActivity.this, QuestionsActivity.class);
        intent.putExtra("Category",Category);
        intent.putExtra("ID",i_d);
        intent.putExtra("post_id",docId);
        startActivity(intent);

    }

    public void sortUpvotes()
    {
        isFirstPageLoad=false;
        sort="upvotes";
        setQuery(sort);
    }

    @Override
    protected void onStart() {
        super.onStart();
        isFirstPageLoad=true;
        setQuery(sort);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
