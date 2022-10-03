package com.developer.tracksy.RVAdapters

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.opengl.Visibility
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.developer.tracksy.DatabaseHandler
import com.developer.tracksy.DetailsActivity
import com.developer.tracksy.Models.APIPincodeResult
import com.developer.tracksy.Models.CentersData
import com.developer.tracksy.Models.DetailReq
import com.developer.tracksy.R
import com.developer.tracksy.Rating
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.gson.Gson
import io.branch.referral.util.BRANCH_STANDARD_EVENT
import io.branch.referral.util.BranchEvent
import java.lang.Exception

class PincodeSlotsRVAdapter(var all_centers: ArrayList<CentersData>,var context: Context) : RecyclerView.Adapter<PincodeSlotsRVAdapter.ItemsViewHolder>() {
    class ItemsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tv_CenterName: TextView
        internal var tv_DoseName: TextView
        internal var tv_NoOfDoses: TextView
        internal var tv_Vaccine: TextView
        internal var iv_fee: ImageView
        internal var iv_vaccine: ImageView
        internal var iv_directions: ImageView
        internal var ll_main: LinearLayout
        internal var ll_doses: LinearLayout
        internal var rl_main: RelativeLayout

        init {
            tv_CenterName = itemView.findViewById<TextView>(R.id.ci_tvName)
            tv_DoseName = itemView.findViewById<TextView>(R.id.ci_tvDose)
            tv_NoOfDoses = itemView.findViewById<TextView>(R.id.ci_tvNoOfDoses)
            tv_Vaccine = itemView.findViewById<TextView>(R.id.ci_tvVaccine)
            iv_fee = itemView.findViewById<ImageView>(R.id.ci_ivFeeTop)
            iv_vaccine = itemView.findViewById<ImageView>(R.id.ci_ivVaccine)
            iv_directions = itemView.findViewById<ImageView>(R.id.ci_ivDirections)
            ll_main = itemView.findViewById<LinearLayout>(R.id.ci_llMain)
            ll_doses = itemView.findViewById<LinearLayout>(R.id.ci_llDoses)
            rl_main = itemView.findViewById<RelativeLayout>(R.id.ci_rlMain)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsViewHolder {
        val view = LayoutInflater.from(parent?.context)
                .inflate(R.layout.centers_item, parent, false)
        return ItemsViewHolder(view)
    }

    open fun clearDATA(){
        this.all_centers= ArrayList()
    }

    override fun onBindViewHolder(holder: ItemsViewHolder, position: Int) {
         val data = all_centers.get(position)
         holder.tv_CenterName.text=data.name
         holder.tv_DoseName.text=data.Dose
        holder.tv_Vaccine.text=data.vaccine
        holder.tv_NoOfDoses.text=data.available_capacity.toString()
        holder.iv_directions.setOnClickListener {
            val gmmIntentUri = Uri.parse("geo:"+data.lat+","+data.long+"?q="+Uri.encode(data.name+" "+data.address))
            Log.d("MAPS_URI",gmmIntentUri.toString());
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            mapIntent.setPackage("com.google.android.apps.maps")
            try {
                holder.itemView.context.startActivity(mapIntent)
            }
            catch (e : Exception){

            }
        }

        holder.ll_main.setOnClickListener {
            val details = Intent( holder.ll_main.context, DetailsActivity::class.java)
            var detailReq = DetailReq(data.date,data.pincode,data.center_id)
            details.putExtra("centerData", Gson().toJson(detailReq))
            BranchEvent(BRANCH_STANDARD_EVENT.VIEW_ITEM)
                .setDescription(data.name)
                .addCustomDataProperty("Center_ID",data.center_id)
                .logEvent(holder.itemView.context)
            holder.itemView.context.startActivity(details)

        }


         if (data.available_capacity<=5){
             holder.ll_main.background=context.getDrawable(R.drawable.very_low_background)
             holder.ll_doses.background=context.getDrawable(R.drawable.very_low_background)
             holder.rl_main.setBackgroundColor(Color.parseColor("#FC4848"))
             holder.tv_NoOfDoses.setTextColor(Color.parseColor("#FC4848"))
             if (data.paid){
                 holder.iv_fee.setImageResource(R.drawable.ic_paid)
             }
             else{
                 holder.iv_fee.setImageResource(R.drawable.ic_free)
             }
         }
        else if(data.available_capacity>5 && data.available_capacity<=10 ){
             holder.ll_main.background=context.getDrawable(R.drawable.low_background)
             holder.ll_doses.background=context.getDrawable(R.drawable.low_background)
             holder.rl_main.setBackgroundColor(Color.parseColor("#FF9356"))
             holder.tv_NoOfDoses.setTextColor(Color.parseColor("#FF9356"))
             if (data.paid){
                 holder.iv_fee.setImageResource(R.drawable.ic_paid)
             }
             else{
                 holder.iv_fee.setImageResource(R.drawable.ic_free)
             }
         }
        else{
             holder.ll_main.background=context.getDrawable(R.drawable.available_background)
             holder.ll_doses.background=context.getDrawable(R.drawable.available_background)
             holder.rl_main.setBackgroundColor(Color.parseColor("#69CD60"))
             holder.tv_NoOfDoses.setTextColor(Color.parseColor("#69CD60"))
             if (data.paid){
                 holder.iv_fee.setImageResource(R.drawable.ic_paid)
             }
             else{
                 holder.iv_fee.setImageResource(R.drawable.ic_free)
             }
         }
    }

    override fun getItemCount(): Int {
            return all_centers.size
    }
}