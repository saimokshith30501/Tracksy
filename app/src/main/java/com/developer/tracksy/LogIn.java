package com.developer.tracksy;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class LogIn extends AppCompatActivity {
    TextInputLayout email,password;
    MaterialButton LogIn;
    ImageButton BackButton;
    TextView BackToSignUp;
    TextView AppName,LoginTv,SloganTv;
    ProgressDialog progressDialog,progressDialog2;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        LogIn=findViewById(R.id.log_loginBt);
        BackToSignUp=findViewById(R.id.log_backToSign);
        mAuth= FirebaseAuth.getInstance();
        AppName=findViewById(R.id.app_name);
        LoginTv=findViewById(R.id.welcome);
        SloganTv=findViewById(R.id.slogan);
        AppName.setGravity(Gravity.CENTER_HORIZONTAL);
        email=findViewById(R.id.log_emailTv);
        password=findViewById(R.id.log_paswordTv);
        BackButton=findViewById(R.id.back_log_login);
        db=FirebaseFirestore.getInstance();
        progressDialog=new ProgressDialog(com.developer.tracksy.LogIn.this);
        progressDialog.setMessage("Authenticating");
        progressDialog2=new ProgressDialog(com.developer.tracksy.LogIn.this);
        progressDialog2.setMessage("Sending");

        BackToSignUp.setText(Html.fromHtml("New User? <u>SignUp</u>"));
    }
    public void setBackButton(View view){
        Intent startActivity = new Intent(LogIn.this,LoginOrSignUp.class);
        Pair[] pair=new Pair[2];
        pair[0]=new Pair<View, String>(findViewById(R.id.login_screen),"trans2");
        pair[1]= new Pair<View, String>(AppName, "trans1");
        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(LogIn.this,pair);
        startActivity(startActivity,options.toBundle());
    }
    public void callSignUpActivity(View view){
        Intent startActivity = new Intent(LogIn.this,SignUp.class);
        Pair[] pair=new Pair[8];
        pair[0]= new Pair<View, String>(AppName, "trans1");
        pair[1]= new Pair<View, String>(LoginTv, "title1");
        pair[2]= new Pair<View, String>(SloganTv, "title2");
        pair[3]= new Pair<View, String>(email, "fields");
        pair[4]= new Pair<View, String>(password, "pfields");
        pair[5]= new Pair<View, String>(LogIn, "button_trans");
        pair[6]= new Pair<View, String>(BackToSignUp, "title3");
        pair[7]= new Pair<View, String>(BackButton, "backTr");
        ActivityOptions options= ActivityOptions.makeSceneTransitionAnimation(LogIn.this,pair);
        startActivity(startActivity,options.toBundle());
    }
    public void recoverPass(final View view){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailEntered=new EditText(this);
        emailEntered.setHint("Enter Registered Email");
        emailEntered.setMinEms(15);
        linearLayout.addView(emailEntered);
//        linearLayout.setBackgroundColor(Color.parseColor("#A9C4DF"));
        linearLayout.setPadding(30,10,30,10);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        builder.setTitle("Recover Password").setPositiveButton("Send Link", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailEntered.getText().toString().trim();
                if (vaidateEmailforRecovery(email)) {
//                    beginRecovery(email);
                }else {
                    Snackbar.make(view, "Enter a valid email", Snackbar.LENGTH_SHORT).show();
                }
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setView(linearLayout);
        builder.setIcon(R.drawable.ic_rec);
        builder.create().show();
    }
    public void callLogin(View view) {
        if (!vaidateEmail() | !validatePassword()) {
            return;
        }
        else {
//            startActivity(new Intent(LogIn.this, HomeScreen.class));
            progressDialog.show();
            isuser();
        }
    }
    private void isuser() {
        final String userEntered=email.getEditText().getText().toString().trim();
        final String passEntered=password.getEditText().getText().toString().trim();
        mAuth.signInWithEmailAndPassword(userEntered,passEntered)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressDialog.dismiss();
                            upDateDeviceToken();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(LogIn.this, "Wrong Credentials", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LogIn.this, "Authentication failed."+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private Boolean vaidateEmailforRecovery(String val) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        if (val.isEmpty()) {
            return false;
        } else if (!val.matches(emailPattern)) {
            return false;
        } else {
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
            email.setError("Enter a valid email");
            return false;
        } else {
            return true;
        }
    }
    private Boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        if (val.isEmpty()) {
            password.setError("Field cannot be empty");
            password.setFocusable(true);
            return false;
        } else if (val.length()<6){
            password.setError("Password length too short");
            password.setFocusable(true);
            return false;
        }else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }
    private void beginRecovery(String email) {
        progressDialog2.show();
        final int[] s = new int[1];
        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog2.dismiss();
                    Toast.makeText(getApplicationContext(),"Reset Link has been sent successfully", Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog2.dismiss();
//                     Snackbar.make(findViewById(R.id.viewSnack),task.getException().getMessage(),Snackbar.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Failed "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog2.dismiss();
                Toast.makeText(getApplicationContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void upDateDeviceToken(){
        FirebaseUser user=mAuth.getCurrentUser();
        String uid=user.getUid();
        DocumentReference ref = db.collection("users").document(uid);
        ref.update("deviceID",AppTracksy.deviceID)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        startActivity(new Intent(LogIn.this, HomeScreen.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(LogIn,"Something Went Wrong",Snackbar.LENGTH_SHORT).show();
                    }
                });


    }
}
