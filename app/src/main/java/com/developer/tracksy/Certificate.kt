package com.developer.tracksy

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.media.RingtoneManager
import android.net.Uri
import android.os.*
import android.os.Environment.getExternalStoragePublicDirectory
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.developer.tracksy.Models.OtpApiResponseModel
import com.developer.tracksy.Models.SuccessOtpApiResponseModel
import com.developer.tracksy.Utilities.ConfirmOTPClass
import com.developer.tracksy.Utilities.StringToSHA
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import com.google.gson.JsonObject
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.*

class Certificate : AppCompatActivity() {
    private val TAG = "VACCINE_TRACKER"
    var bottomSheetBehavior: BottomSheetBehavior<*>? = null
    var downloadStatus : Boolean = false
    lateinit var filePath : File
    lateinit var sendBtn : Button
    lateinit var verifyBtn : Button
    lateinit var download : Button
    lateinit var back : ImageButton
    lateinit var tilMobile : TextInputLayout
    lateinit var tilOTP : TextInputLayout
    lateinit var tilID : TextInputLayout
    lateinit var llEnterotp : LinearLayout
    lateinit var llEnterMob : LinearLayout
    lateinit var llEnterBenID : LinearLayout
    lateinit var llDownloaded : LinearLayout
    lateinit var loadingDialog: LoadingDialog
    var txnID = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certificate)
        val view2 = findViewById<View>(R.id.otp_sheet)
        back = view2.findViewById<ImageButton>(R.id.backArrowOtp)
         llEnterotp = view2.findViewById<LinearLayout>(R.id.enter_otp)
        llEnterMob = view2.findViewById<LinearLayout>(R.id.enter_mobile)
        llEnterBenID = view2.findViewById<LinearLayout>(R.id.enter_benID)
        llDownloaded = view2.findViewById<LinearLayout>(R.id.downloaded)
        tilID = view2.findViewById<TextInputLayout>(R.id.sbs_tilID)
        tilMobile = view2.findViewById<TextInputLayout>(R.id.sbs_tilMobNum)
        tilOTP = view2.findViewById<TextInputLayout>(R.id.sbs_tilOtp)
        sendBtn=view2.findViewById(R.id.otp_send)
        download=view2.findViewById(R.id.otp_download)
        verifyBtn=view2.findViewById(R.id.otp_verify)
        loadingDialog = LoadingDialog(this)
        back.setOnClickListener {
            finish()
        }
        bottomSheetBehavior = BottomSheetBehavior.from<View>(view2)
        (bottomSheetBehavior as BottomSheetBehavior<View>).isHideable = false
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
        }
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        filePath = File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)!!.path + File.separator.toString() +"42786365008140"+"_Vaccine_Certificate.pdf")
//        buildNotification()
        sendBtn.setOnClickListener {
            loadingDialog.startLoading()
            tilMobile.isErrorEnabled=false
            if (tilMobile.editText!!.text.isNotEmpty()){
                if (tilMobile.editText!!.text.length==10){
                    sendOtp(tilMobile.editText!!.text.toString())
                }
                else{
                    loadingDialog.dismissDialog()
                    tilMobile.isErrorEnabled=true
                    tilMobile.error="Please Enter Valid Mobile No"
                }
            }
            else{
                loadingDialog.dismissDialog()
                tilMobile.isErrorEnabled=true
                tilMobile.error="Please Enter Mobile No"
            }
        }
        download.setOnClickListener {
            loadingDialog.startLoading()
            tilID.isErrorEnabled=false
            if (tilID.editText!!.text.isNotEmpty()){
                if (tilID.editText!!.text.length>=13){
                    downloadFile(tilID.editText!!.text.toString())
                }
                else{
                    loadingDialog.dismissDialog()
                    tilID.isErrorEnabled=true
                    tilID.error="Please Enter Valid ID"
                }
            }
            else{
                loadingDialog.dismissDialog()
                tilID.isErrorEnabled=true
                tilID.error="Please Enter 13 Digit ID"
            }
        }
        verifyBtn.setOnClickListener {
            loadingDialog.startLoading()
            tilOTP.isErrorEnabled=false
            if (tilOTP.editText!!.text.isNotEmpty()){
                if (tilOTP.editText!!.text.length==6){
                    verifyOTP(tilOTP.editText!!.text.toString())
                }
                else{
                    loadingDialog.dismissDialog()
                    tilOTP.isErrorEnabled=true
                    tilOTP.error="Please Enter Valid OTP"
                }
            }
            else{
                loadingDialog.dismissDialog()
                tilOTP.isErrorEnabled=true
                tilOTP.error="Please Enter OTP"
            }
        }
    }

    private fun verifyOTP(otp: String) {
        var sha: StringToSHA = StringToSHA(otp)
      var co:ConfirmOTPClass = ConfirmOTPClass(sha.sha,txnID)
        API.get().confirmOTP(co).enqueue(object : Callback<SuccessOtpApiResponseModel<String>>{
            override fun onFailure(call: Call<SuccessOtpApiResponseModel<String>>, t: Throwable) {
                loadingDialog.dismissDialog()
                Snackbar.make(back,t.message.toString(),Snackbar.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<SuccessOtpApiResponseModel<String>>, response: Response<SuccessOtpApiResponseModel<String>>) {
                if (response.isSuccessful){
                    Snackbar.make(sendBtn,"OTP Verified",Snackbar.LENGTH_SHORT).show()
                    Log.d("GENERATE","TOKEN: "+response.body()!!.token)
                    AppTracksy.token=response.body()!!.token
                    llEnterotp.visibility=View.GONE
                    llEnterBenID.visibility=View.VISIBLE
                    llDownloaded.visibility=View.GONE
                    llEnterMob.visibility=View.GONE
                    loadingDialog.dismissDialog()
                }
            }
        })

    }
    private fun writeResponseBodyToDisk(body: ResponseBody): Boolean {
        return try {
            filePath = File(getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)!!.path + File.separator.toString() +tilID.editText!!.text.toString() +"_Vaccine_Certificate.pdf")
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                val fileSize = body.contentLength()
                var fileSizeDownloaded: Long = 0
                inputStream = body.byteStream()
                outputStream = FileOutputStream(filePath)
                while (true) {
                    val read: Int = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                    fileSizeDownloaded += read.toLong()
                    Log.d(TAG, "file download: $fileSizeDownloaded of $fileSize")
                }
                outputStream.flush()
                true
            } catch (e: IOException) {
                false
            } finally {
                if (inputStream != null) {
                    inputStream.close()
                }
                if (outputStream != null) {
                    outputStream.close()
                }
            }
        } catch (e: IOException) {
            false
        }
    }

    private fun downloadFile(id : String) {
        API.get().downloadCertificate(id).enqueue(object : Callback<ResponseBody>{
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Snackbar.make(back,t.message.toString(),Snackbar.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<ResponseBody>,response: Response<ResponseBody>) {
                if (response.isSuccessful){
                  object : AsyncTask<Void?, Void?, Void?>() {

                         override fun doInBackground(vararg voids: Void?): Void? {
                            downloadStatus=writeResponseBodyToDisk(response.body()!!)
                            return null
                        }

                      override fun onPostExecute(result: Void?) {
                          super.onPostExecute(result)
                          if (downloadStatus){
                              loadingDialog.dismissDialog()
                              llEnterBenID.visibility=View.GONE
                              llDownloaded.visibility=View.VISIBLE
                              buildNotification()

                          }
                          else{
                              Snackbar.make(back,"Something Broke Down!",Snackbar.LENGTH_SHORT).show()
                              loadingDialog.dismissDialog()
                          }
                      }

                    }.execute()
                }else if (response.code()==500){
                    loadingDialog.dismissDialog()
                    Snackbar.make(back,"Beneficiary Reference ID Does Not Exist",Snackbar.LENGTH_SHORT).show()
                }

            }
        })
    }

    private fun buildNotification() {


        val target = Intent(Intent.ACTION_VIEW)
        target.setDataAndType(Uri.fromFile(filePath), "application/pdf")
        target.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        target.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        val pdfOpenintent = Intent.createChooser(target, "Open File")
        pdfOpenintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(this, System.currentTimeMillis().toInt() / 100, pdfOpenintent, PendingIntent.FLAG_ONE_SHOT)
        val builder = NotificationCompat.Builder(this, createNotificationChannel()!!)
                .setSmallIcon(R.drawable.tpng)
                .setColor(getResources().getColor(R.color.colorPrimaryDark))
                .setContentTitle("Certificate Downloaded")
                .setContentText(tilID.editText!!.text.toString() +"_Vaccine_Certificate.pdf")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .addAction(R.drawable.ic_baseline_arrow_downward_24, getString(R.string.Download),
                        pendingIntent)
        val notificationManager = NotificationManagerCompat.from(this)
        notificationManager.notify(System.currentTimeMillis().toInt() / 100, builder.build())
    }

    private fun createNotificationChannel(): String? {
        val CHANNEL_ID = "TRACKSY_NOTIFICATIONS"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name: CharSequence = "TRACKSY Notifications"
            val description = "Vaccine Certificate Notifications"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.description = description
            channel.enableVibration(true)
            channel.canShowBadge()
            channel.enableLights(true)
            channel.importance = NotificationManager.IMPORTANCE_HIGH
            channel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
        return CHANNEL_ID
    }

    private fun sendOtp(mob: String) {
        var js = JsonObject();
        js.addProperty("mobile",mob);
           API.get().generateOTP(js).enqueue(object : Callback<OtpApiResponseModel<String>>{
               override fun onFailure(call: Call<OtpApiResponseModel<String>>, t: Throwable) {
                   loadingDialog.dismissDialog()
                   Snackbar.make(back,t.message.toString(),Snackbar.LENGTH_SHORT).show()
               }

               override fun onResponse(call: Call<OtpApiResponseModel<String>>, response: Response<OtpApiResponseModel<String>>) {
                   if (response.isSuccessful()){
                       Snackbar.make(sendBtn,"Otp Sent",Snackbar.LENGTH_SHORT).show()
                    txnID=response.body()!!.txnId
                       llEnterMob.visibility=View.GONE
                       llEnterBenID.visibility=View.GONE
                       llDownloaded.visibility=View.GONE
                       llEnterotp.visibility=View.VISIBLE
                       loadingDialog.dismissDialog()
                }else if (response.code()==400){
                       loadingDialog.dismissDialog()
                       Snackbar.make(sendBtn,"Please Try After Sometime!",Snackbar.LENGTH_SHORT).show()
                   }
               }
           })
    }
}