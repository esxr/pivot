package com.example.icasapp.Forums.ForumActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.icasapp.R;

public class questionView extends AppCompatActivity {
    String activity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_view);

        Intent intent = getIntent();
        ImageView image = findViewById(R.id.image);
        String url = intent.getStringExtra("image_url");
        activity = intent.getStringExtra("activity");
        Glide.with(getApplicationContext()).load(url).into(image);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
