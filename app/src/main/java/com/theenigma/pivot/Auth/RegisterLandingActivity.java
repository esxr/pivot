package com.theenigma.pivot.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.theenigma.pivot.R;

public class RegisterLandingActivity extends AppCompatActivity {

    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_landing);

        i = new Intent(getApplicationContext(), RegisterProgressActivity.class);
    }

    public void onStudent(View view){
        i.putExtra("userType", "STUDENT");
        startActivity(i);
        finish();

    }

    public void onFaculty(View view){
        i.putExtra("userType", "FACULTY");
        startActivity(i);
        finish();
    }

    public void onAlumni(View view){
        i.putExtra("userType", "ALUMNI");
        startActivity(i);
        finish();
    }
}
