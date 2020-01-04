package com.theenigma.pivot.Home;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theenigma.pivot.Firebase.FirebaseHelper;
import com.theenigma.pivot.MainActivity;
import com.theenigma.pivot.Profile.ProfileAdapter;
import com.theenigma.pivot.Profile.ProfileDisplayActivity;
import com.theenigma.pivot.R;
import com.theenigma.pivot.User.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jgabrielfreitas.core.BlurImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */

public class HomeFragment extends Fragment {
    private String TAG = "mfc";
    private View homeView;

    // listview and adapter
    private ArrayList<User> items;
    private ProfileAdapter itemsAdapter;
    private ListView listView;
    private FirebaseFirestore firebaseFirestore;

    //Search
    private AutoCompleteTextView query;

    //toggle
    private Group group;
    LinearLayout homeV;
    private boolean visible = true;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        homeView = inflater.inflate(R.layout.fragment_home, container, false);

        MainActivity.isBack = false;

        new Thread(new Runnable() {
            public void run() {
                setSearchToggle();
            }
        }).start();

        new Thread(new Runnable() {
            public void run() {
                populateView(FirebaseHelper.getUser().getUid());
            }
        }).start();

//        homeView.findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getContext(), TimeTableDisplay.class));
//            }
//        });

        return homeView;
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        populateView(FirebaseHelper.getUser().getUid());
//    }

    public View getHomeView() {
        return homeView;
    }

    //toggle functionality
    private void toggle() {
        startActivity(new Intent(getContext(), SearchActivity.class));
    }


    //set all functionality
    private void setSearchToggle() {
        ImageButton searchInitButton = homeView.findViewById(R.id.initSearch);

        searchInitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });
    }

    private void populateTest(User user) {
        // List<List<String>>
        List<List<String>> list = user.fetchList();

        // Parent layout
        LinearLayout parentLayout = (LinearLayout) homeView.findViewById(R.id.homeV);

        // Layout inflater
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        View view;

        float dpCalculation = getResources().getDisplayMetrics().density;

        // Name, Email and Profile Photo
        TextView name = homeView.findViewById(R.id.name);
        TextView email = homeView.findViewById(R.id.email);
        CircleImageView profilePhoto = homeView.findViewById(R.id.profilePhoto);
        name.setText(user.getName());
        email.setText(user.getEmail());

        try{
            if(!user.getProfilePhoto().isEmpty()) {
                BlurImageView blurImageView = homeView.findViewById(R.id.blurImage);
                Glide.with(this).load(user.getProfilePhoto()).into(profilePhoto);
                Glide.with(this).load(user.getProfilePhoto()).into(blurImageView);
            }
        } catch (Exception e) { }

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
                if(object == null)
                    return;
                populateTest(new User(object));

            }
        });
    }

}