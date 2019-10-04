package com.theenigma.pivot;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;

import com.theenigma.pivot.Feed.FeedFragment;
import com.theenigma.pivot.Forums.ForumFragment;
import com.theenigma.pivot.Home.HomeFragment;
import com.theenigma.pivot.Notes.NotesFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {



    ViewPagerAdapter(FragmentManager manager) {
        super(manager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new ForumFragment();
            case 1:
                return new NotesFragment();
            case 2:
                return new HomeFragment();
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
                return "FORUMS";
            case 1:
                return "NOTES";
            case 2:
                return "HOME";
            case 3:
                return "FEED";
            default:
                return null;

        }
    }
}
