package com.example.icasapp.Forums.ForumActivities;

import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icasapp.Forums.ForumAdapters.AnswerRecyclerAdapter;
import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.ObjectClasses.Answers;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static com.example.icasapp.Forums.ForumActivities.QuestionsActivity.docId;
import static com.example.icasapp.Forums.ForumFragment.collectionReference;


public class AnswersActivity extends AppCompatActivity {

    private FloatingActionButton addAnswer;
    private FloatingActionButton refresh;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String answer;
    private String currentUserId;
    public static String ans_id;
    private String sort;

    boolean isFirstPageLoad=false;

    private List<Answers> answersList;
    private AnswerRecyclerAdapter answerRecyclerAdapter;

    private Button recent;
    private Button votes;

    private TextView topic;
    private TextView content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        answersList=new ArrayList<>();
        answerRecyclerAdapter=new AnswerRecyclerAdapter(answersList);

        RecyclerView answerView=findViewById(R.id.answersView);
        answerView.setLayoutManager(new LinearLayoutManager(AnswersActivity.this));
        answerView.setAdapter(answerRecyclerAdapter);

        addAnswer=findViewById(R.id.add);
        recent=findViewById(R.id.recent);
        votes=findViewById(R.id.topVoted);

        topic=findViewById(R.id.title);
        content=findViewById(R.id.question);

        firebaseAuth=FirebaseAuth.getInstance();
        currentUserId=firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore=FirebaseFirestore.getInstance();

        ForumFragment.setFirestoreReference(firebaseFirestore, ForumFragment.i_d,"c");
        addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
    });


        ans_id = getIntent().getStringExtra("id");
        String Topic=getIntent().getStringExtra("topic");
        String Content=getIntent().getStringExtra("content");

        topic.setText(Topic);
        content.setText(Content);

       try {
           Log.i("ASDASD", ans_id);
           Log.i("ASDADSASD", docId);
       }
       catch (Exception e)
       {

       }
        collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").orderBy(sort, Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (doc.getType()) {
                        case ADDED:
                            String post_id = doc.getDocument().getId();
                            Answers answers = doc.getDocument().toObject(Answers.class).withId(post_id);
                            if(isFirstPageLoad) {
                                answersList.add(answers);

                            }
                            //if the page is not loaded for the first time. That means a new post or mode has been changed has been added then these conditions are invoked
                            else {
                                    answersList.add(0, answers);
                                    shuffle();
                                    //   bestAnswerSelector();
                            }
                            answerRecyclerAdapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            //is upvotes are put the document will be modified hence the arraylist will again be shifted

                            shuffle();

                            //whenever modified best answer is selected in answer activity
                            //  bestAnswerSelector();
                            Toast.makeText(AnswersActivity.this, "Success", Toast.LENGTH_LONG).show();
                            answerRecyclerAdapter.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            break;

                    }

                }
                isFirstPageLoad = false;
            }
        });

        //menu items to select what to sort and how to sort
     /*   recent.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          isFirstPageLoad=true;
                                          sort="timestamp";
                                          setQuery(sort, answersList, answerRecyclerAdapter);
                                      }
                                  });

        votes.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         //when another menu is selected the documents are loaded again
                                         isFirstPageLoad=true;
                                         sort="upvotes";
                                         setQuery(sort,answersList, answerRecyclerAdapter);
                                     }
                                 });*/



    }

    public void Dialog() {
        AlertDialog.Builder alert = new AlertDialog.Builder(AnswersActivity.this);

        alert.setTitle("Add your Answer");
        //  alert.setMessage("Message");

        // Set an EditText view to get user input
        final EditText input = new EditText(AnswersActivity.this);
        alert.setView(input);


        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                answer = input.getText().toString();

                Map<String, Object> postMap = new HashMap<>();
                postMap.put("answer", answer);
                postMap.put("user_id", currentUserId);
                postMap.put("timestamp", FieldValue.serverTimestamp());
                postMap.put("upvotes",0);

                collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").add(postMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(AnswersActivity.this, "Success", Toast.LENGTH_LONG).show();

                            }
                        });

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    public void setQuery(final String sort, final List<Answers> answersList, final AnswerRecyclerAdapter answerRecyclerAdapter)
    {

    answersList.clear();

        collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").orderBy(sort, Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    switch (doc.getType()) {
                        case ADDED:
                            String post_id = doc.getDocument().getId();
                            Answers answers = doc.getDocument().toObject(Answers.class).withId(post_id);
                            if(isFirstPageLoad) {
                                answersList.add(answers);

                            }
                            //if the page is not loaded for the first time. That means a new post or mode has been changed has been added then these conditions are invoked
                            else {
                                if (sort == "timestamp") {
                                    //if sorting was of timestamp. then when a new post is added the post is put up.
                                    answersList.add(0, answers);
                                }
                                if (sort == "upvotes") {
                                    answersList.add(0, answers);
                                    shuffle();
                                   //   bestAnswerSelector();
                                }
                            }
                            answerRecyclerAdapter.notifyDataSetChanged();
                            break;
                        case MODIFIED:
                            //is upvotes are put the document will be modified hence the arraylist will again be shifted

                                shuffle();

                            //whenever modified best answer is selected in answer activity
                          //  bestAnswerSelector();
                            Toast.makeText(AnswersActivity.this, "Success", Toast.LENGTH_LONG).show();
                            answerRecyclerAdapter.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            break;

                    }

                }
                isFirstPageLoad = false;
            }
        });
    }

    public void shuffle()
    {
        try {
            Collections.sort(answersList, new Comparator<Answers>() {
                @Override
                public int compare(Answers u1, Answers u2) {
                    Log.i("ASDFG", Integer.toString(u2.upvotes-u1.upvotes));
                    return u2.upvotes-u1.upvotes; // Ascending
                }

            });
        }
        catch(Exception e){

            Log.d("Ex","ERROR");
        }
    }

    public void bestAnswerSelector()
    {

        String answer=answersList.get(0).getAnswer();
        collectionReference.document(docId).collection("Questions").document(ans_id).update("best_answer",answer);
    }

}
