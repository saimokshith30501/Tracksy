package com.developer.tracksy

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class SearchActivity : AppCompatActivity() {
    lateinit var tilPincode :TextInputLayout
    lateinit var tilDate :TextInputLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        val view2 = findViewById<View>(R.id.bottom_sheet_search)
        val back = view2.findViewById<ImageButton>(R.id.backArrow)
        back.setOnClickListener { finish() }
        var bottomSheetBehavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(view2)
        bottomSheetBehavior.setHideable(false)
        bottomSheetBehavior.peekHeight=2000
        val defaultCalender: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val selectedCalender: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        val nextDaysCalender: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        var tvPinCode = view2.findViewById<TextView>(R.id.tv_pincode)
        var tvDistrict = view2.findViewById<TextView>(R.id.tv_district)
        var cvPinCode = view2.findViewById<CardView>(R.id.cv_pincode)
        var cvDistrict = view2.findViewById<CardView>(R.id.cv_district)
         tilPincode = view2.findViewById<TextInputLayout>(R.id.sbs_tilPinCode)
         tilDate = view2.findViewById<TextInputLayout>(R.id.sbs_tilDate)
        var checkButton = view2.findViewById<MaterialButton>(R.id.sbs_btCheck)
        var doseType = view2.findViewById<RadioGroup>(R.id.sbs_rgDose)
        tvPinCode.setOnClickListener {
            cvPinCode.setCardBackgroundColor(resources.getColor(R.color.white))
            cvDistrict.setCardBackgroundColor(resources.getColor(R.color.sliderBackground))
        }
        tvDistrict.setOnClickListener {
            cvPinCode.setCardBackgroundColor(resources.getColor(R.color.sliderBackground))
            cvDistrict.setCardBackgroundColor(resources.getColor(R.color.white))
        }
        checkButton.setOnClickListener {
            if (!validateDate() or !validatePincode()){
                return@setOnClickListener
            }
            var searchResults = Intent(applicationContext, SearchResults::class.java)
            var dose=view2.findViewById<RadioButton>(doseType.checkedRadioButtonId).tag.toString()
            searchResults.putExtra("PINCODE", tilPincode.editText!!.text.toString())
            searchResults.putExtra("DATE", tilDate.editText!!.text.toString())
            searchResults.putExtra("DAY", selectedCalender.get(Calendar.DAY_OF_MONTH).toString())
            searchResults.putExtra("MONTH", (selectedCalender.get(Calendar.MONTH)).toString())
            searchResults.putExtra("YEAR", selectedCalender.get(Calendar.YEAR).toString())
            searchResults.putExtra("DOSE", dose)
            startActivity(searchResults)

        }
        //

        tilDate.editText!!.setOnClickListener {
            val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
            var dateSetListener : DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                selectedCalender.set(year,month,dayOfMonth)
                nextDaysCalender.set(year,month,dayOfMonth)
                nextDaysCalender.add(Calendar.DATE,1)
                var SelectedDob : String=dayOfMonth.toString()+"-"+(month+1)+"-"+year.toString()
                tilDate.editText!!.setText(SelectedDob)
            }
            var datePickerDialog = DatePickerDialog(
                    this, dateSetListener, selectedCalender.get(
                    Calendar.YEAR
            ), selectedCalender.get(Calendar.MONTH), selectedCalender.get(Calendar.DAY_OF_MONTH)
            )
            datePickerDialog.datePicker.minDate=defaultCalender.timeInMillis
            datePickerDialog.show()
        }
    }
    private fun validatePincode(): Boolean {
        val value: String = tilPincode.editText!!.getText().toString()
        return if (value.isEmpty()) {
            tilPincode.setError("Pincode cannot be empty")
            false
        } else if (value.length<6) {
            tilPincode.setError("Enter a valid Pincode")
            false
        } else {
            tilPincode.isErrorEnabled=false
            true
        }
    }
    private fun validateDate(): Boolean {
        val value: String = tilDate.editText!!.getText().toString()
        return if (value.isEmpty()) {
            tilDate.setError("Date cannot be empty")
            false
        } else {
            tilDate.isErrorEnabled=false
            true
        }
    }
}