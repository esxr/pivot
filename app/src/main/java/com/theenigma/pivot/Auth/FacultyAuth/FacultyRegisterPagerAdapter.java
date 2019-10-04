package com.theenigma.pivot.Auth.FacultyAuth;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.theenigma.pivot.Auth.RegisterCredentialFragment;

public class FacultyRegisterPagerAdapter extends FragmentPagerAdapter {


    public FacultyRegisterPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new RegisterCredentialFragment("FACULTY");
            case 1:
                return new FacultyContactFragment(); //WORK PH, WHERE CAN WE FIND YOU ON CAMPUS?, FREE TIMINGS AND CONTACT HRS?
            case 2:
                return new FacultyDescribeFragment(); //SUBJECT, AREAS OF INTEREST AND HOBBIES AND OTHER QUALIFICATIONS YOU WOULD LIKE TO MENTION.
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }
}
