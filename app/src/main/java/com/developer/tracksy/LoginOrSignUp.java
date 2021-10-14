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
    TextView Title;
    TextView subTitile;
    LottieAnimationView vaccines;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_signup);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
//        SignUp=findViewById(R.id.loginButton);
        LogIn=findViewById(R.id.signUpButton);
        vaccines=findViewById(R.id.home_animation);
        App=findViewById(R.id.App);
        Title=findViewById(R.id.subTitle);
        subTitile=findViewById(R.id.Title);

    }
    public void signup(View view){
        Intent startActivity = new Intent(LoginOrSignUp.this, HomeScreen.class);
        Pair[] pair=new Pair[2];
        pair[0]=new Pair<View, String>(Title,"trans2");
        pair[1]=new Pair<View, String>(subTitile,"trans2");
        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(LoginOrSignUp.this,pair);
        startActivity(startActivity,options.toBundle());
    }
    public void onBackPressed(){
        finish();
    }

}
