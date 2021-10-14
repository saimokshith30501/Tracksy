package com.developer.tracksy;

import androidx.annotation.AnyRes;

import com.developer.tracksy.Models.ApiResponseModel;
import com.developer.tracksy.Models.APIPincodeResult;
import com.developer.tracksy.Models.OtpApiResponseModel;
import com.developer.tracksy.Models.SuccessOtpApiResponseModel;
import com.developer.tracksy.Utilities.ConfirmOTPClass;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface APIService {

    @GET("appointment/sessions/public/findByPin")
    Call<ApiResponseModel<ArrayList<APIPincodeResult>>> checkVaccinesByPincode(
            @Query("pincode") String pincode,
            @Query("date") String date
    );

    @POST("auth/public/generateOTP")
    Call<OtpApiResponseModel<String>> generateOTP(
           @Body JsonObject mobile
    );

    @POST("auth/public/confirmOTP")
    Call<SuccessOtpApiResponseModel<String>> confirmOTP(
            @Body ConfirmOTPClass confirm
    );


}
