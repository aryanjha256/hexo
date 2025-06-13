import android.net.TrafficStats
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

@Composable
fun NetworkSpeedMonitor() {
    var downloadSpeed by remember { mutableStateOf(0L) }
    var uploadSpeed by remember { mutableStateOf(0L) }

    // Store previous total byte counts
    var prevRxBytes by remember { mutableStateOf(TrafficStats.getTotalRxBytes()) }
    var prevTxBytes by remember { mutableStateOf(TrafficStats.getTotalTxBytes()) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)

            val newRxBytes = TrafficStats.getTotalRxBytes()
            val newTxBytes = TrafficStats.getTotalTxBytes()

            downloadSpeed = newRxBytes - prevRxBytes
            uploadSpeed = newTxBytes - prevTxBytes

            prevRxBytes = newRxBytes
            prevTxBytes = newTxBytes
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⬇️ Download Speed: ${formatSpeed(downloadSpeed)}",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "⬆️ Upload Speed: ${formatSpeed(uploadSpeed)}",
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

fun formatSpeed(bytesPerSecond: Long): String {
    val kb = bytesPerSecond / 1024.0
    val mb = kb / 1024.0
    return when {
        mb >= 1 -> String.format("%.2f MB/s", mb)
        kb >= 1 -> String.format("%.2f KB/s", kb)
        else -> "$bytesPerSecond B/s"
    }
}