package com.developer.tracksy.Models

class APIPincodeResult(
    var center_id : String,
    var name: String,
    var address: String,
    var pincode: String,
    var from: String,
    var to: String,
    var lat: String,
    var long : String,
    var fee_type: String,
    var date: String,
    var min_age_limit: Int,
    var max_age_limit: Int,
    var vaccine: String,
    var fee: Double,
    var available_capacity: Int,
    var available_capacity_dose1: Int,
    var available_capacity_dose2: Int,
) {
}