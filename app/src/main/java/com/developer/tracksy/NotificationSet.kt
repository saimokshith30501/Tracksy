package com.developer.tracksy

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.util.*

class NotificationSet : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notification_set)
        var time :TextInputLayout= findViewById(R.id.bs_tilTime)
        var set :Button= findViewById(R.id.bs_btSET)
        var cancel :Button= findViewById(R.id.bs_btCancel)

        cancel.setOnClickListener {
            val notifyIntent = Intent(this, MyReceiver::class.java)
            val calendar = Calendar.getInstance()
            val pendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
            val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent)
        }

        set.setOnClickListener {



            if (time.editText!!.text.toString().isNotEmpty()) {
                val notifyIntent = Intent(this, MyReceiver::class.java)
                val calendar = Calendar.getInstance()
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    0,
                    notifyIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT
                )
                val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis(),
                    time.editText!!.text.toString().toLong()*1000,
                    pendingIntent
                )
                Snackbar.make(set,"Success",Snackbar.LENGTH_SHORT).show()
                Handler().postDelayed({
                    finish()
                }, 2000)
            }else{
                Snackbar.make(set,"Please enter Interval",Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}