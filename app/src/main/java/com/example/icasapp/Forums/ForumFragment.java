package com.example.icasapp.Forums;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.icasapp.Forums.ForumActivities.NewDiscussionActivity;
import com.example.icasapp.Forums.ForumAdapters.DiscussionRecyclerAdapter;
import com.example.icasapp.ObjectClasses.DiscussionTopic;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**

 * A simple {@link Fragment} subclass.

 */

public class ForumFragment extends Fragment implements AdapterView.OnItemClickListener {
    private FloatingActionButton addPost;
    private RecyclerView discussion_list_view;
    private List<DiscussionTopic> discussion_list;
    private FirebaseFirestore firebaseFirestore;
    private DiscussionRecyclerAdapter discussionRecyclerAdapter;
    private boolean isFirstPageFirstLoad = false;
    private FirebaseAuth firebaseAuth;
    private String stream;
    private String semester;
    private  ArrayList<String> subject;
    Spinner spinner;
    public static String i_d;
    static String Category;
    public static CollectionReference collectionReference;
    public static Query query;
    int c = 0;

    public ForumFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_forum, container, false);
        // Inflate the layout for this fragment

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        discussion_list_view = view.findViewById(R.id.discussion_list_view);

        discussion_list = new ArrayList<>();
        //initialising the adapter
        discussionRecyclerAdapter = new DiscussionRecyclerAdapter(discussion_list);

        discussion_list_view.setLayoutManager(new LinearLayoutManager(container.getContext()));
        discussion_list_view.setAdapter(discussionRecyclerAdapter);
        ViewCompat.setNestedScrollingEnabled(discussion_list_view, false);

        //creates subjects menu
        firebaseFirestore.collection("USER").document(firebaseAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        semester = document.get("semester").toString();
                        stream = document.get("stream").toString();
                        //sets the document id according to semester and stream
                        findDocumentId(semester, stream,view);

                    }
                }
            }
        });


        addPost = view.findViewById(R.id.addPost); //getView() only works in on create View
        addPost.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Intent postBtnIntent = new Intent(getActivity(), NewDiscussionActivity.class);
                startActivity(postBtnIntent);
            }
        });
        //snapshot listener lets us get the data in realtime.
        subject=new ArrayList<>();
        subject.add("General");



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

    public void getMenuItem() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public String findDocumentId(String semester, String stream,final View view) {

        Query query = firebaseFirestore.collection("Specific").whereEqualTo("semester", semester).whereEqualTo("stream", stream);

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        i_d=document.getId();
                        //set array of subjects that inflates menu later
                        setSubjectArray(i_d,view);
                    }
                }
            }
        });
        return semester;
    }

    public void setSubjectArray(final String ID, final View view) {
        firebaseFirestore.collection("Specific").document(ID).collection("Subjects").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    subject.add(doc.getId());


                }
                subject.add("Alumni");
                spinner = view.findViewById(R.id.subjects);
                // spinner.setOnItemClickListener(this);
                ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subject);
                a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(a);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        Category =subject.get(position);
                        Log.i("bv",Category);
                        setFirestoreReference(firebaseFirestore,i_d,"q");
                       if(isFirstPageFirstLoad)
                       {
                           addSnapshotToQuery(query,false);
                       }
                       else {
                           addSnapshotToQuery(query,true);
                       }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });
            }
        });


    }

    void addSnapshotToQuery(Query query,Boolean changed)
    {
        if(changed)
        {
            discussion_list.clear();
            discussionRecyclerAdapter.notifyDataSetChanged();
        }
        query.orderBy("timestamp",Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                //check for changes in document if data is added

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                        //this is the part where every item in the Firebase document gets stored in DiscussionTopic list
                        String blogPostId = doc.getDocument().getId();
                        DiscussionTopic discussionTopic = doc.getDocument().toObject(DiscussionTopic.class).withId(blogPostId);


                            discussion_list.add(discussionTopic);



                        discussionRecyclerAdapter.notifyDataSetChanged();
                    }
                }
                isFirstPageFirstLoad = false;
            }
        });
    }

    public static void setFirestoreReference(FirebaseFirestore firebaseFirestore,String ID,String type)
    {
        if(type=="c") {
            if (Category == "General" || Category == "Alumni") {
                collectionReference = firebaseFirestore.collection("General").document(Category).collection("Posts");
            } else {
                collectionReference = firebaseFirestore.collection("Specific").document(ID).collection("Subjects").document(Category).collection("Posts");

            }
        }
        if(type=="q")
        {
            if (Category == "General" || Category == "Alumni") {
                query = firebaseFirestore.collection("General").document(Category).collection("Posts");
            } else {
                query = firebaseFirestore.collection("Specific").document(ID).collection("Subjects").document(Category).collection("Posts");

            }
        }
    }


}