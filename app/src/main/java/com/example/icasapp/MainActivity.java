package com.example.icasapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.icasapp.Auth.LoginActivity;
import com.example.icasapp.DeveloperOptions.DeveloperOptions;
import com.example.icasapp.Firebase.FirebaseHelper;
import com.example.icasapp.Menu_EditProfile.EditProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;


public class MainActivity extends AppCompatActivity{

    TabLayout tabLayout;
    DrawerLayout drawerLayout;
    FirebaseFirestore firebaseFirestore;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    FirebaseUser user;
    String TAG = "msg";
    private TextView navName;
    private ImageView navPhoto;
    private NavigationView mFloatingNavigationView;
    //declaring viewPager
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //NOTE: ENTRY POINT OF THE APPLICATION CHANGED TO LOGIN ACTIVITY.
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if (user == null) {
            Log.i("msg", "NO USER");
            FirebaseHelper.signOut();
            setLoginActivity();
            return;
        } else if (!user.isEmailVerified()) {
            new AlertDialog.Builder(MainActivity.this)
                    .setCancelable(false)
                    .setTitle("EMAIL NOT VERIFIED.")
                    .setMessage("Check your mail to verify your registration.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sendEmailForVerification();
                            FirebaseHelper.signOut();
                            setLoginActivity();
                        }
                    })
                    .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            FirebaseHelper.signOut();
                            setLoginActivity();
                        }
                    }).create().show();
        }

        mFloatingNavigationView = findViewById(R.id.nav_view);
        View hView = mFloatingNavigationView.getHeaderView(0);
        navName = (TextView) hView.findViewById(R.id.name);
        navPhoto = hView.findViewById(R.id.navigation_photo);

        Toolbar toolbar = findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.common_open_on_phone, R.string.vector_path_close);
        drawerLayout.addDrawerListener(toggle);
        //toolbar.setNavigationIcon(R.drawable.pivot_white);
        toggle.syncState();


        firebaseFirestore = FirebaseFirestore.getInstance();

        if(user != null) {


            final DocumentReference docRef = db.collection("USER").document(user.getUid());

            docRef
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot documentSnapshot = task.getResult();
                                if (documentSnapshot.exists()) {
                                    Log.i("msg", documentSnapshot.getData().toString());
                                    Log.i("msg", documentSnapshot.get("buffer").toString());

                                    String userType = documentSnapshot.get("userType").toString();
                                    String buffer = documentSnapshot.get("buffer").toString();

                                    GlobalState.globalState.setBuffer(buffer);
                                    GlobalState.globalState.setUserType(userType);
                                    GlobalState.globalState.setName(documentSnapshot.get("name").toString());
                                    if (buffer.equals("4.0"))
                                        mFloatingNavigationView.getMenu().findItem(R.id.nav_developer_options).setVisible(true);

                                    if (documentSnapshot.get("downloadURL") != null)
                                        GlobalState.globalState.setDownloadURL(documentSnapshot.get("downloadURL").toString());

                                    switch (userType) {
                                        case "STUDENT":
                                            Student.student = documentSnapshot.toObject(Student.class);
                                            break;
                                        case "FACULTY":
                                            Faculty.faculty = documentSnapshot.toObject(Faculty.class);
                                            break;
                                        case "ALUMNI":
                                            Alumni.alumni = documentSnapshot.toObject(Alumni.class);
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    });
        }else {
            setLoginActivity();
            return;
        }


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


        // put photo and text in view
        if(user != null) {
            firebaseFirestore.collection("USER").document(user.getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    if (task.isSuccessful() && documentSnapshot.exists() && task.getResult().exists() && !task.getResult().getData().isEmpty()) {
                        String url = "";
                        if ((documentSnapshot.get("downloadURL") != null)) {
                            url = documentSnapshot.get("downloadURL").toString();
                        }
                        String name = documentSnapshot.get("name").toString();
                        Log.i("NAMESS", name);

                        navName.setText(name);
                        Glide.with(getApplicationContext()).load(url).into(navPhoto);
                    }
                }

            });
        }
        else {
            setLoginActivity();
            return;
        }

        mFloatingNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_edit_profile:
                        startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
                        break;
                    case R.id.nav_sign_out:
                        signOut();
                        return true;
                    case R.id.nav_help:
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                        emailIntent.setData(Uri.parse("mailto:icasapp.team@gmail.com"));
                        startActivity(Intent.createChooser(emailIntent, "Send feedback"));
                        break;
                    case R.id.nav_developer_options:
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
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //SECURITY
    @Override
    public void onStart() {
        super.onStart();

        user = mAuth.getCurrentUser();
        // Check if user is signed in (non-null) and update UI accordingly.
        if(user == null){
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
                                FirebaseAuth.getInstance().signOut();
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


    private void sendEmailForVerification() {
        final FirebaseUser user = mAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@androidx.annotation.NonNull Task<Void> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "CHECK your mail : " + user.getEmail(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "EMAIL VERIFICATION FAILED. TRY TO LOGIN AGAIN OR CONTACT DEV TEAM.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}

