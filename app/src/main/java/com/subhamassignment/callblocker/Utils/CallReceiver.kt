package com.subhamassignment.callblocker.Utils


import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.android.internal.telephony.ITelephony
import com.subhamassignment.callblocker.MainActivity
import com.subhamassignment.callblocker.R
import com.subhamassignment.callblocker.dataBaseHandle.NumberDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalTime
import java.time.format.DateTimeFormatter


open class CallReceiver : BroadcastReceiver() {


    override fun onReceive(context: Context, intent: Intent) {
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED == intent.action && intent.getStringExtra(
                TelephonyManager.EXTRA_STATE
            ) == TelephonyManager.EXTRA_STATE_RINGING
        ) {
            var incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)
                ?: return
            Log.i(TAG, "Received call: $incomingNumber")
            if (TextUtils.isEmpty(incomingNumber)) {
                if (preferances(context).blockHiddenNumbers())
                {
                    rejectCall(context, null)
                }
            } else {
                try {
                    CoroutineScope(Dispatchers.IO).launch {
                        val dm = NumberDatabase.getdbinstance(context)
                        val id = dm.Daocall().getid(incomingNumber)
                        val name = dm.Daocall().getname(incomingNumber)
                        val nuumber=dm.Daocall().getnum(incomingNumber)
                        val nc = dm.Daocall().getnocalls(incomingNumber)
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("hh.mm")
                           try {
                               dm.Daocall().updatesecific(
                                   LocalTime.now().format(formatter).toString(),
                                   (nc.toInt() + 1).toString(), id
                               )
                           }catch (e:Exception){
                              e.printStackTrace()
                           }
                        }

                        if (preferances(context).blockall())
                        {
                            rejectCall(context, incomingNumber)
                        }
                        else if (incomingNumber==nuumber)
                        {
                            rejectCall(context, incomingNumber)
                        }

               //         rejectCall(context, incomingNumber)

                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                //rejectCall(context,incomingNumber)
            }
        }
        if (intent.getStringExtra(
                TelephonyManager.EXTRA_STATE
            ) == TelephonyManager.EXTRA_STATE_OFFHOOK
        ) AlreadyOnCall =
            true else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE) == TelephonyManager.EXTRA_STATE_IDLE) AlreadyOnCall =
            false
    }

    @SuppressLint("MissingPermission")
    protected fun rejectCall(context: Context, numberd: String?) {
        if (!AlreadyOnCall) {
            var failed = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val telecomManager =
                    context.getSystemService(Context.TELECOM_SERVICE) as TelecomManager
                try {
                    telecomManager.endCall()
                    Log.d(TAG, "Invoked 'endCall' on TelecomManager")
                } catch (e: Exception) {
                    Log.e(TAG, "Couldn't end call with TelecomManager", e)
                    failed = true
                }
            } else {
                val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                try {
                    val m = tm.javaClass.getDeclaredMethod("getITelephony")
                    m.isAccessible = true
                    val telephony = m.invoke(tm) as ITelephony
                    telephony.endCall()
                } catch (e: Exception) {
                    Log.e(TAG, "Couldn't end call with TelephonyManager", e)
                    failed = true
                }
            }
            if (failed) {
                Toast.makeText(
                    context,
                    "block not possible",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        if (Build.VERSION.SDK_INT >= 26) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val channel = NotificationChannel(
                "default",
                "call blocker",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "call rejected"
            notificationManager.createNotificationChannel(channel)
        }
        if (preferances(context).showNotifications()){
            val notify: Notification = NotificationCompat.Builder(context, "M_CH_ID")
                .setSmallIcon(R.drawable.ic_launcher_small)
                .setContentTitle("call rejected for $numberd")
                .setContentText("Tap To see")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setShowWhen(true)
                .setAutoCancel(true)
                .setContentIntent(
                    PendingIntent.getActivity(
                        context, 0, Intent(
                            context,
                            MainActivity::class.java
                        ), PendingIntent.FLAG_UPDATE_CURRENT
                    )
                )
                .addPerson("tel:$numberd")
                .setGroup("rejected")
                .setChannelId("default")
                .setGroupSummary(true)
                .build()
            val tag = numberd ?: "private"
            NotificationManagerCompat.from(context).notify(tag.toString(), NOTIFY_REJECTED, notify)
        }
    }

    companion object {
        private const val TAG = "NoPhoneSpam"
        private const val NOTIFY_REJECTED = 0
        private var AlreadyOnCall = false
    }
}
