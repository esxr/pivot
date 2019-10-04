package com.theenigma.pivot.Auth.StudentAuth;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.theenigma.pivot.Auth.FormHelper;
import com.theenigma.pivot.Auth.RegisterProgressActivity;
import com.theenigma.pivot.R;
import com.google.firebase.auth.FirebaseAuth;


public class UserRegCredentialFragment extends Fragment {

    View userRegCredentialView;
    static AppCompatEditText inputFullName;
    static AppCompatEditText inputEmail;
    static AppCompatEditText inputPassword;
    AppCompatButton nextButton;
    private String email;
    static String password;
    FormHelper formHelper;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        userRegCredentialView =  inflater.inflate(R.layout.fragment_student_user_reg_credential, container, false);

        nextButton = userRegCredentialView.findViewById(R.id.nextButton);

        formHelper = new FormHelper();
        inputEmail = userRegCredentialView.findViewById(R.id.inputEmail);
        inputFullName = userRegCredentialView.findViewById(R.id.inputFullName);
        inputPassword = userRegCredentialView.findViewById(R.id.inputPassword);

        inputEmail.setEnabled(false);
        inputEmail.setTextColor(Color.BLACK);

        inputFullName.setEnabled(false);
        inputFullName.setTextColor(Color.BLACK);


        mAuth = FirebaseAuth.getInstance();

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                password = inputPassword.getText().toString();
                if(formHelper.validatePassword(password)) {
                    store();
                    updateUI();
                    RegisterProgressActivity.stepView.go(2,true);
                    RegisterProgressActivity.i++;
                }else{
                    Toast.makeText(getContext(), "PASSWORD SHOULD BE OF ATLEAST 6 CHARACTERS.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return userRegCredentialView;
    }

    public void store(){

         email = inputEmail.getText().toString().trim();
         password = inputPassword.getText().toString().trim();

    }

    public void updateUI(){
        RegisterProgressActivity.pager.setCurrentItem(2);
    }



}

