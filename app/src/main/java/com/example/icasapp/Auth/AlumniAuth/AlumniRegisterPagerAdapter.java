package com.example.icasapp.Auth.AlumniAuth;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.icasapp.Auth.FacultyAuth.FacultyContactFragment;
import com.example.icasapp.Auth.FacultyAuth.FacultyDescribeFragment;
import com.example.icasapp.Auth.RegisterCredentialFragment;

public class AlumniRegisterPagerAdapter extends FragmentPagerAdapter {
    public AlumniRegisterPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RegisterCredentialFragment("ALUMNI");
            case 1:
                return new AlumniContactFragment(); //WORK PH, WHERE CAN WE FIND YOU ON CAMPUS?, FREE TIMINGS AND CONTACT HRS?
            case 2:
                return new AlumniDescribeFragment(); //SUBJECT, AREAS OF INTEREST AND HOBBIES AND OTHER QUALIFICATIONS YOU WOULD LIKE TO MENTION.
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
