package com.example.icasapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

        BottomNavigationView bottomNavigationView;

        //This is our viewPager
        private ViewPager viewPager;

        //4 Fragments
        HomeFragment homeFragment;
        NotesFragment notesFragment;
        ForumFragment forumFragment;
        FeedFragment feedFragment;

        MenuItem prevMenuItem;

        private FirebaseAuth mAuth;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//NOTE: ENTRY POINT OF THE APPLICATION CHANGED TO LOGIN ACTIVITY.
            setContentView(R.layout.activity_main);

            //Initializing viewPager
            viewPager = (ViewPager) findViewById(R.id.viewPager);

            //Initializing the bottomNavigationView. It changes depending on the button clicked.

            bottomNavigationView = (BottomNavigationView)findViewById(R.id.navigation);
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
                        }});


            //Listening for right or left swipes

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

                @Override
                public void onPageSelected(int position) {
                    if (prevMenuItem != null) {
                        prevMenuItem.setChecked(false);
                    }
                    else{
                        bottomNavigationView.getMenu().getItem(0).setChecked(false);
                    }

                    Log.d("page", "onPageSelected: "+position);

                    bottomNavigationView.getMenu().getItem(position).setChecked(true);
                    prevMenuItem = bottomNavigationView.getMenu().getItem(position);

                }

                @Override
                public void onPageScrollStateChanged(int state) {}

            });


            setupViewPager(viewPager);

        }

        private void setupViewPager(ViewPager viewPager) {

            ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

            homeFragment         =new HomeFragment();
            notesFragment        =new NotesFragment();
            forumFragment        =new ForumFragment();
            feedFragment         =new FeedFragment();



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
            menuInflater.inflate(R.menu.action_menu,menu);

            return super.onCreateOptionsMenu(menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            super.onOptionsItemSelected(item);

            switch (item.getItemId()){
                case R.id.signOut:
                    //sign out functionality
                    signOut();
                    Log.i("Item Selected" , "Sign Out");
                    return true;

                case R.id.Help:
                    //Help Functionality. Reporting Bugs Functionality goes here. Vital for debugging and maintainance.
                    return true;

                case R.id.profile:
                    //OPEN AN ACTIVITY  OR DIALOG INTERFACE WHICH SHOWS USER PROFILE
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
        if(FirebaseHelper.checkLoginStatus() == false){
             setLoginActivity();
        }
    }

    public void signOut(){
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("ARE YOU SURE?")
                .setMessage("You will be shifted to the login screen and will have to sign in in order to continue using the app.")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseHelper.signOut();
                        setLoginActivity();
                    }
                })
                .setNegativeButton("NO" , null)
                .show();

    }

    public void setLoginActivity(){
            finish();
            startActivity(new Intent(getApplicationContext() , LoginActivity.class));
    }
}
