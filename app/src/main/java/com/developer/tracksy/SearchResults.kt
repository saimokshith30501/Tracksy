package com.developer.tracksy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.developer.tracksy.Models.APIPincodeResult
import com.developer.tracksy.Models.ApiResponseModel
import com.developer.tracksy.Models.CentersData
import com.developer.tracksy.RVAdapters.PincodeSlotsRVAdapter
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class SearchResults : LoginOrSignUp() {
    lateinit var pincode : String
    lateinit var date : String
    lateinit var day : String
    lateinit var month : String
    lateinit var year : String
    lateinit var dose : String
    lateinit var age : String
    lateinit var rv_centers : RecyclerView
    lateinit var setAlerts : ImageButton
    lateinit var goBack : Button
    lateinit var dismissAlerts : ImageButton
    lateinit var noData : LinearLayout
    lateinit var alertsSuccess : LinearLayout
    lateinit var centersList : ArrayList<CentersData>
    lateinit var loadingDialog: LoadingDialog
    lateinit var adapter: PincodeSlotsRVAdapter
    val nextDaysCalender: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    var db: FirebaseFirestore = FirebaseFirestore.getInstance()
    var mAuth : FirebaseAuth = FirebaseAuth.getInstance()
    var status : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_results)
        pincode=intent.getStringExtra("PINCODE")!!;
        date=intent.getStringExtra("DATE")!!;
        day=intent.getStringExtra("DAY")!!;
        month=intent.getStringExtra("MONTH")!!;
        year=intent.getStringExtra("YEAR")!!;
        dose=intent.getStringExtra("DOSE")!!;
        age=intent.getStringExtra("AGE")!!;
        centersList= ArrayList()
        adapter= PincodeSlotsRVAdapter(centersList, this)
        val back = findViewById<ImageButton>(R.id.sr_backArrow)
        val title = findViewById<TextView>(R.id.sr_tvTitle)
        val nextDay = findViewById<ImageButton>(R.id.sr_next)
        val prevDay = findViewById<ImageButton>(R.id.sr_prev)
        setAlerts = findViewById<ImageButton>(R.id.sr_alerts)
        goBack = findViewById<Button>(R.id.al_goBack)
        dismissAlerts = findViewById<ImageButton>(R.id.sr_dismissAlerts)
        val sub_title = findViewById<TextView>(R.id.sr_tvDate)
        noData = findViewById<LinearLayout>(R.id.sr_llNoData)
        alertsSuccess = findViewById<LinearLayout>(R.id.sr_llAlertsSuccess)
        title.text=pincode
        nextDaysCalender.set(year.toInt(), month.toInt(), day.toInt())
        sub_title.text=day+" "+getMonthForInt(month.toInt() + 1)+" "+year
        loadingDialog = LoadingDialog(this)
        rv_centers = findViewById<RecyclerView>(R.id.sr_rvCenters)
        rv_centers.setHasFixedSize(true)
        rv_centers.layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL,
                false
        )
        back.setOnClickListener { finish() }
        goBack.setOnClickListener {
            startActivity(Intent(this,HomeScreen::class.java))
        }
        nextDay.setOnClickListener {
            loadingDialog.startLoading()
            nextDaysCalender.add(Calendar.DATE, 1)
            var nextDate = nextDaysCalender.get(Calendar.DAY_OF_MONTH).toString()+"-"+(nextDaysCalender.get(Calendar.MONTH)+1).toString()+"-"+nextDaysCalender.get(Calendar.YEAR).toString()
            getSlots(nextDate)
            adapter.notifyDataSetChanged()
            sub_title.text=nextDaysCalender.get(Calendar.DAY_OF_MONTH).toString()+" "+getMonthForInt((nextDaysCalender.get(Calendar.MONTH) + 1))+" "+nextDaysCalender.get(Calendar.YEAR).toString()
        }
        prevDay.setOnClickListener {
            loadingDialog.startLoading()
            nextDaysCalender.add(Calendar.DATE, -1)
            var prevDate = nextDaysCalender.get(Calendar.DAY_OF_MONTH).toString()+"-"+(nextDaysCalender.get(Calendar.MONTH)+1).toString()+"-"+nextDaysCalender.get(Calendar.YEAR).toString()
            getSlots(prevDate)
            adapter.notifyDataSetChanged()
            sub_title.text=nextDaysCalender.get(Calendar.DAY_OF_MONTH).toString()+" "+getMonthForInt((nextDaysCalender.get(Calendar.MONTH) + 1))+" "+nextDaysCalender.get(Calendar.YEAR).toString()
        }

        setAlerts.setOnClickListener {
                openAlertSheet()
        }
        dismissAlerts.setOnClickListener {
            setAlert(false)
            dismissAlerts.visibility=View.GONE
            setAlerts.visibility=View.VISIBLE
        }
        checkAlertsStatus(false)
        loadingDialog.startLoading()
        getSlots(date)
    }

    private fun openAlertSheet() {
        var view = layoutInflater.inflate(R.layout.alerts_bottom_sheet, null)
        val dialog = BottomSheetDialog(this)
        val setAlertsButton = view.findViewById<Button>(R.id.al_setAlerts)

        val close = view.findViewById<ImageButton>(R.id.al_close)
        close.setOnClickListener {
            dialog.dismiss()
        }
        setAlertsButton.setOnClickListener {
            checkAlertsStatus(true)
            dialog.dismiss()

        }
        dialog.setContentView(view)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        dialog.show()
    }

    private fun checkAlertsStatus(managingAlerts : Boolean) {
        db.collection("subscriptions").document(mAuth.currentUser!!.uid).collection("pincodes").document(pincode)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        if (!managingAlerts){
                            if(documentSnapshot.get("is_subscribed").toString().equals("true")){
                                   dismissAlerts.visibility=View.VISIBLE
                                   setAlerts.visibility=View.GONE
                            }
                            else{
                                dismissAlerts.visibility=View.GONE
                                setAlerts.visibility=View.VISIBLE
                            }
                        }
                        else{
                            setAlert(true)
                        }

                    } else {
                        if (!managingAlerts){
                            dismissAlerts.visibility=View.GONE
                            setAlerts.visibility=View.VISIBLE
                        }
                        else{
                            setAlert(true)
                        }
                    }
                }
                .addOnFailureListener { Log.d("TRACKSY_EXISTS", " FAILED") }

    }

    fun setAlert(status: Boolean){
        val hashMap = HashMap<String, Any>()
        hashMap["pincode"] = pincode
        hashMap["dose"] = dose
        hashMap["is_subscribed"] = status
        hashMap["deviceID"] = AppTracksy.deviceID
        db.collection("subscriptions").document(mAuth.currentUser!!.uid).collection("pincodes").document(pincode)
                .set(hashMap)
                .addOnSuccessListener {
                    Log.d("TRACKSY_LOGS", "DocumentSnapshot successfully written!")
                }
                .addOnFailureListener { e -> Log.d("TRACKSY_LOGS", "Error writing document", e) }
        if (status){
            dismissAlerts.visibility=View.VISIBLE
            setAlerts.visibility=View.GONE
            noData.visibility = View.GONE
            rv_centers.visibility = View.GONE
            alertsSuccess.visibility=View.VISIBLE
        }
        else{
            dismissAlerts.visibility=View.VISIBLE
            setAlerts.visibility=View.GONE
        }
    }

    fun getSlots(date: String) {
        API.get().checkVaccinesByPincode(pincode, date).enqueue(object : Callback<ApiResponseModel<ArrayList<APIPincodeResult>>> {
            override fun onResponse(call: Call<ApiResponseModel<ArrayList<APIPincodeResult>>>, response: Response<ApiResponseModel<ArrayList<APIPincodeResult>>>) {
                loadingDialog.dismissDialog()
                if (response.isSuccessful) {
                    if (response.body()!!.sessions!!.size != 0) {
                        rv_centers.visibility = View.VISIBLE
                        noData.visibility = View.GONE
                        centersList = ArrayList()
                        getCenters(response.body()!!.sessions!!)
                        var gson = Gson()
                        var s = gson.toJson(centersList)
                        Log.d("TRACKSY_LOGS", gson.toJson(centersList))
                    } else {
                        noData.visibility = View.VISIBLE
                        rv_centers.visibility = View.GONE
                    }

                } else {
                    Snackbar.make(rv_centers, "Something went wrong", Snackbar.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<ApiResponseModel<ArrayList<APIPincodeResult>>>, t: Throwable) {
                loadingDialog.dismissDialog()
                Snackbar.make(rv_centers, "Request failed", Snackbar.LENGTH_LONG).show()
            }
        })
    }

    private fun getCenters(apiSessions: ArrayList<APIPincodeResult>) {
         for (i in 0..apiSessions.lastIndex){
             if (apiSessions[i].min_age_limit==age){
                 var paid=false
                 if (apiSessions[i].fee_type=="Paid"){
                     paid=true
                 }
                 else
                     paid=false
                 if (dose.toInt()==1 && apiSessions[i].available_capacity_dose1>0){

                     var data : CentersData = CentersData(apiSessions[i].name, apiSessions[i].address, apiSessions[i].pincode, apiSessions[i].from, apiSessions[i].to, apiSessions[i].lat, apiSessions[i].long, paid, apiSessions[i].vaccine, "Dose 1", apiSessions[i].fee, apiSessions[i].available_capacity_dose1)
                     centersList.add(data)
                 }
                 else if (dose.toInt()==2 && apiSessions[i].available_capacity_dose2>0){
                     var data : CentersData = CentersData(apiSessions[i].name, apiSessions[i].address, apiSessions[i].pincode, apiSessions[i].from, apiSessions[i].to, apiSessions[i].lat, apiSessions[i].long, paid, apiSessions[i].vaccine, "Dose 2", apiSessions[i].fee, apiSessions[i].available_capacity_dose2)
                     centersList.add(data)
                 }
                 else{
                     var data : CentersData = CentersData(apiSessions[i].name, apiSessions[i].address, apiSessions[i].pincode, apiSessions[i].from, apiSessions[i].to, apiSessions[i].lat, apiSessions[i].long, paid, apiSessions[i].vaccine, "", apiSessions[i].fee, apiSessions[i].available_capacity)
                     centersList.add(data)
                 }
             }
         }
        loadingDialog.dismissDialog()
        if (centersList.size!=0) {
            rv_centers.visibility=View.VISIBLE
            noData.visibility=View.GONE
            adapter=PincodeSlotsRVAdapter(centersList, applicationContext)
            rv_centers.adapter = adapter
        }
        else{
            noData.visibility=View.VISIBLE
            rv_centers.visibility=View.GONE
        }
    }
    fun getMonthForInt(num: Int): String {
        val monthNames = arrayOf(
                "Jan",
                "Feb",
                "Mar",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec"
        )
        return monthNames[num - 1]
    }


}