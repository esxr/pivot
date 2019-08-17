package com.example.icasapp.Auth.StudentAuth;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.icasapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubjectSemesterCredentialFragment extends Fragment {

    private View subSemView;


    public SubjectSemesterCredentialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        subSemView = inflater.inflate(R.layout.fragment_subject_semester_credential, container, false);

        return subSemView;
    }

}
