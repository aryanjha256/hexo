
import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.direct.data.RamInfoProvider
import kotlinx.coroutines.delay

@Composable
fun RamUsageChart(context: Context) {
    val maxPoints = 10
    var dataPoints by remember { mutableStateOf<List<Float>>(emptyList()) }

    LaunchedEffect(Unit) {
        while (true) {
            RamInfoProvider.update(context)
            val percent = RamInfoProvider.getRamUsedPercent().toFloat()
            dataPoints = (dataPoints + percent).takeLast(maxPoints)
            delay(1000)
        }
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .height(200.dp)
        .padding(16.dp)
        .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Text("RAM Usage", style = MaterialTheme.typography.labelLarge)
        Spacer(Modifier.height(8.dp))

        Canvas(modifier = Modifier.fillMaxSize()) {
            if (dataPoints.size > 1) {
                val widthStep = size.width / (dataPoints.size - 1)
                val maxHeight = size.height
                val maxValue = 100f // RAM percent scale

                dataPoints.forEachIndexed { index, value ->
                    if (index < dataPoints.lastIndex) {
                        val start = Offset(
                            x = index * widthStep,
                            y = maxHeight - (value / maxValue) * maxHeight
                        )
                        val end = Offset(
                            x = (index + 1) * widthStep,
                            y = maxHeight - (dataPoints[index + 1] / maxValue) * maxHeight
                        )
                        drawLine(
                            color = Color.Green,
                            start = start,
                            end = end,
                            strokeWidth = 4f
                        )
                    }
                }
            }
        }
    }
}
