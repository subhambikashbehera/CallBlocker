package com.subhamassignment.callblocker

import android.Manifest
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.subhamassignment.callblocker.Adapter.RecyclerAdapter
import com.subhamassignment.callblocker.Utils.CallReceiver
import com.subhamassignment.callblocker.Utils.preferances
import com.subhamassignment.callblocker.ViewModel.Mainactivity_viewmodel
import com.subhamassignment.callblocker.dataBaseHandle.NumberModel_table
import com.subhamassignment.callblocker.databinding.ActivityMainBinding


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
            val intent = Intent(this, AddNumber::class.java)
            startActivity(intent)
        }
        val x = CallReceiver()
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
        try {
            val check = getSharedPreferences("check", MODE_PRIVATE)
            if (check.getString("isfirst","")  != "no")
            {
                startcheckoption()
            }
        }catch (e:Exception)
        {
            e.printStackTrace()
        }


    }


    override fun onPause() {
        super.onPause()
        val packageManager = packageManager
        val componentName = ComponentName(this@MainActivity, MainActivity::class.java)
        packageManager.setComponentEnabledSetting(
            componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP
        )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val pref = getSharedPreferences("preferences", MODE_PRIVATE)
        val editor = pref.edit()

        when (item.itemId) {

            R.id.share -> {
                try {
                    val intent1 = Intent(Intent.ACTION_SEND)
                    intent1.type = "text/plain"
                    intent1.putExtra(Intent.EXTRA_SUBJECT, "call blocker")
                    val shareMessage =
                        "https://play.google.com/store/apps/details?id=" + com.subhamassignment.callblocker.BuildConfig.APPLICATION_ID + "\n\n"
                    intent1.putExtra(Intent.EXTRA_TEXT, shareMessage)
                    startActivity(Intent.createChooser(intent1, "share by"))
                } catch (e: Exception) {
                    Toast.makeText(this, "error occured", Toast.LENGTH_SHORT).show()
                }

            }
            R.id.block_hidden_numbers -> {
                if (preferances(this).blockHiddenNumbers()) {
                    editor.putString("hidden", "0")
                } else {
                    editor.putString("hidden", "1")
                }
                editor.apply()
            }
            R.id.notifications -> {

                if (preferances(this).showNotifications()) {
                    editor.putString("notification", "0")
                } else {
                    editor.putString("notification", "1")
                }
                editor.apply()

            }
            R.id.blockall -> {
                if (preferances(this).blockall()) {
                    editor.putString("blockall", "0")
                } else {
                    editor.putString("blockall", "1")
                }
                editor.apply()
            }
        }
        return true
    }
    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        try {
            menu!!.findItem(R.id.block_hidden_numbers).isChecked =
                preferances(this).blockHiddenNumbers()
            menu.findItem(R.id.notifications).isChecked = preferances(this).showNotifications()
            menu.findItem(R.id.blockall).isChecked = preferances(this).blockall()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }
    fun startcheckoption(){
        preferances.onstart.onstart(this)
        val check = getSharedPreferences("check", MODE_PRIVATE)
        val chss=check.edit()
        chss.putString("isfirst","no")
        chss.apply()
    }


}