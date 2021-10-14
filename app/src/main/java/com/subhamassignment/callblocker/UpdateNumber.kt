package com.subhamassignment.callblocker

import android.content.ComponentName
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.subhamassignment.callblocker.ViewModel.Mainactivity_viewmodel
import com.subhamassignment.callblocker.dataBaseHandle.NumberModel_table
import com.subhamassignment.callblocker.databinding.ActivityUpdateNumberBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateNumber : AppCompatActivity() {

    lateinit var binding:ActivityUpdateNumberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=DataBindingUtil.setContentView(this,R.layout.activity_update_number)
        val vmc = ViewModelProvider(this)[Mainactivity_viewmodel::class.java]
        val id=intent.getIntExtra("id",0)
        val namex=intent.getStringExtra("name")
        val numberx=intent.getStringExtra("number")
        val tc=intent.getStringExtra("tc")
        val nc=intent.getStringExtra("nc")

        binding.name.setText(namex)
        binding.number.setText(numberx)


        binding.add.setOnClickListener {
            val name = binding.name.text.toString()
            val number = binding.number.text.toString().trim { it <= ' ' }.replace(" ","")

            if (TextUtils.isEmpty(name)) {
                binding.name.error = "required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(number)) {
                binding.number.error = "required"
                return@setOnClickListener
            }
            CoroutineScope(Dispatchers.IO).launch {
                vmc.updatedetails(NumberModel_table(id.toString().toInt(), name, tc!!, number, nc!!))
                finish()
            }
        }
        binding.cancel.setOnClickListener {
            finish()
        }
    }


    override fun onPause() {
        super.onPause()
        val packageManager = packageManager
        val componentName = ComponentName(this, MainActivity::class.java)
        packageManager.setComponentEnabledSetting(componentName,
            PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
            PackageManager.DONT_KILL_APP)
    }
}