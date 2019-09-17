package com.example.icasapp.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.viewpager.widget.PagerAdapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.example.icasapp.Auth.AlumniAuth.AlumniRegisterPagerAdapter;
import com.example.icasapp.Auth.FacultyAuth.FacultyRegisterPagerAdapter;
import com.example.icasapp.Auth.StudentAuth.StudentRegisterPagerAdapter;
import com.example.icasapp.Student;
import com.example.icasapp.NonSwipeableViewpager;
import com.example.icasapp.R;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;

public class RegisterProgressActivity extends AppCompatActivity {

    public static NonSwipeableViewpager pager;
    PagerAdapter pagerAdapter;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_progress);

        pager = findViewById(R.id.pager);

        final Bundle bundle = getIntent().getExtras();
        final String userType = bundle.getString("userType");
        Student.student.setUserType(userType);
        switch (userType) {
            case "STUDENT":
                pagerAdapter = new StudentRegisterPagerAdapter(getSupportFragmentManager());

                break;
            case "FACULTY":
                pagerAdapter = new FacultyRegisterPagerAdapter(getSupportFragmentManager());
                /*PH: PRIVATE , OFFICE ; SUBJECT(s) ; CREDENTIALS ; CABIN LOCATION AND BUILDING ; WORK/ PERSONAL MAIL  */
                break;
            case "ALUMNI":
                pagerAdapter = new AlumniRegisterPagerAdapter(getSupportFragmentManager());
                break;
            default:
                startActivity(new Intent(getApplicationContext(), RegisterLandingActivity.class));
                finish();
                break;
        }


        pager.setAdapter(pagerAdapter);


    }

    @Override
    public void onBackPressed() {
        int currentPosition = pager.getCurrentItem();
        if(currentPosition == 0){
            startActivity(new Intent(getApplicationContext(), RegisterLandingActivity.class));
            finish();
        }else{
            pager.setCurrentItem(--currentPosition);
        }

    }
}



