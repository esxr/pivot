package com.theenigma.pivot.Menu_EditProfile;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class EditProfilePagerAdapter extends FragmentPagerAdapter {
    public EditProfilePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new EditStudentFragment();
            case 1:
                return new EditFacultyFragment();
            case 2:
                return new EditAlumniFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
