package com.example.icasapp.Adapters;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.icasapp.Activities.AnswersActivity;
import com.example.icasapp.ObjectClasses.Answers;
import com.example.icasapp.ObjectClasses.Questions;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.icasapp.Activities.AnswersActivity.ans_id;
import static com.example.icasapp.Activities.QuestionsActivity.docId;

public class AnswerRecyclerAdapter extends RecyclerView.Adapter<AnswerRecyclerAdapter.ViewHolder>{

    private List<Answers> answersList=new ArrayList<>();
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private boolean FirstTime=true;

    public AnswerRecyclerAdapter(List<Answers> answerList){
        this.answersList=answerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflating the view holder with xml objects
        context=viewGroup.getContext();
        View view=LayoutInflater.from(context).inflate(R.layout.answer_list_item,viewGroup,false);
        return new AnswerRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        final Answers answers=answersList.get(i);
        firebaseFirestore=firebaseFirestore.getInstance();

        final String id =answersList.get(i).AnswerPostId;
        viewHolder.setupvote(answersList.get(i).getUpvotes());


        viewHolder.upvotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String upVote=viewHolder.getupvote();
                if(upVote.equals("")||upVote.equals("0")){

                    viewHolder.setupvote("1");
                    answersList.get(i).setUpvotes("1");
                    firebaseFirestore.collection("Posts").document(docId).collection("Questions").document(ans_id).collection("Answers").document(id)
                            .update("upvotes","1");
                }
                else{
                    viewHolder.setupvote("0");
                    answersList.get(i).setUpvotes("0");
                    firebaseFirestore.collection("Posts").document(docId).collection("Questions").document(ans_id).collection("Answers").document(id)
                            .update("upvotes","0");
                }
            }
        });

        String answer= answers.getAnswer();
        viewHolder.setAnswer(answer);

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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            upvotes=mView.findViewById(R.id.upvote_btn);
            upVotes=mView.findViewById(R.id.upvote_text);
            answers=mView.findViewById(R.id.Answer);
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