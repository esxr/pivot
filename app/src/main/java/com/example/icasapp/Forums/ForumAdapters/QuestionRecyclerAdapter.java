package com.example.icasapp.Forums.ForumAdapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.icasapp.Forums.ForumActivities.AnswersActivity;
import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.Forums.OnBottomReachedListener;
import com.example.icasapp.ObjectClasses.Questions;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

//Adaptors are bridge between XML and ando

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.ViewHolder> {

    public List<Questions> questionsList;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private String user_name;
    private OnBottomReachedListener onBottomReachedListener;

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
        firebaseFirestore=firebaseFirestore.getInstance();
        return new QuestionRecyclerAdapter.ViewHolder(view);
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
       String question =  questionsList.get(i).getTopic();
       try{
       Log.i("Ques",question);}
       catch (Exception e)
       {

       }

       final String id=questionsList.get(i).QuestionsId;

       String uid=questionsList.get(i).getUser_id();

       String content=questionsList.get(i).getContent();

       ForumFragment.setFirestoreReference(firebaseFirestore, ForumFragment.i_d,"c");
       firebaseFirestore.collection("USER").document(uid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {


           @Override
                                                                                          public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                              if(task.isSuccessful()){
                                                                                                  DocumentSnapshot document=task.getResult();
                                                                                                  if(document.exists()){
                                                                                                      Log.d("NAME",document.get("name").toString());
                                                                                                      user_name = document.get("name").toString();
                                                                                                      viewHolder.setUsername(user_name);
                                                                                                  }
                                                                                              }
                                                                                          }
                                                                                      });

               viewHolder.setQuestion(question);

               viewHolder.setContent(content);

       viewHolder.addAnswer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(context, AnswersActivity.class);
               intent.putExtra("id",id);
               context.startActivity(intent);
           }
       });

       //this ensures whenever the condition end is reached OnBottomReached is invoked
       if(i==questionsList.size()-1) {
           onBottomReachedListener.OnBottomReached(i);
       }
    }

    @Override
    public int getItemCount() {
        return questionsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView question;

        private Button addAnswer;

        private TextView userName;

        private TextView content;


        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            addAnswer=mView.findViewById(R.id.Answers);
        }

        public void setQuestion(String message){

            question = mView.findViewById(R.id.Question);
            question.setText(message);

        }

        public void setContent(String message)
        {
            content=mView.findViewById(R.id.Content);
            content.setText(message);
        }

        public void setUsername(String username)
        {
            userName=mView.findViewById(R.id.user_name);
            userName.setText(username);
        }



    }

}