package com.subhamassignment.callblocker.Utils

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

class preferances(context: Context) {

    val pref = context.getSharedPreferences("preferences", Context.MODE_PRIVATE)
    var editorx=pref.edit()

    fun blockHiddenNumbers(): Boolean {
        val s = pref.getString("hidden", "0")?.toInt()
        return s == 1
    }
    fun showNotifications(): Boolean {
        val s = pref.getString("notification", "0")?.toInt()
        return s == 1
    }
    fun blockall(): Boolean {
        val s = pref.getString("blockall", "0")?.toInt()
        return s == 1
    }


    object onstart{
       fun onstart(contextl: Context){
           val pref = contextl.getSharedPreferences("preferences", AppCompatActivity.MODE_PRIVATE)
           val editor = pref.edit()
           editor.putString("hidden", "1")
           editor.putString("notification", "1")
           editor.apply()
        }
    }
}