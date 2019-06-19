package com.example.icasapp.Activities;

import android.content.DialogInterface;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.example.icasapp.Adapters.AnswerRecyclerAdapter;
import com.example.icasapp.Adapters.QuestionRecyclerAdapter;
import com.example.icasapp.ObjectClasses.Answers;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static com.example.icasapp.Activities.QuestionsActivity.docId;


public class AnswersActivity extends AppCompatActivity {

    private FloatingActionButton addAnswer;
    private FloatingActionButton refresh;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    private String answer;
    private String currentUserId;
    public static String ans_id;

    boolean isFirstPageLoad=false;

    private List<Answers> answersList;
    private AnswerRecyclerAdapter answerRecyclerAdapter;

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
        refresh=findViewById(R.id.refresh);

        firebaseAuth=FirebaseAuth.getInstance();
        currentUserId=firebaseAuth.getCurrentUser().getUid();

        firebaseFirestore=FirebaseFirestore.getInstance();

        addAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog();
            }
    });

        ans_id = getIntent().getStringExtra("id");

        firebaseFirestore.collection("Posts").document(docId).collection("Questions").document(ans_id).collection("Answers").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    switch (doc.getType()){
                        case ADDED:

                            String post_id=doc.getDocument().getId();
                            Answers answers = doc.getDocument().toObject(Answers.class).withId(post_id);
                            if(isFirstPageLoad==true){
                                answersList.add(answers);
                                answerRecyclerAdapter.notifyDataSetChanged();
                            }
                            else{
                                answersList.add(0,answers);
                                shuffle();
                                answerRecyclerAdapter.notifyDataSetChanged();
                            }
                            break;
                        case MODIFIED:
                            shuffle();
                            Toast.makeText(AnswersActivity.this, "Success", Toast.LENGTH_LONG).show();
                            answerRecyclerAdapter.notifyDataSetChanged();
                            break;
                        case REMOVED:
                            break;

                    }
                    if(isFirstPageLoad){

                    }
                    isFirstPageLoad=false;
                }

            }
        });
    refresh.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            shuffle();
            answerRecyclerAdapter.notifyDataSetChanged();

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
                postMap.put("dirty bit","0");

                firebaseFirestore.collection("Posts").document(docId).collection("Questions").document(ans_id).collection("Answers").add(postMap)
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
    public void shuffle()
    {
try {
    Collections.sort(answersList, new Comparator<Answers>() {
        @Override
        public int compare(Answers u1, Answers u2) {
            return u2.upvotes.compareTo(u1.upvotes); // Ascending
        }

    });
}
catch(Exception e){

}
    }





}
