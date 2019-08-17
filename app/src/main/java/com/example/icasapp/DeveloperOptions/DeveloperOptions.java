package com.example.icasapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class DeveloperOptions extends AppCompatActivity {
    Button setSubjects;
    Button setTimeTable;
    Button announcements;
    Button setSyllabus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer_options);

        setTimeTable  = findViewById(R.id.setTimeTable);
        setSubjects   = findViewById(R.id.setSubjects);
        setSyllabus   = findViewById(R.id.setSyllabus);
        announcements = findViewById(R.id.announcements);

        setSubjects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFields(1);
            }
        });

    }

    void setFields(int option)
    {
        Intent intent = new Intent(DeveloperOptions.this,setOptions.class);
        intent.putExtra("option",option);
        startActivity(intent);
        }
    }

