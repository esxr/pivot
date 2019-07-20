package com.example.icasapp.Forums.ForumAdapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.icasapp.Forums.ForumActivities.QuestionsActivity;
import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.ObjectClasses.DiscussionTopic;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Nullable;

import static com.example.icasapp.Forums.ForumFragment.collectionReference;

public class DiscussionRecyclerAdapter extends RecyclerView.Adapter<DiscussionRecyclerAdapter.ViewHolder> {

    public List<DiscussionTopic> discussionTopicList;
    public Context context;
    FirebaseFirestore firebaseFirestore;

    public DiscussionRecyclerAdapter(List<DiscussionTopic> discussion_list){
       discussionTopicList=discussion_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.discussion_list_item, viewGroup, false);
        context=viewGroup.getContext();
        firebaseFirestore=FirebaseFirestore.getInstance();
        return new ViewHolder(view);
    }

    //bind actual data in the elements

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
         //On Bind View is mostly used to get stuff from array list
        //getting discussion id
        final String blogPostId = discussionTopicList.get(i).DiscussionPostid;
      //  holder.itemView.setOnClickListener(v -> {
      //      DocumentSnapshot snapshot = getSnapshots().getSnapshot(holder.getAdapterPosition());
       //    docId= snapshot.getId();
            // ...
       // });

        //calling method from Discussion Topic class
    final String content=discussionTopicList.get(i).getContent();
        viewHolder.setContentText(content);

    final int question=discussionTopicList.get(i).getQuestion();
        viewHolder.setCommentCount(Integer.toString(question));

    String url=discussionTopicList.get(i).getImage_url();
        viewHolder.setUrl(url);

        long currentTime = Calendar.getInstance().getTime().getTime();
        long uploadtime =discussionTopicList.get(i).getTimestamp().getTime();

        long diff=currentTime-uploadtime;
        String difference = DateFormat.format("hh", new Date(uploadtime)).toString();
        int value= Integer.parseInt(difference);

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


        viewHolder.CommentBtn.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         Intent commentIntent=new Intent(context, QuestionsActivity.class);
                                                         commentIntent.putExtra("post_id", blogPostId);
                                                         context.startActivity(commentIntent);
                                                     }
                                                 });


                // String id=discussionTopicList.get(i).getId();
                // Log.i("Avijit",id);
        ForumFragment.setFirestoreReference(firebaseFirestore, ForumFragment.i_d,"c");


    }
 //populates no. of items in recycler adapter
    //get the view elementts
    @Override
    public int getItemCount() {
        return discussionTopicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        //used to put values in card view elements
        private View mView;
        private TextView contentView;
        private ImageView imageView;
        private  ImageView CommentBtn;
        private TextView commentCount;
        private TextView Time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;
            CommentBtn=mView.findViewById(R.id.blog_comment_icon);
        }
        public void setContentText(String text){
            contentView=mView.findViewById(R.id.forum_topic);
            contentView.setText(text);
        }
        public void setUrl(String url){
            imageView=mView.findViewById(R.id.forum_image);
            Glide.with(context).load(url).into(imageView);

        }
        public void setCommentCount(String count)
        {
            commentCount=mView.findViewById(R.id.blog_comment_count);
            String text=count+" Questions ";
            commentCount.setText(text);
        }
        public void setTime(String time)
        {
            Time=mView.findViewById(R.id.time);
            Time.setText(time);
        }
    }
}
