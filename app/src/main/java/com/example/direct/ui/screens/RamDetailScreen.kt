import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun RamDetailScreen() {
    val context = LocalContext.current

    Column(Modifier.fillMaxSize().padding(16.dp)) {
        Text("💾 RAM Usage Here", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(16.dp))
        RamUsageChart(context)
        SimpleLineChart(
            data = listOf(
                20f, 35f, 50f, 40f, 60f, 70f
            )
        )
    }
}
