package com.theenigma.pivot.Auth.StudentAuth;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class StudentRegisterPagerAdapter extends FragmentPagerAdapter {

    int pageCount;

    public StudentRegisterPagerAdapter(FragmentManager fm){
        super(fm);
    }

    public StudentRegisterPagerAdapter(FragmentManager fm, int pageCount) {
        super(fm);
        this.pageCount = pageCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new StudentRegisterCredentialFragment();
            case 1:
                return new UserRegCredentialFragment();
            case 2:
                return new AdditionalInfoCredentialFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
