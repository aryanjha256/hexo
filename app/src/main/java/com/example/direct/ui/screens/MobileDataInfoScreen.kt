import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.telephony.TelephonyManager
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@SuppressLint("MissingPermission")
@Composable
fun MobileDataInfoScreen() {
    val context = LocalContext.current
    val telephonyManager = remember {
        context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    }

    // Live-updatable values
    var carrierName by remember { mutableStateOf("Unknown") }
    var isConnected by remember { mutableStateOf(false) }
    var networkType by remember { mutableStateOf("N/A") }

    LaunchedEffect(Unit) @androidx.annotation.RequiresPermission(android.Manifest.permission.ACCESS_NETWORK_STATE) {
        // Get carrier name
        carrierName = telephonyManager.networkOperatorName

        // Check connection status via ConnectivityManager
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        isConnected = capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) == true

        // Map network type
        networkType = when (telephonyManager.dataNetworkType) {
            TelephonyManager.NETWORK_TYPE_LTE -> "4G LTE"
            TelephonyManager.NETWORK_TYPE_NR -> "5G"
            TelephonyManager.NETWORK_TYPE_HSPAP -> "3G HSPA+"
            TelephonyManager.NETWORK_TYPE_EDGE -> "2G EDGE"
            else -> "Unknown (${telephonyManager.dataNetworkType})"
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("📶 Carrier: $carrierName", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text("📡 Connected: ${if (isConnected) "Yes" else "No"}", style = MaterialTheme.typography.bodyLarge)
        Spacer(Modifier.height(8.dp))
        Text("🔌 Network Type: $networkType", style = MaterialTheme.typography.bodyLarge)
    }
}
