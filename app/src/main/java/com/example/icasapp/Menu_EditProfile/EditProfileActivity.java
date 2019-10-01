package com.example.icasapp.Menu_EditProfile;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.icasapp.GlobalState;
import com.example.icasapp.NonSwipeableViewpager;
import com.example.icasapp.R;

public class EditProfileActivity extends AppCompatActivity {

    NonSwipeableViewpager pager;
    EditProfilePagerAdapter editProfilePagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        pager = findViewById(R.id.editPager);
        editProfilePagerAdapter = new EditProfilePagerAdapter(getSupportFragmentManager());
        pager.setAdapter(editProfilePagerAdapter);

        switch (GlobalState.globalState.getUserType()){
            case "STUDENT":
                pager.setCurrentItem(0);
                break;
            case "FACULTY":
                pager.setCurrentItem(1);
                break;
            case "ALUMNI":
                pager.setCurrentItem(2);
                break;
            default:
                break;
        }
    }


}
