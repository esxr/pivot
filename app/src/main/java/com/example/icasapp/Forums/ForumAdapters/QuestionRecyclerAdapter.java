package com.example.icasapp.Forums.ForumAdapters;

import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.icasapp.Forums.ForumActivities.AnswersActivity;
import com.example.icasapp.Forums.OnBottomReachedListener;
import com.example.icasapp.ObjectClasses.Questions;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.icasapp.Forums.ForumActivities.QuestionsActivity.docId;
import static com.example.icasapp.Forums.ForumFragment.Category;
import static com.example.icasapp.Forums.ForumFragment.collectionReference;
import static com.example.icasapp.Forums.ForumFragment.i_d;
import static com.example.icasapp.Forums.ForumFragment.setFirestoreReference;

//Adaptors are bridge between XML and ando

public class QuestionRecyclerAdapter extends RecyclerView.Adapter<QuestionRecyclerAdapter.ViewHolder> {

    public List<Questions> questionsList;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private String user_name;
    private OnBottomReachedListener onBottomReachedListener;
    private FirebaseAuth firebaseAuth;
    private ListenerRegistration listenerRegistration;

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

        setFirestoreReference(firebaseFirestore,i_d,"c");
        return new QuestionRecyclerAdapter.ViewHolder(view);
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
       final String question =  questionsList.get(i).getTopic();
       final String id=questionsList.get(i).QuestionsId;
       final String content=questionsList.get(i).getContent();
       String best_answer=questionsList.get(i).getBest_answer();

        //setting user names
        String currentUser=FirebaseAuth.getInstance().getUid();
        String uid=questionsList.get(i).getUser_id();
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
        viewHolder.setBestAnswer(best_answer);
        viewHolder.setQuestion(question);
        viewHolder.setContent(content);

       viewHolder.addAnswer.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent=new Intent(context, AnswersActivity.class);
               intent.putExtra("id",id);
               intent.putExtra("topic",question);
               intent.putExtra("content",content);
               intent.putExtra("Category",Category);
               intent.putExtra("ID",i_d);
               context.startActivity(intent);
           }
       });

       // set time
        long currentTime = Calendar.getInstance().getTime().getTime();
        long uploadtime = questionsList.get(i).getTimestamp().getTime();
        long diff=currentTime-uploadtime;
        int value= (int) (diff/(3.6*(Math.pow(10,6))));

        if(value>24)
        {
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(uploadtime)).toString();
            viewHolder.setTime(dateString);
        }
        else
        {
            String dateString = DateFormat.format("hh:mm", new Date(uploadtime)).toString();
            viewHolder.setTime(dateString);
        }

        if(currentUser.equals(questionsList.get(i).getUser_id()))
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
                collectionReference.document(docId).collection("Questions").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        questionsList.remove(i);
                        collectionReference.document(docId).update("question", FieldValue.increment(-1));
                        notifyDataSetChanged();
                    }
                });

            }
        });

   listenerRegistration= collectionReference.document(docId).collection("Questions").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
        @Override
        public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
            try {
                String n = (documentSnapshot.get("best_answer").toString());
                viewHolder.setBestAnswer(n);
            }
            catch (Exception c)
            {

            }
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

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        listenerRegistration.remove();
    }



    public void lol() {
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private TextView question;

        private Button addAnswer;

        private TextView userName;

        private TextView content;

        private TextView bestAnswer;

        private TextView time;

        private ImageView delete;

        public ViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            addAnswer=mView.findViewById(R.id.Answers);
            delete=mView.findViewById(R.id.delete);
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


        public void setBestAnswer(String best_answer)
        {
            bestAnswer=mView.findViewById(R.id.Answer);
            bestAnswer.setText(best_answer);
        }

        public void setTime(String t)
        {
            time=mView.findViewById(R.id.time);
            time.setText(t);
        }

    }

}