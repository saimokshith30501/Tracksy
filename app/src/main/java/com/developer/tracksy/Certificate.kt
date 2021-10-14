package com.developer.tracksy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import com.developer.tracksy.Models.OtpApiResponseModel
import com.developer.tracksy.Models.SuccessOtpApiResponseModel
import com.developer.tracksy.Utilities.ConfirmOTPClass
import com.developer.tracksy.Utilities.StringToSHA
import com.google.android.gms.common.api.Api
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Certificate : AppCompatActivity() {
    private val TAG = "VACCINE_TRACKER"
    var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    lateinit var sendBtn : Button
    lateinit var verifyBtn : Button
    lateinit var back : ImageButton
    lateinit var tilMobile : TextInputLayout
    lateinit var tilOTP : TextInputLayout
    lateinit var llEnterotp : LinearLayout
    lateinit var llEnterMob : LinearLayout
    var txnID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate)
        val view2 = findViewById<View>(R.id.otp_sheet)
        back = view2.findViewById<ImageButton>(R.id.backArrowOtp)
         llEnterotp = view2.findViewById<LinearLayout>(R.id.enter_otp)
        llEnterMob = view2.findViewById<LinearLayout>(R.id.enter_mobile)
        tilMobile = view2.findViewById<TextInputLayout>(R.id.sbs_tilMobNum)
        tilOTP = view2.findViewById<TextInputLayout>(R.id.sbs_tilOtp)
        sendBtn=view2.findViewById(R.id.otp_send)
        verifyBtn=view2.findViewById(R.id.otp_verify)
        back.setOnClickListener {
            finish()
        }
        bottomSheetBehavior = BottomSheetBehavior.from<View>(view2)
        (bottomSheetBehavior as BottomSheetBehavior<View>).isHideable = false
        sendBtn.setOnClickListener {
            tilMobile.isErrorEnabled=false
            if (tilMobile.editText!!.text.isNotEmpty()){
                if (tilMobile.editText!!.text.length==10){
                    sendOtp(tilMobile.editText!!.text.toString())
                }
                else{
                    tilMobile.isErrorEnabled=true
                    tilMobile.error="Please Enter Valid Mobile No"
                }
            }
            else{
                tilMobile.isErrorEnabled=true
                tilMobile.error="Please Enter Mobile No"
            }
        }
        verifyBtn.setOnClickListener {
            tilOTP.isErrorEnabled=false
            if (tilOTP.editText!!.text.isNotEmpty()){
                if (tilOTP.editText!!.text.length==6){
                    verifyOTP(tilOTP.editText!!.text.toString())
                }
                else{
                    tilOTP.isErrorEnabled=true
                    tilOTP.error="Please Enter Valid Mobile No"
                }
            }
            else{
                tilOTP.isErrorEnabled=true
                tilOTP.error="Please Enter Mobile No"
            }
        }
    }

    private fun verifyOTP(otp: String) {
        var sha: StringToSHA = StringToSHA(otp)
      var co:ConfirmOTPClass = ConfirmOTPClass(sha.sha,txnID)
        API.get().confirmOTP(co).enqueue(object : Callback<SuccessOtpApiResponseModel<String>>{
            override fun onFailure(call: Call<SuccessOtpApiResponseModel<String>>, t: Throwable) {
                Snackbar.make(back,t.message.toString(),Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<SuccessOtpApiResponseModel<String>>, response: Response<SuccessOtpApiResponseModel<String>>) {
                if (response.isSuccessful){
                    Log.d("GENERATE","TOKEN: "+response.body()!!.token)
                }
            }
        })

    }

    private fun sendOtp(mob: String) {
        var js = JsonObject();
        js.addProperty("mobile",mob);
           API.get().generateOTP(js).enqueue(object : Callback<OtpApiResponseModel<String>>{
               override fun onFailure(call: Call<OtpApiResponseModel<String>>, t: Throwable) {
                   Snackbar.make(back,t.message.toString(),Snackbar.LENGTH_SHORT).show()
               }

               override fun onResponse(call: Call<OtpApiResponseModel<String>>, response: Response<OtpApiResponseModel<String>>) {
                   if (response.isSuccessful()){
                       Snackbar.make(sendBtn,"Otp Sent",Snackbar.LENGTH_SHORT).show()
                    Log.d("GENERATE",response.body()!!.txnId)
                    txnID=response.body()!!.txnId
                       llEnterMob.visibility=View.GONE
                       llEnterotp.visibility=View.VISIBLE
                }
               }
           })
    }
}