package com.example.icasapp.Forums.ForumActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icasapp.Forums.ForumAdapters.AnswerRecyclerAdapter;
import com.example.icasapp.Forums.ForumAdapters.FirebaseAnswerAdapter;
import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.MainActivity;
import com.example.icasapp.ObjectClasses.Answers;
import com.example.icasapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
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
import static com.example.icasapp.Forums.ForumFragment.Category;
import static com.example.icasapp.Forums.ForumFragment.collectionReference;
import static com.example.icasapp.Forums.ForumFragment.i_d;


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
    public FirestoreRecyclerAdapter adapter;

    private Button recent;
    private Button votes;

    private TextView topic;
    private TextView content;

    private ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

      /*  answersList=new ArrayList<>();
        answerRecyclerAdapter=new AnswerRecyclerAdapter(answersList);
        RecyclerView answerView=findViewById(R.id.answersView);
        answerView.setLayoutManager(new LinearLayoutManager(AnswersActivity.this));
        answerView.setAdapter(answerRecyclerAdapter); */

        addAnswer=findViewById(R.id.add);
        recent=findViewById(R.id.recent);
        votes=findViewById(R.id.topVoted);

        topic=findViewById(R.id.title);
        content=findViewById(R.id.question);

        firebaseAuth=FirebaseAuth.getInstance();
        currentUserId=firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore=FirebaseFirestore.getInstance();

        addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
    });

        Intent intent=getIntent();
        ans_id = intent.getStringExtra("id");
        String Topic=intent.getStringExtra("topic");
        String Content=intent.getStringExtra("content");
        Category=intent.getStringExtra("Category");
        i_d=intent.getStringExtra("ID");

        //selecting the appropriate Category
        if (Category.equals("General") || Category.equals("Alumni")) {
            Log.i("LOL","SUCC");
            collectionReference = firebaseFirestore.collection("General").document(Category).collection("Posts");
        } else {
            Log.i("LOL","SUC");
            collectionReference = firebaseFirestore.collection("Specific").document(i_d).collection("Subjects").document(Category).collection("Posts");
        }

        topic.setText(Topic);
        content.setText(Content);

        isFirstPageLoad=true;
        sort="timestamp";
        setQuery(sort, answersList, answerRecyclerAdapter);
        //menu items to select what to sort and how to sort
        recent.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          isFirstPageLoad=false;
                                          sort="timestamp";
                                          setQuery(sort, answersList, answerRecyclerAdapter);
                                      }
                                  });

        votes.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View v) {
                                         //when another menu is selected the documents are loaded again
                                        sortUpvotes();
                                     }
                                 });



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
                postMap.put("dirtybit",0);

                collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").add(postMap)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Toast.makeText(AnswersActivity.this, "Uploaded", Toast.LENGTH_LONG).show();
                               // isFirstPageLoad=true;
                              //  setQuery(sort, answersList, answerRecyclerAdapter);
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
       // try {
       //     registration.remove();
       // }
       // catch(Exception e)
       // {

        //}
//    answersList.clear();
      //  answerRecyclerAdapter.notifyDataSetChanged();

    /*   registration =collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").orderBy(sort, Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                                Log.i("SORT",sort);
                                if (sort.equals("timestamp")) {
                                    //if sorting was of timestamp. then when a new post is added the post is put up.
                                    answersList.add(0, answers);
                                }
                                if (sort.equals("upvotes")) {
                                    answersList.add(0, answers);
                                    shuffle();
                                }
                            }
                            answerRecyclerAdapter.notifyItemInserted(0);
                            break;
                        case MODIFIED:
                            //is upvotes are put the document will be modified hence the arraylist will again be shifted

                            if(sort.equals( "upvotes")) {
                                Log.i("LOL","UPLOADING");
                                shuffle();
                            }
                            //whenever modified best answer is selected in answer activity
                            //in best answer selector shuffle is automatically called
                            bestAnswerSelector();
                            Toast.makeText(AnswersActivity.this, "Success", Toast.LENGTH_LONG).show();
                            answerRecyclerAdapter.notifyDataSetChanged();
                            break;
                        case REMOVED:

                         answerRecyclerAdapter.notifyDataSetChanged();
                            break;

                    }

                }
                isFirstPageLoad = false;
            }
        }); */
      if(!isFirstPageLoad) {
          adapter.stopListening();
      }

      Query query = collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").orderBy(sort, Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Answers> options = new FirestoreRecyclerOptions.Builder<Answers>()
                .setQuery(query,Answers.class)
                .build();

        adapter = new FirebaseAnswerAdapter(options);



        RecyclerView recyclerView = findViewById(R.id.answersView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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



    public void shuffle()
    {
        try {

            Collections.sort(answersList, new Comparator<Answers>() {
                @Override
                public int compare(Answers u1, Answers u2) {
                 //   return u2.upvotes-u1.upvotes; // Ascending
                    return u1.getUpvotes() > u2.getUpvotes() ? -1 : (u1.getUpvotes() < u2.getUpvotes()) ? 1 : 0;
                }

            });
            for(Answers a:answersList){
                   Log.i("LOL","After sorting"+ a.getAnswer());
                   Log.i("LOL","After sorting"+a.getUpvotes());
            }
        }
        catch(Exception e){

            Log.d("Error","Sorting Error");
        }

    }

    public void bestAnswerSelector()
    {
        int index = 9999;
        for(Answers answers: answersList)
        {
            int max=0;
            int i=0;
            if(answers.getUpvotes()>max)
            {
                max=answers.getUpvotes();
                index=i;
            }
            i++;
        }

        try {
            String answer = answersList.get(index).getAnswer();
            collectionReference.document(docId).collection("Questions").document(ans_id).update("best_answer", answer);

        }
        catch(Exception noBestAnswer)
        {

        }
    }

    //When pressing back goes to Question activity
    @Override
    public void onBackPressed()
    {
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
        setQuery(sort,answersList, answerRecyclerAdapter);
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
