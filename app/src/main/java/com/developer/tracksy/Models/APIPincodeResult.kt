package com.developer.tracksy.Models

class APIPincodeResult(
    var name: String,
    var address: String,
    var pincode: String,
    var from: String,
    var to: String,
    var lat: String,
    var long : String,
    var fee_type: String,
    var date: String,
    var min_age_limit: String,
    var vaccine: String,
    var fee: Int,
    var available_capacity: Int,
    var available_capacity_dose1: Int,
    var available_capacity_dose2: Int,
) {
}