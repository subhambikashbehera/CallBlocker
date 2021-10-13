package com.subhamassignment.callblocker

import android.Manifest
import android.content.AsyncTaskLoader
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.subhamassignment.callblocker.Adapter.RecyclerAdapter
import com.subhamassignment.callblocker.Utils.BlacklistObserver
import com.subhamassignment.callblocker.ViewModel.Mainactivity_viewmodel
import com.subhamassignment.callblocker.dataBaseHandle.NumberModel_table
import com.subhamassignment.callblocker.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.LinkedHashSet
import android.content.IntentFilter
import com.subhamassignment.callblocker.Utils.CallReceiver


open class MainActivity : AppCompatActivity() {


    private lateinit var adapterx: RecyclerAdapter
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        requestPermissions()

        val vmc = ViewModelProvider(this)[Mainactivity_viewmodel::class.java]

        vmc.getallnumber.observe(this, { allnumber ->
            adapterx = RecyclerAdapter(this, allnumber as ArrayList<NumberModel_table>)
            binding.recyclerView.adapter = adapterx
            val lm = LinearLayoutManager(this)
            binding.recyclerView.layoutManager = lm
        })
        binding.addnumber.setOnClickListener {
            val intent= Intent(this,AddNumber::class.java)
            startActivity(intent)
        }
        val x=CallReceiver()
        val intentFilter = IntentFilter("android.intent.action.PHONE_STATE")
        registerReceiver(x, intentFilter)





    }

    protected fun requestPermissions() {
        val requiredPermissions: MutableList<String> = java.util.ArrayList()
        requiredPermissions.add(Manifest.permission.CALL_PHONE)
        requiredPermissions.add(Manifest.permission.READ_PHONE_STATE)
        requiredPermissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        requiredPermissions.add(Manifest.permission.READ_CALL_LOG)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            requiredPermissions.add(Manifest.permission.ANSWER_PHONE_CALLS)
        }
        val missingPermissions: MutableList<String> = java.util.ArrayList()
        for (permission in requiredPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                !== PackageManager.PERMISSION_GRANTED
            ) {
                missingPermissions.add(permission)
            }
        }
        if (missingPermissions.isNotEmpty()) {
            ActivityCompat.requestPermissions(
                this,
                missingPermissions.toTypedArray(), 0
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var ok = true
        if (grantResults.isNotEmpty()) {
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    ok = false
                    break
                }
            }
        } else {
            // treat cancellation as failure
            ok = false
        }

        if (!ok) {
            Snackbar.make(
                binding.mainla,
                "Permission required",
                Snackbar.LENGTH_INDEFINITE
            )
                .setAction("OK",
                    View.OnClickListener { requestPermissions() })
                .show()

        }


    }






}