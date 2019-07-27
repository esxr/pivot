package com.example.icasapp.Profile;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.icasapp.R;
import com.example.icasapp.User.TestUser;

import java.io.Serializable;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDisplayActivity extends AppCompatActivity {

    // all the views
    TextView profileName, profileRegNo, profileStream, profileSemester;
    CircleImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        // Get all the views
        profileName = (TextView) findViewById(R.id.profileName);
        profilePhoto = (CircleImageView) findViewById(R.id.profilePhoto);
        profileRegNo = (TextView) findViewById(R.id.profileRegNo);
        profileStream = (TextView) findViewById(R.id.profileStream);
        profileSemester = (TextView) findViewById(R.id.profileSemester);

        // get data for user object
        TestUser user = (TestUser) getIntent().getExtras().getSerializable("user");
        profileName.setText(user.getName());
        profileRegNo.setText(user.getRegNo());
        profileStream.setText(user.getStream());
        profileSemester.setText(user.getSemester());

        Glide.with(this).load(user.getProfilePhoto()).into(profilePhoto);

    }
}
