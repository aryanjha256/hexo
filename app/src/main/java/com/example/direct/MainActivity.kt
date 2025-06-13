package com.example.direct

import DashboardScreen
import NetworkSpeedMonitor
import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.direct.ui.theme.DirectTheme
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DirectTheme {
                val navController = rememberNavController()
//                BatteryInfoScreen()
//                MobileDataInfoScreen()
//                RealTimeRamMonitor()
//                NetworkSpeedMonitor()

                NavHost(navController = navController, startDestination = "dashboard") {
                    composable("dashboard") { DashboardScreen(navController) }
                    composable("battery") { BatteryDetailScreen() }
                    composable("ram") { RamDetailScreen() }
                    composable("network") { NetworkDetailScreen() }
                }
            }
        }
    }
}

@Composable
fun BatteryDetailScreen() {
    Text("🔋 Battery Detail View")
}

@Composable
fun RamDetailScreen() {
    Text("💾 RAM Detail View")
}

@Composable
fun NetworkDetailScreen() {
    NetworkSpeedMonitor() // reuse our previous network component
}


@Composable
fun BatteryInfoScreen() {
    val context = LocalContext.current
    var batteryLevel: Int by remember { mutableIntStateOf(-1) }

    // Register receiver only once
    DisposableEffect(Unit) {

        val batteryReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                batteryLevel = getBatteryPercentage(intent)
            }
        }

        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        context.registerReceiver(batteryReceiver, intentFilter)

        onDispose {
            // Good practice: unregister receiver (though ACTION_BATTERY_CHANGED is sticky and safe)
            context.unregisterReceiver(batteryReceiver)
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🔋 Real-time Battery: $batteryLevel%",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}

fun getBatteryPercentage(intent: Intent?): Int {
    if (intent == null) return -1
    val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
    val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
    return if (level >= 0 && scale > 0) (level * 100) / scale else -1
}

@Composable
fun RealTimeRamMonitor() {
    val context = LocalContext.current
    var totalRam by remember { mutableStateOf(0L) }
    var availableRam by remember { mutableStateOf(0L) }

    // Launch side effect that updates RAM info every second
    LaunchedEffect(Unit) {
        while (true) {
            val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val memInfo = ActivityManager.MemoryInfo()
            activityManager.getMemoryInfo(memInfo)

            totalRam = memInfo.totalMem
            availableRam = memInfo.availMem

            delay(1000) // update every 1 second
        }
    }

    val usedRam = totalRam - availableRam
    val usedPercent = if (totalRam > 0) (usedRam * 100 / totalRam) else 0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("💾 Total RAM: ${formatBytes(totalRam)}", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        Text("📗 Available RAM: ${formatBytes(availableRam)}", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        Text("📕 Used RAM: ${formatBytes(usedRam)} ($usedPercent%)", style = MaterialTheme.typography.headlineSmall)
    }
}

fun formatBytes(bytes: Long): String {
    val kb = bytes / 1024
    val mb = kb / 1024
    val gb = mb / 1024
    return when {
        gb > 0 -> "$gb GB"
        mb > 0 -> "$mb MB"
        else -> "$kb KB"
    }
}

