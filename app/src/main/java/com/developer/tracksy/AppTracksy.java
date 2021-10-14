package com.developer.tracksy;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AppTracksy extends Application {
    public static String deviceID = "sample";
    private static APIService service;
    @Override
    public void onCreate() {
        super.onCreate();
        setupFCM();
    }
    public static APIService buildApiService() {

        if (service == null) {
            OkHttpClient.Builder builder;
            builder = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Request.Builder ongoing = chain.request().newBuilder();
                        ongoing.addHeader("Accept", "application/json");
                        Response response = chain.proceed(ongoing.build());
                        return response;
                    });
            if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                builder.addInterceptor(interceptor);
            }

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://cdn-api.co-vin.in/api/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(builder.build())
                    .build();
            service = retrofit.create(APIService.class);
        }
        return service;
    }

    private void setupFCM() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w("TRACKSY_LOGS", "Fetching FCM registration token failed", task.getException());
                            return;
                        }
                        deviceID=task.getResult();
                        // Get new FCM registration token
                        Log.d("TRACKSY_LOGS","DEFAULT"+deviceID);
                    }
                });

    }
}
