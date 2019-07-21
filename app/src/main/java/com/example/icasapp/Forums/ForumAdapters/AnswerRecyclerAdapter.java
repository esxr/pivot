package com.example.icasapp.Forums.ForumAdapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.ObjectClasses.Answers;
import com.example.icasapp.R;
import com.google.android.gms.common.server.converter.StringToIntConverter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import static com.example.icasapp.Forums.ForumActivities.AnswersActivity.ans_id;
import static com.example.icasapp.Forums.ForumActivities.QuestionsActivity.docId;
import static com.example.icasapp.Forums.ForumFragment.collectionReference;

public class AnswerRecyclerAdapter extends RecyclerView.Adapter<AnswerRecyclerAdapter.ViewHolder>{

    private List<Answers> answersList=new ArrayList<>();
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private boolean FirstTime=true;
    private FirebaseAuth firebaseAuth;

    public AnswerRecyclerAdapter(List<Answers> answerList){
        this.answersList=answerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflating the view holder with xml objects
        context=viewGroup.getContext();
        View view=LayoutInflater.from(context).inflate(R.layout.answer_list_item,viewGroup,false);
        firebaseAuth=firebaseAuth.getInstance();
        return new AnswerRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {


        final Answers answers = answersList.get(i);
        firebaseFirestore = firebaseFirestore.getInstance();
        final String currentUser = firebaseAuth.getCurrentUser().getUid();

        final String id = answersList.get(i).AnswerPostId;
        viewHolder.setupvote(Integer.toString(answersList.get(i).getUpvotes()));

        String answer= answers.getAnswer();
        viewHolder.setAnswer(answer);

        viewHolder.upvotes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).collection("Upvotes")
                                        .document(currentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if (documentSnapshot.exists()) {
                                            collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).collection("Upvotes")
                                                    .document(currentUser).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Toast.makeText(context, "Down-voted", Toast.LENGTH_LONG).show();
                                                    collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).update("upvotes",FieldValue.increment(-1));
                                                }
                                            });
                                        } else {
                                            HashMap<String, Object> postMap = new HashMap<>();
                                            postMap.put("timestamp", FieldValue.serverTimestamp());
                                            collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).collection("Upvotes")
                                                    .document(currentUser).set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).update("upvotes",FieldValue.increment(1));
                                                    Toast.makeText(context, "Upvoted", Toast.LENGTH_LONG).show();
                                                }
                                            });
                                        }
                                    }
                                });

                            }
                        });

        collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).collection("Upvotes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (snapshots.isEmpty()) {
                            viewHolder.setupvote("0");
                            answersList.get(i).setUpvotes(0);
                            return;
                        } else {
                            String count = Integer.toString(snapshots.size());
                            int c= Integer.parseInt(count);
                            answersList.get(i).setUpvotes(c);
                            viewHolder.setupvote(count);

                        }
                    }
                });

        if(currentUser.equals(answersList.get(i).getUser_id()))
        {
            viewHolder.delete.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.delete.setVisibility(View.GONE);
        }

        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        answersList.remove(i);
                        notifyDataSetChanged();
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return answersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View mView;
        Button upvotes;
        TextView answers;
        TextView upVotes;
        ImageView delete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            upvotes=mView.findViewById(R.id.upvote_btn);
            upVotes=mView.findViewById(R.id.upvote_text);
            answers=mView.findViewById(R.id.Answer);
            delete=mView.findViewById(R.id.delete);
        }

        public void setAnswer(String answer){
             answers.setText(answer);
        }

        public String getupvote(){
            return  upVotes.getText().toString();
        }

        public void setupvote(String upvote){
            upVotes.setText(upvote);
        }

    }

}