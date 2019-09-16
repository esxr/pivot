package com.example.icasapp.Auth;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.example.icasapp.Alumni;
import com.example.icasapp.Faculty;
import com.example.icasapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterCredentialFragment extends Fragment {

    View registerCredentialFragment;
    AppCompatButton nextButton;
    FormHelper formHelper;
    EditText inputEmail;
    EditText inputPassword;
    public static String  email, password, userType;



    public RegisterCredentialFragment() {
        // Required empty public constructor
    }

    public RegisterCredentialFragment(String userType) {
        // Required empty public constructor
        Log.i("msg", userType);
        RegisterCredentialFragment.userType = userType;
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
                email = inputEmail.getText().toString().trim();
                password = inputPassword.getText().toString().trim();

                if(formHelper.validateEmail(email) && formHelper.validatePassword(password)){
                    RegisterProgressActivity.pager.setCurrentItem(1);
                    Log.i("msg", email);
                    if(userType.equalsIgnoreCase("FACULTY"))
                        Faculty.faculty.setEmail(email);
                    else
                        Alumni.alumni.setEmail(email);
                }
                else {
                    Toast.makeText(registerCredentialFragment.getContext(), "INVALID OR INCOMPLETE CREDENTIALS.", Toast.LENGTH_LONG).show();
                    if(!formHelper.validateEmail(email))
                        inputEmail.requestFocus();
                    else
                        inputPassword.requestFocus();
                }
            }
        });

        return registerCredentialFragment;
    }

}
