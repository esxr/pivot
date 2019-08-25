package com.example.icasapp.DeveloperOptions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.icasapp.Firebase.SpiderDocument;
import com.example.icasapp.Forums.FirebaseForumsHelper;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Map;

public class DeveloperOptions extends AppCompatActivity {
    Button setSubjects;
    Button setTimeTable;
    Button announcements;
    Button setSyllabus;
    Button cleanDB;
    Button authentication;
    Button setPriveledge;
    FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_options);

        firebaseFirestore = FirebaseFirestore.getInstance();

        setTimeTable   = findViewById(R.id.setTimeTable);
        setSubjects    = findViewById(R.id.setSubjects);
        setSyllabus    = findViewById(R.id.setSyllabus);
        announcements  = findViewById(R.id.announcements);
        cleanDB        = findViewById(R.id.cleanDB);
        authentication = findViewById(R.id.setAuth);
        setPriveledge  = findViewById(R.id.setPriveledge);

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
              //  firebaseForumsHelper.answerEmptyDelete();
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


    }

    void setFields(int option)
    {
        Intent intent = new Intent(DeveloperOptions.this, setOptions.class);
        intent.putExtra("option",option);
        startActivity(intent);
        }


}

