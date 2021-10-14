package com.subhamassignment.callblocker

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.subhamassignment.callblocker.ViewModel.Mainactivity_viewmodel
import com.subhamassignment.callblocker.dataBaseHandle.NumberDatabase
import com.subhamassignment.callblocker.dataBaseHandle.NumberModel_table
import com.subhamassignment.callblocker.databinding.ActivityAddNumberBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNumber : AppCompatActivity() {


    lateinit var binding: ActivityAddNumberBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_number)
        val vmc = ViewModelProvider(this)[Mainactivity_viewmodel::class.java]
        binding.add.setOnClickListener {
            val name = binding.name.text.toString().trim()
            var number = binding.number.text.toString().trim { it <= ' ' }.replace(" ","")

            if (TextUtils.isEmpty(name)) {
                binding.name.error = "required"
                return@setOnClickListener
            }
            if (TextUtils.isEmpty(number)) {
                binding.number.error = "required"
                return@setOnClickListener
            }
            if (number.length==10)
            {
                number= "+91$number"
            }

            CoroutineScope(Dispatchers.IO).launch {
                vmc.addnumber(NumberModel_table(null, name, "00:00", number, "0"))
                finish()
            }
        }
        binding.cancel.setOnClickListener {
            finish()
        }
        binding.contact.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
            startActivityForResult(intent, 102)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            val contacturi = data?.data ?: return
            val cols = arrayOf(
                ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            )
            var rs = contentResolver.query(contacturi, cols, null, null, null)
            if (rs!!.moveToFirst()) {
                binding.number.setText(rs.getString(0))
                binding.name.setText(rs.getString(1))

            }


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