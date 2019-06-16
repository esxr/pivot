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

import com.example.icasapp.Activities.AnswersActivity;
import com.example.icasapp.ObjectClasses.Questions;
import com.example.icasapp.R;

import java.util.List;

//Adaptors are bridge between XML and ando

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.ViewHolder> {

    public List<Questions> questionsList;
    public Context context;

    public QuestionRecyclerAdapter(List<Questions> discussion_list){
        questionsList=discussion_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //Layout inflator takes XML as input and put view objects
        //First XML objects are inflated
        //Then the view is passed to ViewHolder **CLASS** and the view is made.
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.question_list_item, viewGroup, false);
        context=viewGroup.getContext();
        return new QuestionRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
       String question =  questionsList.get(i).getContent();

       final String id=questionsList.get(i).QuestionsId;

       viewHolder.setQuestion(question);

       viewHolder.addAnswer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(context, AnswersActivity.class);
               intent.putExtra("id",id);
               context.startActivity(intent);
           }
       });
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView question;

        private Button addAnswer;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            addAnswer=mView.findViewById(R.id.Answers);
        }

        public void setQuestion(String message){

            question = mView.findViewById(R.id.Question);
            question.setText(message);


        }



    }

}