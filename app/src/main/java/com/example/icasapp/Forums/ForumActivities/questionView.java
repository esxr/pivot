package com.example.icasapp.Forums.ForumActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.icasapp.R;
import com.jsibbold.zoomage.ZoomageView;

public class questionView extends AppCompatActivity {
    String activity;
    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.0f;
    private ZoomageView image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_view);

        Intent intent = getIntent();
        image = findViewById(R.id.image);
        String url = intent.getStringExtra("image_url");
        activity = intent.getStringExtra("activity");
        Glide.with(getApplicationContext()).load(url).into(image);



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
