package com.example.icasapp.Auth;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.MainActivity;
import com.example.icasapp.R;
import com.example.icasapp.User.TestUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import de.hdodenhof.circleimageview.CircleImageView;
import in.codeshuffle.typewriterview.TypeWriterView;

public class RegisterActivity extends AppCompatActivity {

    private EditText inputEmail;
    private EditText inputPassword;
    private EditText inputUsername, inputRegNo;

    private String email, username, regNo;
    private String password;
    private RelativeLayout relativeLayout;
    private CircleImageView circleImageView;

    private FirebaseAuth mAuth;
    private TypeWriterView typeWriterView;

    public String getUsername() {
        return username;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        inputEmail = findViewById(R.id.inputEmail);
        inputUsername = findViewById(R.id.inputUsername);
        inputRegNo = findViewById(R.id.inputRegNo);
        inputPassword = findViewById(R.id.inputPassword);
        relativeLayout = findViewById(R.id.rl);
        circleImageView = findViewById(R.id.circle_avi);

        //TYPE WRITER EFFECT
        //Create Object and refer to layout view
         typeWriterView=(TypeWriterView)findViewById(R.id.typeWriterView);

        //Setting each character animation delay
        typeWriterView.setDelay(300);

        //Setting music effect On/Off
        typeWriterView.setWithMusic(false);

        //Animating Text
        typeWriterView.animateText("Hello, welcome to the Official ICAS App. \nBy registering, you'll agree to the terms and conditions that you never read.");



        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Animations (UI)
        Animation fade = AnimationUtils.loadAnimation(this,R.anim.fade_scale);
        Animation transition = AnimationUtils.loadAnimation(this,R.anim.fade_transition);
        relativeLayout.setAnimation(fade);
        circleImageView.setAnimation(transition);

    }

    public void onRegister(View view){

        try {

            email = inputEmail.getText().toString().trim();
            password = inputPassword.getText().toString().trim();
            username = inputUsername.getText().toString().trim();
            regNo = inputRegNo.getText().toString().trim();

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

                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(getUsername())
                                        .build();

                                // Add a database entry in "USER" database
                                String generic_profile_photo_path = "https://firebasestorage.googleapis.com/v0/b/icas-phase-1.appspot.com/o/USER_profile_photos%2Fgeneric_profile_photo.png?alt=media&token=0eafbb20-0e6c-4c66-afd2-90b784c3d026";
                                TestUser user_USER = new TestUser(
                                        username,
                                        "1",
                                        "CSE",
                                        regNo,
                                        getResources().getString(R.string.generic_profile_photo_path),
                                        user.getUid()
                                );
                                FirebaseHelper.addDocumentToCollection(user_USER.getFirebaseDocument(), "USER");



                                Log.e("Registration", "Registration successful");
                                // ---------------------------------------

                                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.e("msg", "createUserWithEmail:failure", task.getException());
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
