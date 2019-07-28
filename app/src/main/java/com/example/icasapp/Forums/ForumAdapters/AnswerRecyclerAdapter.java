package com.example.icasapp.Forums.ForumAdapters;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import static com.example.icasapp.Forums.ForumActivities.AnswersActivity.ans_id;
import static com.example.icasapp.Forums.ForumActivities.QuestionsActivity.docId;
import static com.example.icasapp.Forums.ForumFragment.collectionReference;

public class AnswerRecyclerAdapter extends RecyclerView.Adapter<AnswerRecyclerAdapter.ViewHolder>{

    private List<Answers> answersList;
    public Context context;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth firebaseAuth;
    public boolean isClickable = true;
    public ListenerRegistration registration;

    public AnswerRecyclerAdapter(List<Answers> answerList){
        this.answersList=answerList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        //inflating the view holder with xml objects
        context=viewGroup.getContext();
        View view=LayoutInflater.from(context).inflate(R.layout.answer_list_item,viewGroup,false);
       /* firebaseAuth=firebaseAuth.getInstance();
        firebaseFirestore = firebaseFirestore.getInstance();
        ForumFragment.setFirestoreReference(firebaseFirestore, ForumFragment.i_d,"c");*/
        return new AnswerRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        Answers answers = answersList.get(i);
        final String currentUser = firebaseAuth.getCurrentUser().getUid();

        Log.i("MSGG","index"+ String.valueOf(i));
        final ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Uploading");
        progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100



        final String id = answersList.get(i).AnswerPostId;
        viewHolder.setupvote(Integer.toString(answersList.get(i).getUpvotes()));

        final String answer= answers.getAnswer();
        viewHolder.setAnswer(answer);
        Log.i("MSGG","index"+answer);


        viewHolder.upvotes.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                progressBar.show();
                               // viewHolder.upvotes.setEnabled(false);//displays the progress bar
                                collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).collection("Upvotes")
                                            .document(currentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                                            if (documentSnapshot.exists()) {
                                                collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).collection("Upvotes")
                                                        .document(currentUser).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.i("FLOW", "downvoted" + String.valueOf(i));
                                                       collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).update("upvotes", FieldValue.increment(-1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                           @Override
                                                           public void onSuccess(Void aVoid) {
                                                            // viewHolder.upvotes.setEnabled(true);//displays the progress bar
                                                               progressBar.cancel();
                                                           }
                                                       });
                                                    }
                                                });

                                            } else {
                                                HashMap<String, Object> postMap = new HashMap<>();
                                                postMap.put("timestamp", FieldValue.serverTimestamp());
                                                collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).collection("Upvotes")
                                                        .document(currentUser).set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        Log.i("FLOW", "upvoted" + String.valueOf(i));
                                                       collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).update("upvotes", FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                           @Override
                                                           public void onSuccess(Void aVoid) {
                                                               //viewHolder.upvotes.setEnabled(true);
                                                               progressBar.cancel();
                                                           }
                                                       });
                                                    }
                                                });
                                            }
                                        }
                                    });
                                }

                        });


   /*   registration= collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).collection("Upvotes")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshots,
                                        @Nullable FirebaseFirestoreException e) {
                        if (snapshots.isEmpty()) {
                            Log.i("FLOW", "WHILE UPVOTE" + i);
                            try {
                                Log.i("LOL", "Position" + i);
                                Log.i("LOL", answersList.get(i).getAnswer() + "SetUpvote" + answersList.get(i).upvotes);
                                answersList.get(i).setUpvotes(0);
                                Log.i("LOL", answersList.get(i).getAnswer() + "SetUpvote" + answersList.get(i).upvotes);
                                viewHolder.setupvote("0");
                                sortAdapter();
                            } catch (Exception c) {

                            }
                            isClickable = true;
                        } else {
                            try {
                                Log.i("LOL", "Position" + i);
                                String count = Integer.toString(snapshots.size());
                                int c = Integer.parseInt(count);
                                Log.i("LOL", answersList.get(i).getAnswer() + "SetUpvote" + answersList.get(i).upvotes);
                                answersList.get(i).setUpvotes(c);
                                Log.i("LOL", answersList.get(i).getAnswer() + "SetUpvote" + answersList.get(i).upvotes);
                                viewHolder.setupvote(count);
                                sortAdapter();
                            } catch (Exception c) {

                            }
                        }
                    }
                });*/

      registration= collectionReference.document(docId).collection("Questions").document(ans_id).collection("Answers").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
          @Override
          public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
            try {
                String upvotes = documentSnapshot.get("upvotes").toString();
                if (answersList.get(i).getUpvotes() == Integer.parseInt(upvotes)) {
                    Log.i("MESSAGE", "INVOKED");

                } else {
                    try {
                        Log.i("LOL", "Position" + i);
                        String count = upvotes;
                        int c = Integer.parseInt(count);
                        Log.i("LOL", answersList.get(i).getAnswer() + "SetUpvote" + answersList.get(i).upvotes);
                        answersList.get(i).setUpvotes(c);
                        Log.i("LOL", answersList.get(i).getAnswer() + "SetUpvote" + answersList.get(i).upvotes);
                        viewHolder.setupvote(count);

                    } catch (Exception c) {

                    }
                }

            }
            catch (Exception c)
            {

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
                        Log.i("MSGG", "INDEX"+String.valueOf(i));
                        answersList.remove(i);
                        notifyItemRemoved(i);
                        Log.i("MSGG", String.valueOf(answersList.size()));
                      //  sortAdapter();
                    }
                });
            }
        });


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
    public int getItemCount() {
        return answersList.size();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        registration.remove();
    }

    public void notifyAdapterDataSetChanged()
    {
        registration.remove();
        notifyDataSetChanged();
    }

    public void sortAdapter()
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
        notifyDataSetChanged();
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
