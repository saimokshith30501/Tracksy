package com.developer.tracksy.Models;

import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;

import retrofit2.Response;

public class ErrorHandler {

        public static ApiResponseModel parseError(Response response) {
            ApiResponseModel error;
            Gson gson = new Gson();
            try {
                error = gson.fromJson(response.errorBody().string(), ApiResponseModel.class);
            } catch (Exception e) {
                return null;
            }

            return error;
        }

        public static void handleErrorBody(Response response, View view) {
            ApiResponseModel body = ErrorHandler.parseError(response);
            if (body != null) {
                if (body.meta != null) {
                    Snackbar snackbar = Snackbar.make(view, body.meta.error_message, Snackbar.LENGTH_INDEFINITE)
                            .setAction("OK", v -> {
                            });
                    snackbar.show();
                } else
                    Snackbar.make(view, "Something went wrong", Snackbar.LENGTH_LONG).show();
            } else
                Snackbar.make(view, "Something went wrong", Snackbar.LENGTH_LONG).show();

        }
    }

