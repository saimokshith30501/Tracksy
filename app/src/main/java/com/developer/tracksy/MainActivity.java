package com.developer.tracksy;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.developer.tracksy.Models.BranchEventApiModel;
import com.developer.tracksy.Models.DetailReq;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.LinkProperties;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView AppName;
    BranchUniversalObject buo = new BranchUniversalObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Branch.enableLogging();
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(Color.parseColor("#F89E34"));
        AppName = findViewById(R.id.App);
        Animation appName = AnimationUtils.loadAnimation(this, R.anim.animation_app_name);
        AppName.setAnimation(appName);
    }
    private void SplashDelay(Boolean deepLink) {
         new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run(){

                            if (deepLink){
                                Log.d("BRANCH_OBJECT","splash "+ new Gson().toJson(buo));
                                if (buo.getContentMetadata().getCustomMetadata().get("$android_deeplink_path").equals("DetailsActivity")){
//                                    Log.d("BRANCH_DATA", new Gson().toJson(buo.getContentMetadata().getCustomMetadata()));
                                    Intent details = new Intent(MainActivity.this, DetailsActivity.class);
                                    DetailReq detailReq = new DetailReq(buo.getContentMetadata().getCustomMetadata().get("date"),buo.getContentMetadata().getCustomMetadata().get("pincode"),buo.getContentMetadata().getCustomMetadata().get("center_id"));
                                    details.putExtra("centerData", new Gson().toJson(detailReq));
                                    startActivity(details);
                                    finish();
                                }
                            }else {
                                Intent startActivity = new Intent(MainActivity.this, LoginOrSignUp.class);
                                startActivity(startActivity);
                                finish();
                            }
                        }
                    },2000);

    }
    @Override
    public void onStart() {
        super.onStart();
        Branch.getInstance().initSession(branchReferralInitListener, this.getIntent().getData(), this);
        Branch.enableLogging();
    }

    public Branch.BranchUniversalReferralInitListener branchReferralInitListener = new Branch.BranchUniversalReferralInitListener() {
        @Override
        public void onInitFinished(@Nullable BranchUniversalObject branchUniversalObject, @Nullable LinkProperties linkProperties, @Nullable BranchError error) {

            Log.d("BRANCH_OBJECT","ON_INIT_FINISHED"+ new Gson().toJson(branchUniversalObject));
            Log.d("BRANCH_OBJECT","ON_INIT_FINISHED"+ "link "+ new Gson().toJson(linkProperties));
            Log.d("BRANCH_OBJECT","ON_INIT_FINISHED"+"latest "+ new Gson().toJson(Branch.getAutoInstance(getApplicationContext()).getLatestReferringParams()));
                if (branchUniversalObject!=null && branchUniversalObject.getContentMetadata().getCustomMetadata().containsKey("$android_deeplink_path")){
                    buo=branchUniversalObject;
                    SplashDelay(true);

                }else {

                    SplashDelay(false);
                }
        }
    };

    private void callCustomEventApi()  {
        JsonObject payload = new JsonObject();
        BranchEventApiModel userData = new BranchEventApiModel();
            payload.addProperty("branch_key","key_live_jhWT3SoRhVR9f3e1h2ZFpdhdsqlktmTp");
            payload.addProperty("name","Opened_Tracksy");
            userData.os="Android";
            userData.developer_identity="user123";
            userData.aaid="abcdabcd-0123-0123-00f0-000000000000";
            payload.addProperty("user_data", new Gson().toJson(userData));
        API.get().customEvent("https://api.branch.io/v2/event/custom",payload).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                  if (response.isSuccessful()){
                      Log.d("BRANCH_DATA","Custom Event Added");
                  }else {
                      Log.d("BRANCH_DATA", String.valueOf(response.code()));
                  }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("BRANCH_DATA",t.getMessage());
            }
        });
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        this.setIntent(intent);
    }
}
