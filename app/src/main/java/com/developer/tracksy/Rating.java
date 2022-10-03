package com.developer.tracksy;

public class Rating {
    public int center_id;
    public String rating;
    public String given;

    public Rating(int center_id, String rating,String given) {
        this.center_id = center_id;
        this.rating = rating;
        this.given = given;
    }

    public String isGiven() {
        return given;
    }

    public void setGiven(String given) {
        this.given = given;
    }

    public int getCenter_id() {
        return center_id;
    }

    public void setCenter_id(int center_id) {
        this.center_id = center_id;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
