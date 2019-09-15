package com.example.icasapp.Front_End;

import android.app.ActivityOptions;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.icasapp.Auth.LoginActivity;
import com.example.icasapp.Auth.RegisterActivity;
import com.example.icasapp.R;

public class SplashActivity extends AppCompatActivity {
    private ConstraintLayout SplashScreen;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ImageView imageView;
        TextView tv;




            tv = (TextView) findViewById(R.id.textview);
            imageView= (ImageView) findViewById(R.id.imageview);
            Animation anim = AnimationUtils.loadAnimation(this,R.anim.transition);
            imageView.startAnimation(anim);
            tv.startAnimation(anim);
        Intent sharedIntent = new Intent(SplashActivity.this, LoginActivity.class);


        final Pair[] pairs = new Pair[2];
        pairs[0] = new Pair<View, String>(imageView, "pivot");
        pairs[1] = new Pair<View, String>(tv, "pivot text");


        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this, pairs);
        startActivity(sharedIntent, options.toBundle());

        final Intent i = new Intent(this, LoginActivity.class);
            Thread timer = new Thread(){
                public void run() {
                    try{
                        sleep(5000);
                    } catch (InterruptedException e){
                        e.printStackTrace();
                    }
                    finally {
                        startActivity(i);
                        ActivityCompat.finishAfterTransition(SplashActivity.this);
                    }
                }
            }; timer.start();



        }




    }






