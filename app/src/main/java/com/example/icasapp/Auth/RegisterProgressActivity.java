package com.example.icasapp.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;

import com.example.icasapp.Auth.AlumniAuth.AlumniRegisterPagerAdapter;
import com.example.icasapp.Auth.FacultyAuth.FacultyRegisterPagerAdapter;
import com.example.icasapp.Auth.StudentAuth.StudentRegisterPagerAdapter;
import com.example.icasapp.MainActivity;
import com.example.icasapp.R;

public class RegisterProgressActivity extends AppCompatActivity {

    ViewPager pager;
    PagerAdapter pagerAdapter;
    AppCompatButton nextButton;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_progress);

        pager = findViewById(R.id.pager);
        nextButton = findViewById(R.id.nextButton);

        Bundle bundle = getIntent().getExtras();
        final String userType = bundle.getString("userType");

        switch(userType){
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
                break;
        }


        pager.setAdapter(pagerAdapter);


        //DISABLE SWIPES
        pager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return true;
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = pager.getCurrentItem();
                Log.i("msg", userType + "---->" + pager.getAdapter().getCount());

                if (position != 2) {
                    position += 1;
                    pager.setCurrentItem(position);
                } else
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });


    }
}
