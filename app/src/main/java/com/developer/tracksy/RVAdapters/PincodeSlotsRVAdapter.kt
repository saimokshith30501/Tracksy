package com.developer.tracksy.RVAdapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.developer.tracksy.Models.APIPincodeResult
import com.developer.tracksy.Models.CentersData
import com.developer.tracksy.R

class PincodeSlotsRVAdapter(var all_centers: ArrayList<CentersData>,var context: Context) : RecyclerView.Adapter<PincodeSlotsRVAdapter.ItemsViewHolder>() {
    class ItemsViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal var tv_CenterName: TextView
        internal var tv_DoseName: TextView
        internal var tv_NoOfDoses: TextView
        internal var tv_Vaccine: TextView
        internal var tv_Fee: TextView
        internal var tv_From: TextView
        internal var tv_To: TextView
        internal var iv_fee: ImageView
        internal var iv_rupee: ImageView
        internal var iv_vaccine: ImageView
        internal var iv_directions: ImageView
        internal var ll_main: LinearLayout
        internal var ll_rupee: LinearLayout
        internal var ll_doses: LinearLayout
        internal var rl_main: RelativeLayout

        init {
            tv_CenterName = itemView.findViewById<TextView>(R.id.ci_tvName)
            tv_DoseName = itemView.findViewById<TextView>(R.id.ci_tvDose)
            tv_NoOfDoses = itemView.findViewById<TextView>(R.id.ci_tvNoOfDoses)
            tv_To = itemView.findViewById<TextView>(R.id.ci_tvTo)
            tv_Vaccine = itemView.findViewById<TextView>(R.id.ci_tvVaccine)
            tv_Fee = itemView.findViewById<TextView>(R.id.ci_tvFee)
            tv_From = itemView.findViewById<TextView>(R.id.ci_tvFrom)
            iv_fee = itemView.findViewById<ImageView>(R.id.ci_ivFeeTop)
            iv_fee = itemView.findViewById<ImageView>(R.id.ci_ivFeeTop)
            iv_rupee = itemView.findViewById<ImageView>(R.id.ci_ivRupee)
            iv_vaccine = itemView.findViewById<ImageView>(R.id.ci_ivVaccine)
            iv_directions = itemView.findViewById<ImageView>(R.id.ci_ivDirections)
            ll_main = itemView.findViewById<LinearLayout>(R.id.ci_llMain)
            ll_doses = itemView.findViewById<LinearLayout>(R.id.ci_llDoses)
            ll_rupee = itemView.findViewById<LinearLayout>(R.id.ci_llRupee)
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
        holder.tv_From.text="From : "+data.from
        holder.tv_To.text="To : "+data.to
        holder.tv_NoOfDoses.text=data.available_capacity.toString()
         if (data.available_capacity<=5){
             holder.ll_main.background=context.getDrawable(R.drawable.very_low_background)
             holder.ll_doses.background=context.getDrawable(R.drawable.very_low_background)
             holder.rl_main.setBackgroundColor(Color.parseColor("#FC4848"))
             holder.tv_NoOfDoses.setTextColor(Color.parseColor("#FC4848"))
             if (data.paid){
                 holder.iv_fee.setImageResource(R.drawable.ic_paid)
                 holder.ll_rupee.visibility=View.VISIBLE
                 holder.ll_rupee.background=context.getDrawable(R.drawable.very_low_background)
                 holder.tv_Fee.text=data.fee.toString()
                 holder.tv_Fee.setTextColor(Color.parseColor("#FC4848"))
                 holder.iv_rupee.setColorFilter(ContextCompat.getColor(context, R.color.verLow))
             }
             else{
                 holder.iv_fee.setImageResource(R.drawable.ic_free)
                 holder.ll_rupee.visibility=View.GONE
             }
         }
        else if(data.available_capacity>5 && data.available_capacity<=10 ){
             holder.ll_main.background=context.getDrawable(R.drawable.low_background)
             holder.ll_doses.background=context.getDrawable(R.drawable.low_background)
             holder.rl_main.setBackgroundColor(Color.parseColor("#FF9356"))
             holder.tv_NoOfDoses.setTextColor(Color.parseColor("#FF9356"))
             if (data.paid){
                 holder.iv_fee.setImageResource(R.drawable.ic_paid)
                 holder.ll_rupee.visibility=View.VISIBLE
                 holder.ll_rupee.background=context.getDrawable(R.drawable.low_background)
                 holder.tv_Fee.text=data.fee.toString()
                 holder.tv_Fee.setTextColor(Color.parseColor("#FF9356"))
                 holder.iv_rupee.setColorFilter(ContextCompat.getColor(context, R.color.low))
             }
             else{
                 holder.iv_fee.setImageResource(R.drawable.ic_free)
                 holder.ll_rupee.visibility=View.GONE
             }
         }
        else{
             holder.ll_main.background=context.getDrawable(R.drawable.available_background)
             holder.ll_doses.background=context.getDrawable(R.drawable.available_background)
             holder.rl_main.setBackgroundColor(Color.parseColor("#69CD60"))
             holder.tv_NoOfDoses.setTextColor(Color.parseColor("#69CD60"))
             if (data.paid){
                 holder.iv_fee.setImageResource(R.drawable.ic_paid)
                 holder.ll_rupee.visibility=View.VISIBLE
                 holder.ll_rupee.background=context.getDrawable(R.drawable.available_background)
                 holder.tv_Fee.text=data.fee.toString()
                 holder.tv_Fee.setTextColor(Color.parseColor("#69CD60"))
                 holder.iv_rupee.setColorFilter(ContextCompat.getColor(context, R.color.available))
             }
             else{
                 holder.iv_fee.setImageResource(R.drawable.ic_free)
                 holder.ll_rupee.visibility=View.GONE
             }
         }
    }

    override fun getItemCount(): Int {
            return all_centers.size
    }
}