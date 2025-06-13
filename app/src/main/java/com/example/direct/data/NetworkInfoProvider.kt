package com.example.direct.data

import android.annotation.SuppressLint
import android.net.TrafficStats

object NetworkInfoProvider {
    private var prevRx = TrafficStats.getTotalRxBytes()
    private var prevTx = TrafficStats.getTotalTxBytes()

    private var downloadSpeed = 0L
    private var uploadSpeed = 0L

    fun update() {
        val nowRx = TrafficStats.getTotalRxBytes()
        val nowTx = TrafficStats.getTotalTxBytes()

        downloadSpeed = nowRx - prevRx
        uploadSpeed = nowTx - prevTx

        prevRx = nowRx
        prevTx = nowTx
    }

    fun downloadSpeedStr(): String = formatSpeed(downloadSpeed)
    fun uploadSpeedStr(): String = formatSpeed(uploadSpeed)

    @SuppressLint("DefaultLocale")
    private fun formatSpeed(bytesPerSecond: Long): String {
        val kb = bytesPerSecond / 1024.0
        val mb = kb / 1024.0
        return when {
            mb >= 1 -> String.format("%.2f MB/s", mb)
            kb >= 1 -> String.format("%.2f KB/s", kb)
            else -> "$bytesPerSecond B/s"
        }
    }
}
