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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.direct.data.BatteryInfoProvider
import com.example.direct.data.NetworkInfoProvider
import com.example.direct.data.RamInfoProvider

@Composable
fun DashboardScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("📊 Hexo Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

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
