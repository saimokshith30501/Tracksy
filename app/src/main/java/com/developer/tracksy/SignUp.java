package com.developer.tracksy;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Pair;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class SignUp extends AppCompatActivity {
    TextInputLayout fname,email,phone,password;
    MaterialButton SignUp;
    ImageButton BackButton;
    TextView AppName,SignupTv,SloganTv,BackTologin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        fname=findViewById(R.id.fullnameTv);
        email=findViewById(R.id.emailTv);
        phone=findViewById(R.id.phoneTv);
        password=findViewById(R.id.paswordTv);
        SignUp=findViewById(R.id.signup);
        BackButton=findViewById(R.id.back_log_signup);
        AppName=findViewById(R.id.app_name);
        SignupTv=findViewById(R.id.welcome);
        SloganTv=findViewById(R.id.slogan);
        BackTologin=findViewById(R.id.back_login_tv);
        BackTologin.setText(Html.fromHtml("Already a member? <u>LogIn</u>"));
    }
    public void callSignup(View view){
        if (!vaidateName() | !vaidateEmail() | !vaidatePhone() | !vaidatePassword() ){
            return;
        }

        String regName = fname.getEditText().getText().toString();
        String regEmail = email.getEditText().getText().toString();
        String regPassword = password.getEditText().getText().toString();
        String regPhone = phone.getEditText().getText().toString();
        Intent startActivity = new Intent(SignUp.this,VerifyOTP.class);
        startActivity.putExtra("FNAME",regName);
        startActivity.putExtra("EMAIL",regEmail);
        startActivity.putExtra("PASSWORD",regPassword);
        startActivity.putExtra("PHONE",regPhone);
        Pair[] pair=new Pair[2];
        pair[0]= new Pair<View, String>(BackButton, "backTr");
        pair[1]= new Pair<View, String>(SignUp, "button_trans");
        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pair);
        startActivity(startActivity,options.toBundle());
        finish();
    }
    private Boolean vaidateName() {
        String val = fname.getEditText().getText().toString();
        if (val.isEmpty()) {
            fname.setError("Cannot be empty");
            return false;
        }
        else if (val.length()<5){
            fname.setError("Atleast 5 characters");
            return false;
        }
        else
         {
            fname.setError(null);
             fname.setFocusable(false);
            return true;
        }
    }
    private Boolean vaidateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            email.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean vaidatePhone() {
        String val = phone.getEditText().getText().toString();

        if (val.isEmpty()) {
            phone.setError("Field cannot be empty");
            return false;
        } else if (val.length()<10){
            phone.setError("Enter a valid phone number");
            return false;
        }
        else {
            phone.setError(null);
            phone.setErrorEnabled(false);
            return true;
        }
    }
    private Boolean vaidatePassword() {
        String val = password.getEditText().getText().toString();
        String passwordVal = "^" +
                "(?=.*[a-zA-Z])" +      //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";

        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak (a-z,$,4)");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
    public void callLoginActivity(View view){
        Intent startActivity = new Intent(SignUp.this,LogIn.class);
        Pair[] pair=new Pair[10];
        pair[0]= new Pair<View, String>(AppName, "trans1");
        pair[1]= new Pair<View, String>(SignupTv, "title1");
        pair[2]= new Pair<View, String>(SloganTv, "title2");
        pair[3]= new Pair<View, String>(email, "fields");
        pair[4]= new Pair<View, String>(fname, "fields");
        pair[5]= new Pair<View, String>(phone, "fields");
        pair[6]= new Pair<View, String>(password, "pfields");
        pair[7]= new Pair<View, String>(SignUp, "button_trans");
        pair[8]= new Pair<View, String>(BackTologin, "title3");
        pair[9]= new Pair<View, String>(BackButton, "backTr");
        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pair);
        startActivity(startActivity,options.toBundle());
    }
    public void setBackButton(View view){
        Intent startActivity = new Intent(SignUp.this,LoginOrSignUp.class);
        Pair[] pair=new Pair[2];
        pair[0]=new Pair<View, String>(findViewById(R.id.signup_screen),"trans2");
        pair[1]= new Pair<View, String>(AppName, "trans1");
        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pair);
        startActivity(startActivity,options.toBundle());
    }

}
