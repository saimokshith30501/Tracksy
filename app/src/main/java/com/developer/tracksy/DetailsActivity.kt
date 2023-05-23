package com.developer.tracksy

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.developer.tracksy.Models.APIPincodeResult
import com.developer.tracksy.Models.ApiResponseModel
import com.developer.tracksy.Models.CentersData
import com.developer.tracksy.Models.DetailReq
import com.developer.tracksy.databinding.ActivityDetailsBinding
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import io.branch.indexing.BranchUniversalObject
import io.branch.referral.Branch.BranchLinkCreateListener
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchEvent
import io.branch.referral.util.ContentMetadata
import io.branch.referral.util.LinkProperties
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class DetailsActivity : AppCompatActivity() {
   lateinit var dataReq: DetailReq
    lateinit var data : CentersData
    lateinit var loadingDialog: LoadingDialog
    lateinit var binding: ActivityDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadingDialog = LoadingDialog(this)
        dataReq=Gson().fromJson(intent.extras!!.getString("centerData",null),DetailReq::class.java)

        getSlots()
        binding.srShare.setOnClickListener {
            openSendIntent()
           BranchEvent(BRANCH_STANDARD_EVENT.SHARE)
               .setDescription(data.name)
               .addCustomDataProperty("Center_ID",data.center_id)
               .logEvent(this)
        }
    }

    private fun openSendIntent() {
        var myBranchObj= BranchUniversalObject()
        myBranchObj = BranchUniversalObject()
            .setContentIndexingMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
            .setLocalIndexMode(BranchUniversalObject.CONTENT_INDEX_MODE.PUBLIC)
        val lp = LinkProperties()
            .setChannel("CoWin")
            .setFeature("sharing")
            .setCampaign("Sharing vaccination centers")
            .setStage("Center Details")
            .addControlParameter("\$android_deeplink_path","DetailsActivity")
            .addControlParameter("\$og_title","Gemüseauflauf mit veganem Pfannenkäse")
            .addControlParameter("\$desktop_url","https://website-stage.sevencooks.com/de/rezept/17624")
            .addControlParameter("date", data.date)
            .addControlParameter("pincode",data.pincode)
            .addControlParameter("center_id",data.center_id)
        lp.alias="ikik"

        myBranchObj.generateShortUrl(this, lp
        ) { url, error ->
            if (error == null) {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, url)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, "Check out this vaccination centre!")
                startActivity(shareIntent)
                Log.d("BRANCH_DATA", "got my Branch link to share: $url")
            }
        }
    }

    fun getSlots() {
        loadingDialog.startLoading()
        API.get().checkVaccinesByPincode(dataReq.pincode, dataReq.date).enqueue(object :
            Callback<ApiResponseModel<ArrayList<APIPincodeResult>>> {
            override fun onResponse(call: Call<ApiResponseModel<ArrayList<APIPincodeResult>>>, response: Response<ApiResponseModel<ArrayList<APIPincodeResult>>>) {
                if (response.isSuccessful) {
                    if (response.body()!!.sessions!!.size != 0) {
                        getCenters(response.body()!!.sessions!!)
                    } else {
                        loadingDialog.dismissDialog()
                        Snackbar.make(binding.srTvTitle, "Something went wrong", Snackbar.LENGTH_LONG).show()
                    }

                } else {
                    loadingDialog.dismissDialog()
                    Snackbar.make(binding.srTvTitle, "Something went wrong", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponseModel<ArrayList<APIPincodeResult>>>, t: Throwable) {
                loadingDialog.dismissDialog()
                Snackbar.make(binding.srTvTitle, "Request failed", Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun getCenters(apiSessions: ArrayList<APIPincodeResult>) {
        for (i in 0..apiSessions.lastIndex){
            if (apiSessions[i].center_id==dataReq.center_id){
                var paid: Boolean = apiSessions[i].fee_type=="Paid"
                data = CentersData(apiSessions[i].center_id,apiSessions[i].name, apiSessions[i].address, apiSessions[i].pincode,apiSessions[i].date, apiSessions[i].from, apiSessions[i].to, apiSessions[i].lat, apiSessions[i].long, paid, apiSessions[i].vaccine, "", apiSessions[i].fee.toInt(),apiSessions[i].min_age_limit,apiSessions[i].max_age_limit, apiSessions[i].available_capacity)
            }
        }
        setData()
        Log.d("BRANCH_DATA",Gson().toJson(data))
    }

    private fun setData(){
        binding.tvCenterName.text=data.name
        binding.tvDose.text=data.Dose
        binding.tvVaccine.text=data.vaccine
        binding.tvFrom.text="From : "+data.from
        binding.tvTo.text="To : "+data.to
        binding.tvNoOfDoses.text=data.available_capacity.toString()
        binding.tvMinAge.text=data.min_age_limit.toString()
        if (data.max_age_limit!=null && data.max_age_limit!=0){
            binding.tvMaxAge.text=data.max_age_limit.toString()
            binding.ivAll.visibility=View.GONE
            binding.tvMaxAge.visibility=View.VISIBLE
        }else{
            binding.ivAll.visibility=View.VISIBLE
            binding.tvMaxAge.visibility=View.GONE
        }
        binding.ivDirections.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:"+data.lat+","+data.long+"?q="+ Uri.encode(data.name+" "+data.address))
            Log.d("MAPS_URI",gmmIntentUri.toString());
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            try {
                startActivity(mapIntent)
            }
            catch (e : Exception){

            }
        }
        if (data.available_capacity<=5){
            binding.llMain.background=this.getDrawable(R.drawable.very_low_background)
            binding.llDoses.background=this.getDrawable(R.drawable.very_low_background)
            binding.rlMain.setBackgroundColor(Color.parseColor("#FC4848"))
            binding.tvNoOfDoses.setTextColor(Color.parseColor("#FC4848"))
            if (data.paid){
                binding.ivFee.setImageResource(R.drawable.ic_paid)
                binding.llRupee.visibility=View.VISIBLE
                binding.llRupee.background=this.getDrawable(R.drawable.very_low_background)
                binding.tvFee.text=data.fee.toString()
                binding.tvFee.setTextColor(Color.parseColor("#FC4848"))
                binding.ivRupee.setColorFilter(ContextCompat.getColor(this, R.color.verLow))
            }
            else{
                binding.ivFee.setImageResource(R.drawable.ic_free)
                binding.llRupee.visibility=View.GONE
            }
        }
        else if(data.available_capacity>5 && data.available_capacity<=10 ){
            binding.llMain.background=this.getDrawable(R.drawable.low_background)
            binding.llDoses.background=this.getDrawable(R.drawable.low_background)
            binding.rlMain.setBackgroundColor(Color.parseColor("#FF9356"))
            binding.tvNoOfDoses.setTextColor(Color.parseColor("#FF9356"))
            if (data.paid){
                binding.ivFee.setImageResource(R.drawable.ic_paid)
                binding.llRupee.visibility=View.VISIBLE
                binding.llRupee.background=this.getDrawable(R.drawable.low_background)
                binding.tvFee.text=data.fee.toString()
                binding.tvFee.setTextColor(Color.parseColor("#FF9356"))
                binding.ivRupee.setColorFilter(ContextCompat.getColor(this, R.color.low))
            }
            else{
                binding.ivFee.setImageResource(R.drawable.ic_free)
                binding.llRupee.visibility=View.GONE
            }
        }
        else{
            binding.llMain.background=this.getDrawable(R.drawable.available_background)
            binding.llDoses.background=this.getDrawable(R.drawable.available_background)
            binding.rlMain.setBackgroundColor(Color.parseColor("#69CD60"))
            binding.tvNoOfDoses.setTextColor(Color.parseColor("#69CD60"))
            if (data.paid){
                binding.ivFee.setImageResource(R.drawable.ic_paid)
                binding.llRupee.visibility=View.VISIBLE
                binding.llRupee.background=this.getDrawable(R.drawable.available_background)
                binding.tvFee.text=data.fee.toString()
                binding.tvFee.setTextColor(Color.parseColor("#69CD60"))
                binding.ivRupee.setColorFilter(ContextCompat.getColor(this, R.color.available))
            }
            else{
                binding.ivFee.setImageResource(R.drawable.ic_free)
                binding.llRupee.visibility=View.GONE
            }
        }
        loadingDialog.dismissDialog()
    }


}