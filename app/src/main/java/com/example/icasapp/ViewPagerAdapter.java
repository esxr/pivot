package com.example.icasapp;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;

import com.example.icasapp.Feed.FeedFragment;
import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.Home.HomeFragment;
import com.example.icasapp.Notes.NotesFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {



    ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new NotesFragment();
            case 2:
                return new ForumFragment();
            case 3:
                return new FeedFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "HOME";
            case 1:
                return "NOTES";
            case 2:
                return "FORUMS";
            case 3:
                return "FEED";
            default:
                return null;

        }
    }
}
