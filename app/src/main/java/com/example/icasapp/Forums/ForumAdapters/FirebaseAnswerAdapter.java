package com.example.icasapp.Forums.ForumAdapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.ObjectClasses.Answers;
import com.example.icasapp.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import javax.annotation.Nullable;

import io.reactivex.annotations.NonNull;


public class FirebaseAnswerAdapter extends FirestoreRecyclerAdapter<Answers, FirebaseAnswerAdapter.FirebaseAnswerHolder> {
    private Context context;
    FirebaseFirestore firebaseFirestore;
    private ListenerRegistration listener2;
    private ListenerRegistration listener3;


    public FirebaseAnswerAdapter(@NonNull FirestoreRecyclerOptions<Answers> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final FirebaseAnswerHolder holder, final int position, @NonNull Answers model) {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();

        //progress dialogue box
        final ProgressDialog progressBar = new ProgressDialog(context);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("Upvoting.....");

        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setProgress(0);//initially progress is 0
        progressBar.setMax(100);//sets the maximum value 100

        final String currentUser=firebaseAuth.getCurrentUser().getUid();
        final String id=model.getUser_id();

        //if current user is logged
        if( model.getUser_id().equals(currentUser)){
            holder.delete.setVisibility(View.VISIBLE);
        }
        else
            holder.delete.setVisibility(View.GONE);

        holder.setAnswer(model.getAnswer());

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
                Log.i("POSITION", "INDELETE"+String.valueOf(position));
                notifyDataSetChanged();
            }
        });

        holder.setupvote(String.valueOf(model.upvotes));

        //button blue and empty is upvoted or not
        listener2 = getSnapshots().getSnapshot(position).getReference().collection("Upvotes").document().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    //if upvoted
                    holder.upvotes.setImageDrawable(context.getDrawable(R.drawable.upvote_blue));
                    Log.i("msg","upvote drawable upvote invoked");
                }
                else
                {
                    holder.upvotes.setImageDrawable(context.getDrawable(R.drawable.upvote));
                    Log.i("msg","upvote drawable downvote invoked");
                }
            }
        });

        listener3 = firebaseFirestore.collection("USER").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                                                                                                       @Override
                                                                                                       public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                                                                                                           String url = documentSnapshot.get("downloadURL").toString();
                                                                                                           holder.setProfilePic(url);
                                                                                                       }
                                                                                                   });

                holder.upvotes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        progressBar.show();
                        // viewHolder.upvotes.setEnabled(false);//displays the progress bar
                        getSnapshots().getSnapshot(position).getReference().collection("Upvotes")
                                .document(currentUser).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                if (documentSnapshot.exists()) {
                                    getSnapshots().getSnapshot(position).getReference().collection("Upvotes").document(currentUser).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("FLOW", "downvoted" + String.valueOf(position));
                                            getSnapshots().getSnapshot(position).getReference().update("upvotes", FieldValue.increment(-1)).addOnSuccessListener(new OnSuccessListener<Void>() {
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
                                    getSnapshots().getSnapshot(position).getReference().collection("Upvotes").document(currentUser).set(postMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Log.i("FLOW", "upvoted" + String.valueOf(position));
                                            getSnapshots().getSnapshot(position).getReference().update("upvotes", FieldValue.increment(1)).addOnSuccessListener(new OnSuccessListener<Void>() {
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



    }

    @NonNull
    @Override
    public FirebaseAnswerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        firebaseFirestore=firebaseFirestore.getInstance();
        View v =  LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.answer__item,viewGroup,false);
        ForumFragment.setFirestoreReference(firebaseFirestore, ForumFragment.i_d,"c");
        return new FirebaseAnswerHolder(v);
    }

    public void deleteItem(final int position){
        new AlertDialog.Builder(context)
                .setTitle("Deleting the answer")
                .setMessage("Are you sure you want to delete this entry?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        getSnapshots().getSnapshot(position).getReference().delete();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    public void upvote(int position){
        getSnapshots().getSnapshot(position).getReference().update("upvotes",FieldValue.increment(1));
    }

    public void downvote(int position) {
        getSnapshots().getSnapshot(position).getReference().update("upvotes", FieldValue.increment(-1));
    }

    class FirebaseAnswerHolder extends RecyclerView.ViewHolder{
        View mView;
        ImageView upvotes;
        TextView answers;
        TextView upVotes;
        ImageView delete;
        ImageView profilePic;

        public FirebaseAnswerHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            upvotes=mView.findViewById(R.id.upvote_btn);
            upVotes=mView.findViewById(R.id.upvote_text);
            answers=mView.findViewById(R.id.Answer);
            delete=mView.findViewById(R.id.delete);
            profilePic=mView.findViewById(R.id.profilePic);
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

        public void setProfilePic(String url)
        {
            Glide.with(context).load(url).into(profilePic);
        }

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
    public void onViewDetachedFromWindow(@androidx.annotation.NonNull FirebaseAnswerHolder holder) {
        super.onViewDetachedFromWindow(holder);
    }


}
