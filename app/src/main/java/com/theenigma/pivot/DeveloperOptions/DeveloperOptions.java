package com.theenigma.pivot.DeveloperOptions;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.theenigma.pivot.Forums.FirebaseForumsHelper;
import com.theenigma.pivot.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DeveloperOptions extends AppCompatActivity {
    Button setSubjects;
    Button setTimeTable;
    Button announcements;
    Button setSyllabus;
    Button cleanDB;
    Button authentication;
    Button setFacultySubjects;
    Button setPriveledge;
    ArrayList arrayList;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_options);

        firebaseFirestore = FirebaseFirestore.getInstance();

        setTimeTable       = findViewById(R.id.setTimeTable);
        setSubjects        = findViewById(R.id.setSubjects);
        setFacultySubjects = findViewById(R.id.setFacsub);
        setSyllabus        = findViewById(R.id.setSyllabus);
        announcements      = findViewById(R.id.announcements);
        cleanDB            = findViewById(R.id.cleanDB);
        authentication     = findViewById(R.id.setAuth);
        setPriveledge      = findViewById(R.id.setPriveledge);

        setSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFields(1);
            }
        });

        cleanDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("DEVOPS","clicked");
                FirebaseForumsHelper firebaseForumsHelper = new FirebaseForumsHelper();
                //firebaseForumsHelper.answerEmptyDelete();
                firebaseForumsHelper.emptyTopicDelete();
               // firebaseForumsHelper.questionEmptyDelete();
            }
        });

        setPriveledge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),setUserPriveledge.class));
            }
        });

        setFacultySubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });

//        FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
//        arrayList = new ArrayList();
//
//        firebaseFirestore.collection("Specific").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for(DocumentSnapshot documentSnapshot : task.getResult()){
//                    final DocumentReference documentReference = documentSnapshot.getReference();
//                    documentReference.collection("Subjects").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                        @Override
//                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                            for(DocumentSnapshot documentSnapshot : task.getResult()){
//                                arrayList.add(documentSnapshot.getId());
//                            }
//                            HashMap map = new HashMap();
//                            map.put("subjects",arrayList);
//                            documentReference.set(map,SetOptions.merge());
//                            arrayList.clear();
//                        }
//                    });
//                }
//
//            }
//        });


    }

    void setFields(int option)
    {
        Intent intent = new Intent(DeveloperOptions.this, setOptions.class);
        intent.putExtra("option",option);
        startActivity(intent);
        }


}

