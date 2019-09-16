package com.example.icasapp.Menu_EditProfile;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.icasapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class EditAlumniFragment extends Fragment {

    View view;


    public EditAlumniFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_edit_alumni, container, false);
        return view;
    }

}
