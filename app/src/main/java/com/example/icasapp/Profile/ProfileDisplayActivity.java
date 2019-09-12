package com.example.icasapp.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.R;
import com.example.icasapp.User.TestUser;

import org.w3c.dom.Text;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDisplayActivity extends AppCompatActivity {

    // all the views
    TextView profileName, profileRegNo, profileStream, profileSemester;
    TextView profileDescription;
    CircleImageView profilePhoto;
    TestUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        Intent intent = getIntent();
        String id = intent.getStringExtra("USER_ID");
        FirebaseHelper.findDocumentWithUID(id, new FirebaseHelper.CallbackObject<Map<String, Object>>() {
            @Override
            public void callbackCall(Map<String, Object> object) {
               user = new TestUser(object);
               setData(new TestUser(object));
            }
        });
        Log.i("OBJECT_ID",id);

        // Get all the views
        profileName = (TextView) findViewById(R.id.profileName);
        profilePhoto = (CircleImageView) findViewById(R.id.profilePhoto);
        profileRegNo = (TextView) findViewById(R.id.profileRegNo);
        profileStream = (TextView) findViewById(R.id.profileStream);
        profileSemester = (TextView) findViewById(R.id.profileSemester);
        profileDescription = (TextView) findViewById(R.id.profileDescription);

        // get data for user object
//        TestUser user = (TestUser) getIntent().getExtras().getSerializable("user");
//        profileName.setText(user.getName());
//        profileRegNo.setText(user.getRegNo());
//        profileStream.setText(user.getStream());
//        profileSemester.setText(user.getSemester());
//        profileDescription.setText(user.getDescription());
//        Glide.with(this).load(user.getProfilePhoto()).into(profilePhoto);

    }

    void setData(TestUser user) {
        profileName.setText(user.getName());
        profileRegNo.setText(user.getRegNo());
        profileStream.setText(user.getStream());
        profileSemester.setText(user.getSemester());
        profileDescription.setText(user.getDescription());
        Glide.with(this).load(user.getProfilePhoto()).into(profilePhoto);
    }
}
