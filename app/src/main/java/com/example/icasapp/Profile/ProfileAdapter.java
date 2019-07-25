package com.example.icasapp.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.icasapp.R;
import com.example.icasapp.User.TestUser;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends ArrayAdapter<TestUser> {

    TextView profileName, profileRegNo; CircleImageView profilePhoto;

    public ProfileAdapter(Context context, ArrayList<TestUser> TestUsers) {
        super(context, 0, TestUsers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        TestUser testUser = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_profile_display, parent, false);
        }

        // Lookup view for data population
        profileName = (TextView) convertView.findViewById(R.id.profileName);
        profileRegNo = (TextView) convertView.findViewById(R.id.profileRegNo);
        profilePhoto = (CircleImageView) convertView.findViewById(R.id.profilePhoto);

        // Populate the data into the template view using the data object
        profileName.setText(testUser.getName());
        profileRegNo.setText(testUser.getStream());
        Glide.with(getContext()).load(testUser.getProfilePhoto()).into(profilePhoto);

        // Return the completed view to render on screen
        return convertView;
    }
}
