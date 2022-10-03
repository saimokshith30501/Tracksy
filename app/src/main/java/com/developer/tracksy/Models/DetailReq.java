package com.developer.tracksy.Models;

public class DetailReq {
    public String date;
    public String pincode;
    public String center_id;

    public DetailReq(String date, String pincode, String center_id) {
        this.date = date;
        this.pincode = pincode;
        this.center_id = center_id;
    }
}
