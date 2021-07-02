package com.developer.tracksy;

import com.developer.tracksy.Models.ApiResponseModel;
import com.developer.tracksy.Models.APIPincodeResult;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {

    @GET("findByPin")
    Call<ApiResponseModel<ArrayList<APIPincodeResult>>> checkVaccinesByPincode(
            @Query("pincode") String pincode,
            @Query("date") String date
    );

}
