package com.example.icasapp.Auth;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.icasapp.MainActivity;
import com.example.icasapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.reactivex.annotations.NonNull;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;

    private Button onReg;
    private Button Login;


    private String email;
    private String password;

    private FirebaseAuth mAuth;

    private ProgressDialog progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        onReg = findViewById(R.id.button3);
        Login = findViewById(R.id.login);

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(false);//you can cancel it by pressing back button
        progressBar.setMessage("LOADING ...");
        progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressBar.setMax(100);//sets the maximum value 100
        progressBar.setProgress(0);//initially progress is 0

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            // User is signed in (getCurrentUser() will be null if not signed in)
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }

        //register button
        onReg.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this, RegisterLandingActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onLogin(View view) {
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
                FirebaseLogin();
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "EMPTY FIELDS", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "AUTHENTICATION NOT SUCCESSFUL. RE-ENTER CREDENTIALS", Toast.LENGTH_LONG).show();
        }

    }

    public void FirebaseLogin() {
        progressBar.show();

        try {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("msg", "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                finish();
                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                progressBar.hide();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("msg", "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                progressBar.hide();

                            }

                            // ...
                        }
                    });
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "EMPTY FIELDS", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "AUTHENTICATION FAILED", Toast.LENGTH_LONG).show();
        } finally {
            inputEmail.requestFocus();
            progressBar.hide();
        }

    }

    public void forgotPassword(View view) {

        final EditText inputEmail = new EditText(this);
        inputEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);

        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_email)
                .setTitle("FORGOT YOUR PASSWORD?")
                .setMessage("Not a problem. Just enter your email.")
                .setCancelable(false)
                .setView(inputEmail)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mAuth = FirebaseAuth.getInstance();
                                String emailAddress = inputEmail.getText().toString();

                                if(emailAddress == null || emailAddress == " " || emailAddress == "") {
                                    inputEmail.requestFocus();
                                    Toast.makeText(getApplicationContext() , "ENTER A VALID EMAIL ADDRESS" , Toast.LENGTH_LONG).show();
                                }

                                mAuth.sendPasswordResetEmail(emailAddress)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.d("msg", "Email sent.");
                                                    Toast.makeText(getApplicationContext() , "EMAIL SENT.CHECK MAIL." , Toast.LENGTH_LONG).show();

                                                }
                                            }
                                        });

                            }
                        }
                )
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .show();
    }




}
