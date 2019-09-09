package com.example.icasapp.Forums.ForumAdapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.icasapp.Forums.ForumActivities.AnswersActivity;
import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.ObjectClasses.Questions;
import com.example.icasapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

import io.reactivex.annotations.Nullable;

import static com.example.icasapp.Forums.ForumActivities.QuestionsActivity.docId;
import static com.example.icasapp.Forums.ForumFragment.Category;
import static com.example.icasapp.Forums.ForumFragment.collectionReference;
import static com.example.icasapp.Forums.ForumFragment.i_d;

public class FirebaseQuestionRecyclerAdapter extends FirestoreRecyclerAdapter<Questions, FirebaseQuestionRecyclerAdapter.QuestionHolder> {

    FirebaseFirestore firebaseFirestore;
    Context context;
    private ListenerRegistration listener1;
    private ListenerRegistration listener3;
    private String bestAnswer;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FirebaseQuestionRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Questions> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final QuestionHolder questionHolder, final int i, @NonNull Questions questions) {

        final String question =  questions.getTopic();
        final String id = getSnapshots().getSnapshot(i).getId();
        final String content = questions.getContent();
        String name = questions.getName();
        String best_answer = questions.getBest_answer();

        //setting user names
        String currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        final String uid = questions.getUser_id();

        if(!name.equals("empty"))
        firebaseFirestore.collection("USER").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    Log.d("NAME", documentSnapshot.get("name").toString());
                    String user_name = documentSnapshot.get("name").toString();
                    questionHolder.setUsername(user_name);


                    listener3 = firebaseFirestore.collection("USER").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                        @Override
                        public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                            try {
                                String url = documentSnapshot.get("downloadURL").toString();
                                questionHolder.setProfileView(url);
                            }
                            catch (Exception d)
                            {
                                Log.d("msg","Question Adapter. No photo of user");
                            }
                        }
                    });
                }
            }
        });
        else{
            questionHolder.setUsername("Anonymous");
        }

        questionHolder.setQuestion(question);
        questionHolder.setBestAnswer(best_answer);
        questionHolder.setQuestion(question);
        questionHolder.setContent(content);

        String totalAnswers = getSnapshots().getSnapshot(i).get("answers").toString();
        Log.i("total answers",totalAnswers);
        questionHolder.setDisplayAnswers(totalAnswers);

        questionHolder.addAnswer.setOnClickListener(new View.OnClickListener() {
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
        long uploadtime = questions.getTimestamp().getTime();
        long diff = currentTime-uploadtime;
        int value= (int) (diff/(3.6*(Math.pow(10,6))));

        if(value>24)
        {
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(uploadtime)).toString();
            questionHolder.setTime(dateString);
        }
        else
        {
            String dateString = DateFormat.format("hh:mm", new Date(uploadtime)).toString();
            questionHolder.setTime(dateString);
        }

        if(currentUser.equals(questions.getUser_id()))
        {
            questionHolder.delete.setVisibility(View.VISIBLE);
        }
        else
        {
            questionHolder.delete.setVisibility(View.GONE);
        }

        questionHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(i);
                notifyDataSetChanged();
            }
        });


        //sets best answer
       bestAnswer(i,questionHolder);

    }

    @NonNull
    @Override
    public QuestionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        firebaseFirestore = FirebaseFirestore.getInstance();
        context = parent.getContext();
        ForumFragment.setFirestoreReference(firebaseFirestore,i_d,"c");
        View v = LayoutInflater.from(context).inflate(R.layout.question_list_item,parent,false);
        return new QuestionHolder(v);
    }

    class QuestionHolder extends RecyclerView.ViewHolder{

        private View mView;

        private TextView question;

        private Button addAnswer;

        private TextView userName;

        private TextView content;

        private TextView bestAnswer;

        private TextView time;

        private ImageView delete;

        private ImageView profileView;

        private TextView displayAnswers;


        public QuestionHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            addAnswer = mView.findViewById(R.id.Answers);
            delete = mView.findViewById(R.id.delete);
            displayAnswers = mView.findViewById(R.id.answers);
        }

        public void setQuestion(String message){

            question = mView.findViewById(R.id.Question);
            question.setText(message);

        }

        public void setContent(String message)
        {
            content = mView.findViewById(R.id.Content);
            content.setText(message);
        }

        public void setUsername(String username)
        {
            userName = mView.findViewById(R.id.user_name);
            userName.setText(username);
        }


        public void setBestAnswer(String best_answer)
        {
            bestAnswer = mView.findViewById(R.id.Answer);
            bestAnswer.setText(best_answer);
        }

        public void setTime(String t)
        {
            time = mView.findViewById(R.id.time);
            time.setText(t);
        }

        public void setProfileView(String url)
        {
            profileView = mView.findViewById(R.id.profilePic);
            Glide.with(context).load(url).into(profileView);
        }

        public void setDisplayAnswers(String number) { displayAnswers.setText(number+" answers");}

    }

    public void delete(final int position)
    {
        new AlertDialog.Builder(context)
                .setTitle("Deleting the topic")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        getSnapshots().getSnapshot(position).getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                collectionReference.document(docId).update("question", FieldValue.increment(-1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,"Deleted",Toast.LENGTH_LONG).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {

                                        Toast.makeText(context,"Something went wrong",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        });
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
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
    public void onViewDetachedFromWindow(@NonNull QuestionHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }

    void bestAnswer(int position, final QuestionHolder questionHolder)
    {
        //if no answers best answer will be "no answers"
        bestAnswer = "no answers";
       getSnapshots().getSnapshot(position).getReference().collection("Answers").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
           @Override
           public void onComplete(@NonNull Task<QuerySnapshot> task) {
               long max = 0;
               for(QueryDocumentSnapshot documentSnapshot : task.getResult())
               {
                   long n  = (long) documentSnapshot.get("upvotes");
                   if(n>max)
                   {
                       bestAnswer =  documentSnapshot.get("answer").toString();
                       max=n;
                   }
               }
               questionHolder.setBestAnswer(bestAnswer);
           }
       });
    }
}