package com.developer.tracksy;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.developer.tracksy.Models.OtpApiResponseModel;
import com.developer.tracksy.Utilities.StringToSHA;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.security.NoSuchAlgorithmException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeScreen extends AppCompatActivity {
    private static final String TAG ="VACCINE_TRACKER";
    BottomSheetBehavior bottomSheetBehavior;
    String txnID="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        View view2 = findViewById(R.id.bottom_sheet);
        ImageButton back = view2.findViewById(R.id.backArrow);


//        done.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                StringToSHA sha = new StringToSHA(otpET.getText().toString());
//                try {
//                    sha.getSHA();
//                } catch (NoSuchAlgorithmException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        CardView searchScreen = view2.findViewById(R.id.cv_searchScreen);
        CardView certificateScreen = view2.findViewById(R.id.cv_certificate);
        searchScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SearchActivity.class));
            }
        });
        certificateScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Certificate.class));
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        bottomSheetBehavior = BottomSheetBehavior.from(view2);
        bottomSheetBehavior.setHideable(false);
//        JsonObject js = new JsonObject();
//        js.addProperty("mobile","9481962826");
//        API.get().generateOTP(js).enqueue(new Callback<OtpApiResponseModel<String>>() {
//            @Override
//            public void onResponse(Call<OtpApiResponseModel<String>> call, Response<OtpApiResponseModel<String>> response) {
//                if (response.isSuccessful()){
//                    Log.d("GENERATE",response.body().txnId);
//                    txnID=response.body().txnId;
//                }
//            }
//
//            @Override
//            public void onFailure(Call<OtpApiResponseModel<String>> call, Throwable t) {
//                Log.d("GENERATE",t.getMessage());
//            }
//        });
    }

}