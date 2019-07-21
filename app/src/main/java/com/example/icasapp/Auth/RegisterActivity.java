package com.example.icasapp.Auth;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.icasapp.MainActivity;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;

    private String email;
    private String password;
    private RelativeLayout relativeLayout;
    private CircleImageView circleImageView;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setTitle("REGISTER");

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        relativeLayout = findViewById(R.id.rl);
        circleImageView = findViewById(R.id.circle_avi);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        Animation fade = AnimationUtils.loadAnimation(this,R.anim.fade_scale);
        Animation transition = AnimationUtils.loadAnimation(this,R.anim.fade_transition);
        relativeLayout.setAnimation(fade);
        circleImageView.setAnimation(transition);





    }



    public void onRegister(View view){

        try {

            email = inputEmail.getText().toString().trim();
            password = inputPassword.getText().toString().trim();

            if (email == null) {
                Toast.makeText(getApplicationContext(), "EMAIL ID IS MISSING", Toast.LENGTH_LONG).show();
                inputEmail.requestFocus();
            } else if (password == null) {
                Toast.makeText(getApplicationContext(), "PASSWORD IS MISSING", Toast.LENGTH_LONG).show();
                inputPassword.requestFocus();
            } else if (email == null && password == null) {
                Toast.makeText(getApplicationContext(), "BOTH FIELDS ARE EMPTY", Toast.LENGTH_LONG).show();
                inputEmail.requestFocus();
            } else
                FirebaseRegister();
        }catch(IllegalArgumentException e){
            Toast.makeText(getApplicationContext() , "INVALID CREDENTIALS OR EMPTY FIELDS" , Toast.LENGTH_LONG).show();
            }
         catch(Exception e){
            Toast.makeText(getApplicationContext() , "AUTHENTICATION NOT SUCCESSFUL. RE-ENTER CREDENTIALS" , Toast.LENGTH_LONG).show();
         }


    }

    public void FirebaseRegister(){
        try {

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("msg", "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("msg", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            // ...
                        }
                    });
        }catch(IllegalArgumentException e){
                Toast.makeText(getApplicationContext(),"EMPTY FIELDS" , Toast.LENGTH_LONG).show();
            }catch(Exception e){
                Toast.makeText(getApplicationContext(),"AUTHENTICATION FAILED",Toast.LENGTH_LONG).show();
            } finally {
                inputEmail.requestFocus();
        }
    }

    public void onLogin(View view){
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
    }



}
