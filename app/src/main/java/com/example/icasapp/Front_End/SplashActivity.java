package com.example.icasapp.Front_End;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.icasapp.Auth.LoginActivity;
import com.example.icasapp.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); ImageView imageView;
        TextView tv;

            tv = (TextView) findViewById(R.id.textview);
            imageView= (ImageView) findViewById(R.id.imageview);
            Animation anim = AnimationUtils.loadAnimation(this,R.anim.transition);
            imageView.startAnimation(anim);
            tv.startAnimation(anim);
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
                        finish();
                    }
                }
            }; timer.start();



        }
    }





