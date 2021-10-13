package com.subhamassignment.callblocker.Utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.subhamassignment.callblocker.MainActivity

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(p0: Context?, p1: Intent?) {

        if (Intent.ACTION_BOOT_COMPLETED == p1?.action) {
            val serviceIntent = Intent(p0, MainActivity::class.java)
           p0?.startService(serviceIntent)
        }

    }
}