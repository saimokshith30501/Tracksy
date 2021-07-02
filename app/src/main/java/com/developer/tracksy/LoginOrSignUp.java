package com.developer.tracksy;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class LoginOrSignUp extends AppCompatActivity {
    Button SignUp;
    Button LogIn;
    TextView App;
    LottieAnimationView vaccines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_signup);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        SignUp=findViewById(R.id.loginButton);
        LogIn=findViewById(R.id.signUpButton);
        vaccines=findViewById(R.id.home_animation);
        App=findViewById(R.id.App);

    }
    public void login(View view){
        Intent startActivity = new Intent(LoginOrSignUp.this, LogIn.class);
        Pair[] pair1=new Pair[1];
        pair1[0]=new Pair<View, String>(LogIn,"trans1");
        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(LoginOrSignUp.this,pair1);
        startActivity(startActivity,options.toBundle());
    }
    public void signup(View view){
        Intent startActivity = new Intent(LoginOrSignUp.this, SignUp.class);
        Pair[] pair=new Pair[1];
        pair[0]=new Pair<View, String>(SignUp,"trans2");
        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(LoginOrSignUp.this,pair);
        startActivity(startActivity,options.toBundle());
    }
    public void onBackPressed(){
        finish();
    }

}
