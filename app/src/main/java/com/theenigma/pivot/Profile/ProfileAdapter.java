package com.theenigma.pivot.Profile;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.theenigma.pivot.R;
import com.theenigma.pivot.User.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends ArrayAdapter<User> {

    private TextView profileName, profileRegNo;
    private CircleImageView profilePhoto;

    public ProfileAdapter(Context context, ArrayList<User> TestUsers) {
        super(context, 0, TestUsers);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        User testUser = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.profile_list_item, parent, false);
        }

        // Lookup view for data population
        profileName = (TextView) convertView.findViewById(R.id.list_profileName);
        profileRegNo = (TextView) convertView.findViewById(R.id.list_profileRegNo);
        profilePhoto = (CircleImageView) convertView.findViewById(R.id.list_profilePhoto);

        // Populate the data into the template view using the data object
        try {
            profileName.setText(testUser.getName());
            profileRegNo.setText(testUser.getStream());
            Glide.with(getContext()).load(testUser.getProfilePhoto()).into(profilePhoto);
        } catch(Exception e) { Log.e("testUser.getName()", e.getLocalizedMessage()); }

        // Return the completed view to render on screen
        return convertView;
    }
}
