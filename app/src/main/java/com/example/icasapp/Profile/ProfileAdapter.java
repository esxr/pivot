package com.example.icasapp.Profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.icasapp.R;
import com.example.icasapp.User.TestUser;

import java.util.ArrayList;

public class ProfileAdapter extends ArrayAdapter<TestUser> {

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
        TextView profileName = (TextView) convertView.findViewById(R.id.profileName);
        TextView profileRegNo = (TextView) convertView.findViewById(R.id.profileRegNo);

        // Populate the data into the template view using the data object
        profileName.setText(testUser.getName());
        profileRegNo.setText(testUser.getStream());

        // Return the completed view to render on screen
        return convertView;
    }
}
