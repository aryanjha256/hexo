package com.example.direct.data

import android.app.ActivityManager
import android.content.Context

object RamInfoProvider {
    private var totalRam = 0L
    private var availableRam = 0L

    fun update(context: Context) {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memInfo)

        totalRam = memInfo.totalMem
        availableRam = memInfo.availMem
    }

    fun getRamUsedPercent(): Int {
        val used = totalRam - availableRam
        return if (totalRam > 0) ((used * 100) / totalRam).toInt() else 0
    }

    fun getFormatted(): String {
        val used = totalRam - availableRam
        return "Used: ${formatBytes(used)} / ${formatBytes(totalRam)}"
    }

    private fun formatBytes(bytes: Long): String {
        val kb = bytes / 1024
        val mb = kb / 1024
        val gb = mb / 1024
        return when {
            gb > 0 -> "$gb GB"
            mb > 0 -> "$mb MB"
            else -> "$kb KB"
        }
    }
}
