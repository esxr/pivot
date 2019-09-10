package com.example.icasapp.Auth.StudentAuth;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.fragment.app.Fragment;

import com.example.icasapp.Auth.FormHelper;
import com.example.icasapp.Auth.RegisterLandingActivity;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class UserRegCredentialFragment extends Fragment {

    View userRegCredentialView;
    static AppCompatEditText inputFullName;
    static AppCompatEditText inputEmail;
    static AppCompatEditText inputPassword;
    AppCompatButton nextButton;
    private String email, password;
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
                if(formHelper.validatePassword(password))
                    createNewUser();
            }
        });

        return userRegCredentialView;
    }

    public void createNewUser(){

         email = inputEmail.getText().toString().trim();
         password = inputPassword.getText().toString().trim();

        mAuth
                .createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.i("msg", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getContext(), "Account creation successfull.", Toast.LENGTH_LONG).show();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.i("msg", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(userRegCredentialView.getContext(), RegisterLandingActivity.class));
                        }

                    }
                });






    }


}
