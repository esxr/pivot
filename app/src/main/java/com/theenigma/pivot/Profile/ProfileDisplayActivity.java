package com.theenigma.pivot.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theenigma.pivot.Firebase.FirebaseHelper;
import com.theenigma.pivot.R;
import com.theenigma.pivot.User.User;
import com.jgabrielfreitas.core.BlurImageView;

import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileDisplayActivity extends AppCompatActivity {

    // all the views
    TextView profileName, profileRegNo, profileStream, profileSemester;
    TextView profileDescription;
    CircleImageView profilePhoto;

    static String TAG = "mfc";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_display);

        // Get all the views

        // get data for user object
        try {
            User user = (User) getIntent().getExtras().getSerializable("user");
            Log.e(TAG, "lolwa:"+user.fetchList().toString());
            populateTest(user);

        } catch(Exception e) { Log.e(TAG, "lolwa:"+e.getMessage()+""); }

    }

    private void populateTest(User user) {
        // List<List<String>>
        List<List<String>> list = user.fetchList();

        // Parent layout
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.profile);

        // Layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view;

        float dpCalculation = getResources().getDisplayMetrics().density;

        // Name, Email and Profile Photo
        TextView name = findViewById(R.id.profile_name);
        TextView email = findViewById(R.id.profile_email);
        CircleImageView profilePhoto = findViewById(R.id.profile_profilePhoto);
        name.setText(user.getName());
        email.setText(user.getEmail());

        try {
            if(!user.getProfilePhoto().isEmpty()) {
                BlurImageView blurImageView = findViewById(R.id.profile_blurImage);
                Glide.with(this).load(user.getProfilePhoto()).into(profilePhoto);
                Glide.with(this).load(user.getProfilePhoto()).into(blurImageView);
        } } catch(Exception e) {}

        // LinearLayout
        for (List<String> element : list) {
            // Apply Case to elements
            String key = capitalize(element.get(0));
            String value = capitalize(element.get(1));

            // Add the text layout to the parent layout
            view = layoutInflater.inflate(R.layout.profilefieldelement, null);

            // handle Name and Email seperately
            if(element.get(0).equals("email")) continue;
            if(element.get(0).equals("name")) continue;

            // In order to get the view we have to use the new view with text_layout in it
            TextView t1 = (TextView) view.findViewById(R.id.t1);
            t1.setText(key);

            TextView t2 = (TextView) view.findViewById(R.id.t2);
            t2.setText(value);

            // Add the text view to the parent layout
            parentLayout.addView(view);
        }
    }

    public static String capitalize(String str)
    {
        if(str == null) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    private void populateView(String uid) {
        Log.e("populate", "Working 0");
        FirebaseHelper.getUserDetails(uid, new FirebaseHelper.CallbackObject<Map<String, Object>>() {
            @Override
            public void callbackCall(Map<String, Object> object) {
                populateTest(new User(object));

            }
        });
    }
}
