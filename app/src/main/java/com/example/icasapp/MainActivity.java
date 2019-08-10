package com.example.icasapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.icasapp.Auth.LoginActivity;
import com.example.icasapp.Feed.FeedFragment;
import com.example.icasapp.Forums.ForumFragment;
import com.example.icasapp.Home.HomeFragment;
import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.Menu_EditProfile.EditProfileActivity;
import com.example.icasapp.Notes.NotesFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

  //  BottomNavigationView bottomNavigationView;
    BottomNavigationView bottomNavigationView;
    private FloatingNavigationView mFloatingNavigationView;

    //This is our viewPager
    private ViewPager viewPager;

    //4 Fragments
    HomeFragment homeFragment;
    NotesFragment notesFragment;
    ForumFragment forumFragment;
    FeedFragment feedFragment;
    RelativeLayout lin_id;

    MenuItem prevMenuItem;

    private FirebaseAuth mAuth;

    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NOTE: ENTRY POINT OF THE APPLICATION CHANGED TO LOGIN ACTIVITY.
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //Initializing viewPager
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        //fab = (VectorMasterView) findViewById(R.id.fab);
        //fab1 = (VectorMasterView) findViewById(R.id.fab1);
        //fab2 = (VectorMasterView) findViewById(R.id.fab2);


        //lin_id = (RelativeLayout)findViewById(R.id.lin_id);
        //bottomNavigationView.setOnNavigationItemSelectedListener(MainActivity.this);



        //Initializing the bottomNavigationView. It changes depending on the button clicked.z

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(

                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.navigation_home:
                                viewPager.setCurrentItem(0);
                                break;

                            case R.id.navigation_notes:
                                viewPager.setCurrentItem(1);
                                break;

                            case R.id.navigation_forum:
                                viewPager.setCurrentItem(2);
                                break;

                            case R.id.navigation_feed:
                                viewPager.setCurrentItem(3);

                                break;
                        }

                        return false;
                    }
                });


        //Listening for right or left swipes

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }

                Log.d("page", "onPageSelected: " + position);

                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }

        });


        setupViewPager(viewPager);
        mFloatingNavigationView = (FloatingNavigationView) findViewById(R.id.floating_navigation_view);
        mFloatingNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingNavigationView.open();

            }
        });
        mFloatingNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                Snackbar.make((View) mFloatingNavigationView.getParent(), item.getTitle() + " Selected!", Snackbar.LENGTH_SHORT).show();
                mFloatingNavigationView.close();
                return true;
            }
        });


    }
    @Override
    public void onBackPressed() {
        if (mFloatingNavigationView.isOpened()) {
            mFloatingNavigationView.close();
        } else {
            super.onBackPressed();
        }
    }


    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        homeFragment = new HomeFragment();
        notesFragment = new NotesFragment();
        forumFragment = new ForumFragment();
        feedFragment = new FeedFragment();

        adapter.addFragment(homeFragment);
        adapter.addFragment(notesFragment);
        adapter.addFragment(forumFragment);
        adapter.addFragment(feedFragment);

        viewPager.setAdapter(adapter);
    }

    //Action Bar Menu Architecture.
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Makes the menu visible
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        switch (item.getItemId()) {
            case R.id.signOut:
                //sign out functionality
                signOut();
                Log.i("Item Selected", "Sign Out");
                return true;

            case R.id.Help:
                //Help Functionality. Reporting Bugs Functionality goes here. Vital for debugging and maintainance.
                return true;

            case R.id.profile:
                //OPEN AN ACTIVITY  OR DIALOG INTERFACE WHICH SHOWS USER PROFILE
                startActivity(new Intent(MainActivity.this, EditProfileActivity.class));
                return true;

            default:
                //In the case where something went wrong.
                return false;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (FirebaseHelper.checkLoginStatus() == false) {
            setLoginActivity();
        }
    }

    public void signOut() { //dialog box sign out
        final AlertDialog.Builder signOutDialog = new AlertDialog.Builder(this);

        this.setTheme(R.style.AlertDialogCustom);
        signOutDialog
                .setIcon(R.drawable.alert)
                .setTitle("ARE YOU SURE?")
                .setMessage("You will be shifted to the login screen and will have to sign in in order to continue using the app.")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseHelper.signOut();
                                setLoginActivity();
                            }
                        }
                )
                .setNegativeButton("NO", null)
                .create()
                .show();


    }


    public void setLoginActivity() {
        finish();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
    }
   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.navigation_home:

                //Animation
                draw(6);
                //findin the correct path
                lin_id.setX(bottomNavigationView.mFirstCurveControlPoint1.x);
                fab.setVisibility(View.VISIBLE);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.GONE);
                drawAnimation(fab);
                break;

            case R.id.navigation_notes:

                draw(2);
                //findin the correct path
                lin_id.setX(bottomNavigationView.mFirstCurveControlPoint1.x);
                fab.setVisibility(View.GONE);
                fab1.setVisibility(View.VISIBLE);
                fab2.setVisibility(View.GONE);
                drawAnimation(fab1);

                break;

            case R.id.navigation_forum:

                draw(6);
                //findin the correct path
                lin_id.setX(bottomNavigationView.mFirstCurveControlPoint1.x);
                fab.setVisibility(View.GONE);
                fab1.setVisibility(View.GONE);
                fab2.setVisibility(View.VISIBLE);
                drawAnimation(fab2);
                break;
        }
        return true;
    }
    private void draw(int i)
    {
        bottomNavigationView.mFirstCurveStartPoint.set((bottomNavigationView.mNavigationBarWidth/i)
        -(bottomNavigationView.CURVE_CIRCLE_RADIUS*2)-(bottomNavigationView.CURVE_CIRCLE_RADIUS/3),0);


        bottomNavigationView.mFirstCurveEndPoint.set((bottomNavigationView.mNavigationBarWidth/i),(bottomNavigationView.CURVE_CIRCLE_RADIUS)
    +(bottomNavigationView.CURVE_CIRCLE_RADIUS/4));


        bottomNavigationView.mSecondCurveStartPoint = bottomNavigationView.mFirstCurveStartPoint;


        bottomNavigationView.mSecondCurveEndPoint.set((bottomNavigationView.mNavigationBarWidth / i)
                + (bottomNavigationView.CURVE_CIRCLE_RADIUS * 2) + (bottomNavigationView.CURVE_CIRCLE_RADIUS / 3),0);

        bottomNavigationView.mFirstCurveControlPoint1.set(bottomNavigationView.mFirstCurveStartPoint.x + bottomNavigationView.CURVE_CIRCLE_RADIUS +
                (bottomNavigationView.CURVE_CIRCLE_RADIUS/4),bottomNavigationView.mFirstCurveStartPoint.y);

        bottomNavigationView.mFirstCurveControlPoint2.set(bottomNavigationView.mFirstCurveEndPoint.x - (bottomNavigationView.CURVE_CIRCLE_RADIUS*2)
                        + bottomNavigationView.CURVE_CIRCLE_RADIUS,bottomNavigationView.mFirstCurveEndPoint.y);

        //second
        bottomNavigationView.mSecondCurveControlPoint1.set(bottomNavigationView.mSecondCurveStartPoint.x + (bottomNavigationView.CURVE_CIRCLE_RADIUS*2)
        -bottomNavigationView.CURVE_CIRCLE_RADIUS,bottomNavigationView.mSecondCurveStartPoint.y);

        bottomNavigationView.mSecondCurveControlPoint2.set(bottomNavigationView.mSecondCurveEndPoint.x -
                (bottomNavigationView.CURVE_CIRCLE_RADIUS +(bottomNavigationView.CURVE_CIRCLE_RADIUS/4)) , bottomNavigationView.mSecondCurveEndPoint.y);

    }
    private void drawAnimation(final VectorMasterView fab)
    {
        outline = fab.getPathModelByName("outline");
        outline.setStrokeColor(Color.WHITE);
        outline.setTrimPathEnd(0.0f);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0.0f,1.0f);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                outline.setTrimPathEnd((Float)animation.getAnimatedValue());
                fab.update();

            }
        });
        valueAnimator.start();
    }*/


}
