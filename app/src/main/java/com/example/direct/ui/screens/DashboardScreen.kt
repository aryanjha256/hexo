import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.navigation.NavController
import com.example.direct.data.BatteryInfoProvider
import com.example.direct.data.NetworkInfoProvider
import com.example.direct.data.RamInfoProvider
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen(navController: NavController) {

    val context = LocalContext.current

    var battery by remember { mutableStateOf("Loading...") }
    var ram by remember { mutableStateOf("0%") }
    var net by remember { mutableStateOf("⬇️ 0 KB/s / ⬆️ 0 KB/s") }

    LaunchedEffect(Unit) {
        while (true) {
            BatteryInfoProvider.getBatteryPercent().also { battery = it }

            RamInfoProvider.update(context)
            ram = "${RamInfoProvider.getRamUsedPercent()}%"

            NetworkInfoProvider.update()
            net = "⬇️ ${NetworkInfoProvider.downloadSpeedStr()} / ⬆️ ${NetworkInfoProvider.uploadSpeedStr()}"

            delay(1000)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("📊 Hexo Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        MetricTile("Battery", "🔋 $battery", onClick = { navController.navigate("battery") })
        MetricTile("RAM Usage", "💾 $ram", onClick = { navController.navigate("ram") })
        MetricTile("Network", net, onClick = { navController.navigate("network") })

        MetricTile(
            title = "Battery",
            subtitle = "🔋 ${BatteryInfoProvider.getBatteryPercent()}%",
            onClick = { navController.navigate("battery") }
        )

        MetricTile(
            title = "RAM Usage",
            subtitle = "💾 ${RamInfoProvider.getRamUsedPercent()}%",
            onClick = { navController.navigate("ram") }
        )

        MetricTile(
            title = "Network Speed",
            subtitle = "⬇️ ${NetworkInfoProvider.downloadSpeedStr()} / ⬆️ ${NetworkInfoProvider.uploadSpeedStr()}",
            onClick = { navController.navigate("network") }
        )
    }
}

@Composable
fun MetricTile(title: String, subtitle: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(subtitle, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
