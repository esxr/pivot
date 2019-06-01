package com.example.icasapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.icasapp.Activities.CommentsActivity;
import com.example.icasapp.ObjectClasses.DiscussionTopic;
import com.example.icasapp.R;

import java.util.List;

public class DiscussionRecyclerAdapter extends RecyclerView.Adapter<DiscussionRecyclerAdapter.ViewHolder> {

    public List<DiscussionTopic> discussionTopicList;
    public Context context;

    public DiscussionRecyclerAdapter(List<DiscussionTopic> discussion_list){
       discussionTopicList=discussion_list;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.discussion_list_item, viewGroup, false);
        context=viewGroup.getContext();
        return new ViewHolder(view);
    }

    //bind actual data in the elements

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        //getting discussion id
        final String blogPostId = discussionTopicList.get(i).DiscussionPostid;

        //calling method from Discussion Topic class
    String content=discussionTopicList.get(i).getContent();
        viewHolder.setContentText(content);


    String url=discussionTopicList.get(i).getImage_url();
        viewHolder.setUrl(url);
        Log.i("Avijit",url);

        viewHolder.CommentBtn.setOnClickListener(new View.OnClickListener() {
                                                     @Override
                                                     public void onClick(View v) {
                                                         Intent commentIntent=new Intent(context, CommentsActivity.class);
                                                         commentIntent.putExtra("post_id", blogPostId);
                                                         context.startActivity(commentIntent);
                                                     }
                                                 });

                // String id=discussionTopicList.get(i).getId();
                // Log.i("Avijit",id);


    }
 //populates no. of items in recycler adapter
    //get the view elementts
    @Override
    public int getItemCount() {
        return discussionTopicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View mView;
        private TextView contentView;
        private ImageView imageView;
        private  ImageView CommentBtn;
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
    }
}
