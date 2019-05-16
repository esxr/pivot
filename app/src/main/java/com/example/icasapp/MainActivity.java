package com.example.icasapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

        BottomNavigationView bottomNavigationView;

        //This is our viewPager
        private ViewPager viewPager;

        //Fragments
        HomeFragment homeFragment;
        NotesFragment notesFragment;
        ForumFragment forumFragment;
        FeedFragment feedFragment;

        MenuItem prevMenuItem;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main);

            //Initializing viewPager
            viewPager = (ViewPager) findViewById(R.id.viewPager);

            //Initializing the bottomNavigationView
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
    }
