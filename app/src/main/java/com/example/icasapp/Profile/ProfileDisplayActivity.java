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
    TextView profileName, profileRollno;
    ImageView profilePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        // Get all the views
        profileName = (TextView) findViewById(R.id.profileName);
        profileRollno = (TextView) findViewById(R.id.profileRegNo);
        profilePhoto = (ImageView) findViewById(R.id.profilePhoto);

        // get data for user object
        TestUser user = (TestUser) getIntent().getExtras().getSerializable("user");
        profileName.setText(user.getName());
        profileRollno.setText(user.getStream());
        Glide.with(this).load("http://goo.gl/gEgYUd").into(profilePhoto);

        try {
            profilePhoto.setImageBitmap(BitmapFactory.decodeFile(user.getProfilePhoto()));
        } catch(Exception e) { Log.e("Image frontend", "Problem loading image "+e); }
    }
}
