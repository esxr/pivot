package com.example.icasapp.Forums.ForumActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.icasapp.R;

public class questionView extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_view);

        Intent intent = getIntent();
        ImageView image = findViewById(R.id.image);
        String url = intent.getStringExtra("image_url");
        Glide.with(getApplicationContext()).load(url).into(image);


    }

}
