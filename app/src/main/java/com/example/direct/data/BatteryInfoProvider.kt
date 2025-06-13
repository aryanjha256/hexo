package com.example.direct.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter

object BatteryInfoProvider {
    private var batteryPercent = "N/A"

    fun start(context: Context) {
        val filter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val level = intent?.getIntExtra("level", -1) ?: -1
                val scale = intent?.getIntExtra("scale", -1) ?: -1
                if (level >= 0 && scale > 0) {
                    val percent = (level * 100 / scale)
                    batteryPercent = "$percent%"
                }
            }
        }, filter)
    }

    fun getBatteryPercent(): String = batteryPercent
}
