package com.theenigma.pivot.Auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.theenigma.pivot.Auth.AlumniAuth.AlumniRegisterPagerAdapter;
import com.theenigma.pivot.Auth.FacultyAuth.FacultyRegisterPagerAdapter;
import com.theenigma.pivot.Auth.StudentAuth.StudentRegisterPagerAdapter;
import com.theenigma.pivot.Student;
import com.theenigma.pivot.NonSwipeableViewpager;
import com.theenigma.pivot.R;
import com.shuhart.stepview.StepView;

import java.util.ArrayList;

public class RegisterProgressActivity extends AppCompatActivity {

    public static NonSwipeableViewpager pager;
    PagerAdapter pagerAdapter;
   public static StepView stepView;
   public static int i=0;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_progress);

        pager = findViewById(R.id.pager);
        stepView = findViewById(R.id.step_view);

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
        stepView.getState()
                .animationType(StepView.ANIMATION_ALL)
                // You should specify only stepsNumber or steps array of strings.
                // In case you specify both steps array is chosen.
                .steps(new ArrayList<String>() {{
                    add("First step");
                    add("Second step");
                    add("Third step");
                }})
                // You should specify only steps number or steps array of strings.
                // In case you specify both steps array is chosen.
                .stepsNumber(4)
                .animationDuration(getResources().getInteger(android.R.integer.config_shortAnimTime))
                // other state methods are equal to the corresponding xml attributes
                .commit();


    }

    @Override
    public void onBackPressed() {
        int currentPosition = pager.getCurrentItem();
        if(currentPosition == 0){
            startActivity(new Intent(getApplicationContext(), RegisterLandingActivity.class));
            finish();
        }else{
            pager.setCurrentItem(--currentPosition);
            RegisterProgressActivity.stepView.go(--i,true);
        }

    }
}



