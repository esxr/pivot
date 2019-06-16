package com.example.icasapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.icasapp.Activities.NewDiscussionActivity;
import com.example.icasapp.Adapters.DiscussionRecyclerAdapter;
import com.example.icasapp.ObjectClasses.DiscussionTopic;
import com.example.icasapp.R;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**

 * A simple {@link Fragment} subclass.

 */

public class ForumFragment extends Fragment {
    private FloatingActionButton addPost ;
    private RecyclerView discussion_list_view;
    private List<DiscussionTopic> discussion_list;
    private FirebaseFirestore firebaseFirestore;
    private DiscussionRecyclerAdapter discussionRecyclerAdapter;
    private boolean isFirstPageFirstLoad =false;
    int c=0;
    public ForumFragment() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false);
        // Inflate the layout for this fragment
        addPost = view.findViewById(R.id.addPost); //getView() only works in on create View
        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postBtnIntent = new Intent(getActivity(), NewDiscussionActivity.class);
                startActivity(postBtnIntent);
            }
        });

        discussion_list_view = view.findViewById(R.id.discussion_list_view);

        discussion_list = new ArrayList<>();
        //initialising the adapter
        discussionRecyclerAdapter = new DiscussionRecyclerAdapter(discussion_list);

        discussion_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        discussion_list_view.setAdapter(discussionRecyclerAdapter);


        firebaseFirestore = FirebaseFirestore.getInstance();
        //snapshot listener lets us get the data in realtime.If


        //initialise list
   /*     if(c!=0){
        firebaseFirestore.collection("Posts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                DiscussionTopic discussionTopic = document.toObject(DiscussionTopic.class);
                                discussion_list.add(discussionTopic);
                                c=c+1;
                                Log.d(TAG, document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });}
*/

        firebaseFirestore.collection("Posts").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                //check for changes in document if data is added

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        //this is the part where every item in teh firebase document gets stored in DiscussionTopic list
                        String blogPostId=doc.getDocument().getId();
                        DiscussionTopic discussionTopic = doc.getDocument().toObject(DiscussionTopic.class).withId(blogPostId);
                        if (isFirstPageFirstLoad) {

                            discussion_list.add(discussionTopic);

                        } else {

                            discussion_list.add(0, discussionTopic);

                        }

                        discussionRecyclerAdapter.notifyDataSetChanged();
                   }
                }
                isFirstPageFirstLoad=false;
            }
        });
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onStart() {
        super.onStart();

    }

}