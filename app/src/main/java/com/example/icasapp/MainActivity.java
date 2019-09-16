package com.example.icasapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.treebo.internetavailabilitychecker.InternetAvailabilityChecker;
import com.treebo.internetavailabilitychecker.InternetConnectivityListener;


public class MainActivity extends AppCompatActivity implements InternetConnectivityListener {

    TabLayout tabLayout;
    ImageView navigationImage;
    TextView navigationText;
    FirebaseFirestore firebaseFirestore;
    FloatingActionButton developerOptions;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String TAG = "msg";
    private FloatingNavigationView mFloatingNavigationView;
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

        firebaseFirestore = FirebaseFirestore.getInstance();
        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.i("msg", user);


        db
                .collection("USER")
                .document(user)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                Log.d(TAG, document.get("userType").toString());
                                String userType = document.get("userType").toString();
                                String buffer = document.get("buffer").toString();
                                GlobalState.buffer = buffer;
                                GlobalState.userType = userType;
                                switch (userType){
                                    case "STUDENT":
                                        Student.student = document.toObject(Student.class);
                                        break;
                                    case "FACULTY":
                                        Faculty.faculty = document.toObject(Faculty.class);
                                        break;
                                    case "ALUMNI":
                                        Alumni.alumni = document.toObject(Alumni.class);
                                        break;
                                    default:
                                        break;
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        }else{

                        }
                    }
                });



        developerOptions = findViewById(R.id.developerOptions);
        developerOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DeveloperOptions.class);
                startActivity(intent);
                finish();
            }
        });
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


        mFloatingNavigationView = findViewById(R.id.floating_navigation_view);
        mFloatingNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFloatingNavigationView.open();
                navigationImage = mFloatingNavigationView.getHeaderView(0).findViewById(R.id.navigation_photo);
                navigationText = mFloatingNavigationView.getHeaderView(0).findViewById(R.id.name);

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
                        break;
                    default:
                        return false;
                }

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

