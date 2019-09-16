package com.example.icasapp.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.R;
import com.example.icasapp.User.TestUser;
import com.example.icasapp.User.User;

import org.w3c.dom.Text;

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
            populateTest(user);

        } catch(Exception e) { Log.e(TAG, e.getMessage()+""); }

    }

    private void populateTest(User user) {
        // List<List<String>>
        List<List<String>> list = user.fetchList();

        // Parent layout
        LinearLayout parentLayout = (LinearLayout) findViewById(R.id.profile);

        // Layout inflater

        LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
        View view;

        // Profile Photo
        ImageView profilePhoto = new ImageView(getApplicationContext());
        float dpCalculation = getResources().getDisplayMetrics().density;

        // Customize image params
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER_HORIZONTAL;
        profilePhoto.setLayoutParams(params);

        profilePhoto.setScaleType(ImageView.ScaleType.CENTER);
        try {
            profilePhoto.getLayoutParams().height = (int) (150 * dpCalculation);
            profilePhoto.getLayoutParams().width = (int) (150 * dpCalculation);
        } catch(Exception e) {
            Log.e("mfc", e+"");
        }

        Glide.with(this).load(user.getProfilePhoto()).into(profilePhoto);
        parentLayout.addView(profilePhoto);

        // LinearLayout
        for (List<String> element : list) {
            // Add the text layout to the parent layout
            view = layoutInflater.inflate(R.layout.profilefieldelement, null);

            // In order to get the view we have to use the new view with text_layout in it
            TextView t1 = (TextView) view.findViewById(R.id.t1);
            t1.setText(element.get(0));

            TextView t2 = (TextView) view.findViewById(R.id.t2);
            t2.setText(element.get(1));

            // Add the text view to the parent layout
            parentLayout.addView(view);
        }
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
