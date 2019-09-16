package com.example.icasapp.Forums.ForumAdapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.DocumentsContract;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.Forums.ForumActivities.questionView;
import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.ObjectClasses.Answers;
import com.example.icasapp.Profile.ProfileDisplayActivity;
import com.example.icasapp.R;
import com.example.icasapp.User.User;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import io.reactivex.annotations.NonNull;

import static com.example.icasapp.Forums.ForumFragment.collectionReference;
import static com.example.icasapp.Forums.ForumFragment.i_d;
import static com.example.icasapp.Forums.ForumFragment.query;


public class FirebaseAnswerAdapter extends FirestoreRecyclerAdapter<Answers, FirebaseAnswerAdapter.FirebaseAnswerHolder> {
    private Context context;
    FirebaseFirestore firebaseFirestore;
    private ListenerRegistration listener2;
    private ListenerRegistration listener3;
    String buffer;


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

        final String currentUser = firebaseAuth.getCurrentUser().getUid();
        final String id = model.getUser_id();
        String name = model.getName();
        final String url2 = model.getImage_url();

        if(!name.equals("empty"))
            firebaseFirestore.collection("USER").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                    if(documentSnapshot.exists()){
                        Log.d("NAME", documentSnapshot.get("name").toString());
                        String user_name = documentSnapshot.get("name").toString();
                        holder.setName(user_name);


                        listener3 = firebaseFirestore.collection("USER").document(id).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                            @Override
                            public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                                try {
                                    String url = documentSnapshot.get("downloadURL").toString();
                                   holder.setProfilePic(url);
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
           holder.setName("Anonymous");
        }

        // set time
        long currentTime = Calendar.getInstance().getTime().getTime();
        long uploadtime = model.getTimestamp().getTime();
        long diff = currentTime-uploadtime;
        int value= (int) (diff/(3.6*(Math.pow(10,6))));

        if(value>24)
        {
            String dateString = DateFormat.format("MM/dd/yyyy", new Date(uploadtime)).toString();
            holder.setTime(dateString);
        }
        else
        {
            String dateString = DateFormat.format("hh:mm", new Date(uploadtime)).toString();
            holder.setTime(dateString);
        }
        firebaseFirestore.collection("USER").document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                                                     @Override
                                                                                                     public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                                                                                         buffer = (String) task.getResult().get("buffer");
                                                                                                     }
                                                                                                 });
                //if current user is logged
        if( model.getUser_id().equals(currentUser) || buffer == "4"){
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

        holder.setupvote(String.valueOf(model.getUpvotes()));


        holder.setName(name);


        if(url2.equals("")){
            holder.picture.setVisibility(View.GONE);
        }else{
            holder.setPicture(url2);
            holder.picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, questionView.class);
                    intent.putExtra("image_url",url2);
                    intent.putExtra("activity","answer");
                    context.startActivity(intent);
                }
            });}

        if(!name.equals("empty")){
            holder.name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Intent intent = new Intent(context , ProfileDisplayActivity.class);
                    FirebaseHelper.getUserDetails(id, new FirebaseHelper.CallbackObject<Map<String, Object>>() {
                        @Override
                        public void callbackCall(Map<String, Object> object) {
                            Log.e("mfc", "lolwa: "+object.toString());
                            intent.putExtra("user", (User) new User(object));
                            context.startActivity(intent);
                        }
                    });
                }
            });
        }

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
                                            Log.i("FLOW", "upvoted" + position);
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

                holder.name.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context , ProfileDisplayActivity.class);
                        intent.putExtra("USER_ID",id);
                        context.startActivity(intent);
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
                        getSnapshots().getSnapshot(position).getReference().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<Void> task) {
                               getSnapshots().getSnapshot(position).getReference().getParent().getParent().update("answers",FieldValue.increment(-1));
                            }
                        });
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
        TextView moreanswers;
        TextView upVotes;
        TextView name;
        TextView time;
        ImageView delete;
        ImageView profilePic;
        ImageView picture;

        public FirebaseAnswerHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
            name = mView.findViewById(R.id.name);
            upvotes = mView.findViewById(R.id.upvote_btn);
            upVotes = mView.findViewById(R.id.upvote_text);
            moreanswers = mView.findViewById(R.id.Answer);
            delete = mView.findViewById(R.id.delete);
            time = mView.findViewById(R.id.time);
            profilePic = mView.findViewById(R.id.profilePic);
            picture = mView.findViewById(R.id.image);
        }

        public void setAnswer(String answer){
            moreanswers.setText(answer);
        }

        public void setName(String name1) { name.setText(name1); }

        public void setTime(String time1) { time.setText(time1);}

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

        public void setPicture(String url){ Glide.with(context).load(url).into(picture);}



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
