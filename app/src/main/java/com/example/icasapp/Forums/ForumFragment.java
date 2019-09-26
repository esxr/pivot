package com.example.icasapp.Forums;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.example.icasapp.Forums.ForumAdapters.FirebaseDiscussionRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.icasapp.Forums.ForumActivities.NewDiscussionActivity;
import com.example.icasapp.ObjectClasses.DiscussionTopic;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

import static android.content.Context.MODE_PRIVATE;
import static android.os.Build.ID;

/**

 * A simple {@link Fragment} subclass.

 */

public class  ForumFragment extends Fragment implements AdapterView.OnItemClickListener {

   private String stream;
   private String semester;
   private boolean isFirstPageFirstLoad = false;
   public static String i_d;
   public static String Category;
   private  ArrayList<String> subject;
   private static String buffer;
   private String email;
   public static DocumentReference documentReference;

   private FloatingActionButton addPost;
   private Spinner spinner;

   private FirebaseFirestore firebaseFirestore;
   public static CollectionReference collectionReference;
   public static Query query;
   private FirebaseAuth firebaseAuth;

   private FirebaseDiscussionRecyclerAdapter adapter;

   private View view;
   private static Map docs;

    public ForumFragment() {

    }


    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_forum, container, false);

        addPost = view.findViewById(R.id.addPost);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        final String currentUser = firebaseAuth.getCurrentUser().getUid();
        i_d = null;



        //gets the stream and semester of the user and passes into findDocumentId
        firebaseFirestore.collection("USER").document(firebaseAuth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    final DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        firebaseFirestore.collection("USER").document(currentUser).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                buffer = task.getResult().get("buffer").toString();
                                Log.i("buffer",buffer);
                                //if the user is not teacher or alumni
                                if(buffer.equals("1.0")||buffer.equals("1.1")||buffer.equals("1.2")) {
                                    semester = document.get("semester").toString();
                                    stream = document.get("stream").toString();
                                    Log.i("semester", semester);
                                    Log.i("stream", stream);
                                    findDocumentIdStudent(semester, stream, view);
                                }
                                //sets the document id that contains the subjects of the particular stream and semester
                                if(buffer.equals("2.0")){
                                    email = document.get("email").toString();
                                    setSubjectArrayFaculty(email);
                                    Log.i("email",email);
                                }
                                if(buffer.equals("3.0")||buffer.equals("4.0")){
                                    subject.add("Alumni");
                                    spinner = view.findViewById(R.id.subjects);

                                    //creates new adapter with inflated subject
                                    ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subject);
                                    a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                                    spinner.setAdapter(a);


                                    //when a spinner item is selected, snapshot is added to its particular category item
                                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                        @Override
                                        public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                                            Category = subject.get(position);
                                            firebaseFirestore.collection("Specific").document("parent").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                @Override
                                                public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                                    setFirestoreReference(firebaseFirestore,i_d,"q");
                                                    //add snapshot to query
                                                    addSnapshotToQuery(query);
                                                }
                                            });

                                        }


                                        @Override
                                        public void onNothingSelected(AdapterView<?> parent) {

                                        }});
                                }
                            }
                        });
                }}
            }
        });


        addPost.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onClick(View v) {
                Intent postBtnIntent = new Intent(getActivity(), NewDiscussionActivity.class);
                //Category and document id that is passed between activities so that the value can be used globally
                postBtnIntent.putExtra("Category",Category);
                postBtnIntent.putExtra("ID",i_d);
                startActivity(postBtnIntent);
            }
        });



        //Array list inflates spinner afterwards
        //first category General is added initially
        subject=new ArrayList<>();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        if(isFirstPageFirstLoad)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(isFirstPageFirstLoad)
            adapter.stopListening();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    public void findDocumentIdStudent(String semester, String stream,final View view) {

            //the document id is retreived which contains the subjects of the stream and semester
            Query query = firebaseFirestore.collection("Specific").whereEqualTo("semester", semester).whereEqualTo("stream", stream);

            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            i_d = document.getId();
                            //set array of subjects that inflates menu later
                            setSubjectArray(i_d, view);

                        }
                    }
                }
            });
    }

    public void setSubjectArrayFaculty(String name)
    {
        firebaseFirestore.collection("Specific").whereEqualTo("email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@androidx.annotation.NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {


                    for (QueryDocumentSnapshot document : task.getResult()) {
                        //overwrites subject array list with this array list
                        subject = (ArrayList) document.get("subjects");
                        Log.i("Subjects of Faculty", String.valueOf(subject));
                    }


                    subject.add(0,"General");
                    subject.add("Alumni");
                    spinner = view.findViewById(R.id.subjects);

                        //creates new adapter with inflated subject
                        ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subject);
                        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                        spinner.setAdapter(a);


                        //when a spinner item is selected, snapshot is added to its particular category item
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                                Category = subject.get(position);
                                firebaseFirestore.collection("Specific").document("parent").get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@androidx.annotation.NonNull Task<DocumentSnapshot> task) {
                                        i_d = (String) task.getResult().get(Category);
                                        setFirestoreReference(firebaseFirestore,i_d,"q");
                                        //add snapshot to query
                                        addSnapshotToQuery(query);
                                    }
                                });

                            }


                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }

                        });


                }
            }
        });
    }



    public void setSubjectArray(final String ID, final View view) {
        firebaseFirestore.collection("Specific").document(ID).collection("Subjects").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {

                for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                    subject.add(doc.getId());
                }

                subject.add(0,"General");

                subject.add("Alumni");
                spinner = view.findViewById(R.id.subjects);

                //creates new adapter with inflated subject
                ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, subject);
                a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                spinner.setAdapter(a);

                //when a spinner item is selected, snapshot is added to its particular category item
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                        Category = subject.get(position);
                        //Query static variable is created that points to the category that has its constituent topic
                        setFirestoreReference(firebaseFirestore,i_d,"q");

                        //add snapshot to query
                        addSnapshotToQuery(query);

                    }


                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }

                });

                //after category is initialised
            }
        });


    }

    void addSnapshotToQuery(Query query)
    {

        //Query is created according to the timestamp
        Query query1 = query.orderBy("timestamp",Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<DiscussionTopic> options = new FirestoreRecyclerOptions.Builder<DiscussionTopic>()
                .setQuery(query1,DiscussionTopic.class)
                .build();
        adapter = new FirebaseDiscussionRecyclerAdapter(options);

        Context context = getContext();
        RecyclerView recyclerView = view.findViewById(R.id.discussion_list_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(true);

        addPost.show();
        setAddPost();

        adapter.startListening();
        isFirstPageFirstLoad=true;
    }



    public static void setFirestoreReference(FirebaseFirestore firebaseFirestore,String ID,String type)
    {
        if(type.equals("c")) {
            if (Category.equals("General") || Category.equals("Alumni")) {
                Log.i("LOL","SUCC");
                collectionReference = firebaseFirestore.collection("General").document(Category).collection("Posts");
            } else {

                    collectionReference = firebaseFirestore.collection("Specific").document(ID).collection("Subjects").document(Category).collection("Posts");


            }
        }
        if(type.equals("q"))
        {
            if (Category.equals("General") || Category.equals("Alumni")) {
                query = firebaseFirestore.collection("General").document(Category).collection("Posts");
            } else {


                    query = firebaseFirestore.collection("Specific").document(ID).collection("Subjects").document(Category).collection("Posts");

            }
        }
    }

    void setAddPost(){
//        if(Category.equals("Alumni")&&!(buffer.equals("3.0")||buffer.equals("4.0"))){
//            addPost.hide();
//        }
//        else {
//            if(!Category.equals("General")){
//               if(!buffer.equals("1.0"))
//            addPost.hide();
//        }}
    }
}