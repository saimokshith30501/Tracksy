package com.developer.tracksy.Models;

public class ApiResponseModel<T> {
    public boolean success;
    public ResponseMetaData meta;
    public T sessions;
}

