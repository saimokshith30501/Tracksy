package com.developer.tracksy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    TextView AppName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.parseColor("#F89E34"));
        AppName=findViewById(R.id.App);
        Animation appName= AnimationUtils.loadAnimation(this,R.anim.animation_app_name);
        AppName.setAnimation(appName);

    }
    private void CheckUserStatus() {
      FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run(){
                    Intent startActivity = new Intent(MainActivity.this, HomeScreen.class);
                    startActivity(startActivity);
                    finish();
                }
            },2000);
        }
        else {      new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run(){
                            Intent startActivity = new Intent(MainActivity.this, LoginOrSignUp.class);
                            startActivity(startActivity);
                            finish();
                        }
                    },2000);
        }
    }
    @Override
    protected void onStart() {
        CheckUserStatus();
        super.onStart();
    }
}
