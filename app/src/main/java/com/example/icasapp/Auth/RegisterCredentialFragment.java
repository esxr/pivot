package com.example.icasapp.Auth;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.icasapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterCredentialFragment extends Fragment {

    View registerCredentialFragment;



    public RegisterCredentialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registerCredentialFragment =  inflater.inflate(R.layout.fragment_register_credential, container, false);

        return registerCredentialFragment;
    }

}
