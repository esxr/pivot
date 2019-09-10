package com.example.icasapp.Auth;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.icasapp.R;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterCredentialFragment extends Fragment {

    View registerCredentialFragment;
    Button nextButton;
    FormHelper formHelper;
    EditText inputEmail;
    EditText inputPassword;
    String email, password;

    private FirebaseAuth mAuth;





    public RegisterCredentialFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        registerCredentialFragment =  inflater.inflate(R.layout.fragment_register_credential, container, false);

        nextButton = registerCredentialFragment.findViewById(R.id.nextButton);
        inputEmail = registerCredentialFragment.findViewById(R.id.inputEmail);
        inputPassword = registerCredentialFragment.findViewById(R.id.inputPassword);

        formHelper = new FormHelper();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = inputEmail.getText().toString();
                password = inputPassword.getText().toString();

                if(formHelper.validateEmail(email) && formHelper.validatePassword(password)){
                    signIn();
                }
                else {
                    Toast.makeText(registerCredentialFragment.getContext(), "INVALID OR INCOMPLETE CREDENTIALS.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return registerCredentialFragment;
    }

    private void signIn(){
    }

}
