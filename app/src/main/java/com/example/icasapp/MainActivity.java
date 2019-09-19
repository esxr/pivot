package com.example.icasapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.bumptech.glide.Glide;
import com.example.icasapp.Auth.LoginActivity;
import com.example.icasapp.DeveloperOptions.DeveloperOptions;
import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.Menu_EditProfile.EditProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;


public class MainActivity extends AppCompatActivity implements InternetConnectivityListener {

    TabLayout tabLayout;
    ImageView navigationImage;
    DrawerLayout drawerLayout;
    TextView navigationText;
    FirebaseFirestore firebaseFirestore;
    FloatingActionButton developerOptions;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String TAG = "msg";
    private NavigationView mFloatingNavigationView;
    //declaring viewPager
    private ViewPager viewPager;
    private InternetAvailabilityChecker mInternetAvailabilityChecker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NOTE: ENTRY POINT OF THE APPLICATION CHANGED TO LOGIN ACTIVITY.
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        InternetAvailabilityChecker.init(this);

        mInternetAvailabilityChecker = InternetAvailabilityChecker.getInstance();
        mInternetAvailabilityChecker.addInternetConnectivityListener(this);
        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.common_open_on_phone,R.string.vector_path_close);
        drawerLayout.addDrawerListener(toggle);
        //toolbar.setNavigationIcon(R.drawable.pivot_white);
        toggle.syncState();

        mFloatingNavigationView = findViewById(R.id.nav_view);


        firebaseFirestore = FirebaseFirestore.getInstance();
        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i("msg",user);
        Log.i("USER_ID",user);

            final DocumentReference docRef = db.collection("USER").document(user);
            docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }
                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());
                        String userType = snapshot.get("userType").toString();
                        String buffer = snapshot.get("buffer").toString();
                        GlobalState.buffer = buffer;
                        GlobalState.userType = userType;
                        if(snapshot.get("downloadURL") != null)
                        GlobalState.downloadURL = snapshot.get("downloadURL").toString();
                        GlobalState.name = snapshot.get("name").toString();

                        Log.i("msg", GlobalState.userType + " ENTERED THE APP");
                        switch (userType) {
                            case "STUDENT":
                                Student.student = snapshot.toObject(Student.class);
                                break;
                            case "FACULTY":
                                Faculty.faculty = snapshot.toObject(Faculty.class);
                                break;
                            case "ALUMNI":
                                Alumni.alumni = snapshot.toObject(Alumni.class);
                                break;
                            default:
                                break;
                        }
                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });


//        developerOptions = findViewById(R.id.developerOptions);
//        developerOptions.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this, DeveloperOptions.class);
//                startActivity(intent);
//                finish();
//            }
//        });
        //Initializing viewPager
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener
                (new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        // Note that system bars will only be "visible" if none of the
                        // LOW_PROFILE, HIDE_NAVIGATION, or FULLSCREEN flags are set.
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            // TODO: The system bars are visible. Make any desired
                            // adjustments to your UI, such as showing the action bar or
                            // other navigational controls.
                            int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
                            decorView.setSystemUiVisibility(uiOptions);
                        } else {
                            // TODO: The system bars are NOT visible. Make any desired
                            // adjustments to your UI, such as hiding the action bar or
                            // other navigational controls.
                        }
                    }
                });


        PagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager);



        mFloatingNavigationView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                Log.i("msg", "CLICKED");

            }
        });

        mFloatingNavigationView.setClickable(true);
        mFloatingNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigationImage = mFloatingNavigationView.getHeaderView(0).findViewById(R.id.navigation_photo);
                navigationText = mFloatingNavigationView.getHeaderView(0).findViewById(R.id.name);

                Log.i("msg", "CLICKED");

                // put photo and text in view
                firebaseFirestore.collection("USER").document(user).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        try {
                            DocumentSnapshot documentSnapshot = task.getResult();

                            String url = documentSnapshot.get("downloadURL").toString();
                            String name = documentSnapshot.get("name").toString();

                            navigationText.setText(name);
                            Glide.with(getApplicationContext()).load(url).into(navigationImage);
                        } catch (Exception c) {

                        }
                    }
                });


            }
        });
        mFloatingNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.nav_notes:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.nav_forums:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.nav_feed:
                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.nav_edit_profile:
                        startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                        break;
                    case R.id.nav_display_profile:
                        return false;
                    case R.id.nav_sign_out:
                        signOut();
                        return true;
                    case R.id.nav_help:
                        Intent intent = new Intent(MainActivity.this, DeveloperOptions.class);
                startActivity(intent);
                finish();
                        break;
                    default:
                        return false;
                }

                Snackbar.make((View) mFloatingNavigationView.getParent(), item.getTitle() + " Selected!", Snackbar.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //SECURITY
    @Override
        public void onStart() {
            super.onStart();
            // Check if user is signed in (non-null) and update UI accordingly.
            if (!FirebaseHelper.checkLoginStatus()) {
                setLoginActivity();
            }
    }

    public void signOut() {
        //dialog box sign out
        final AlertDialog.Builder signOutDialog = new AlertDialog.Builder(this);

        signOutDialog
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

        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();

    }

    @Override
    public void onInternetConnectivityChanged(boolean isConnected) {
        Log.i("msg", "CONNECTION:" + isConnected);
        GlobalState.internetConnection = isConnected;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mInternetAvailabilityChecker
                .removeInternetConnectivityChangeListener(this);
    }
}

