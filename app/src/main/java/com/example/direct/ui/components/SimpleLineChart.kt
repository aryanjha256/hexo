
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries

@Composable
fun SimpleLineChart(
    data: List<Float>,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
) {
    // Convert List<Float> to ChartEntry
//    val entries = data.mapIndexed { index, value -> FloatEntry(index.toFloat(), value) }
//    val model = ChartEntryModelProducer(entries)
//
//    val chartStyle = rememberLineChartStyle(
//        lines = listOf(
//            lineChart(
//                lines = listOf(
//                    lineChart.LineSpec(
//                        lineColor = color,
//                        lineThicknessDp = 2f,
//                        pointSizeDp = 4f
//                    )
//                )
//            )
//        )
//    )
//
//    Box(
//        modifier = modifier
//            .fillMaxWidth()
//            .height(200.dp)
//            .padding(horizontal = 16.dp)
//    ) {
//        Chart(
//            chart = lineChart(),
//            chartModelProducer = model,
//            startAxis = null,
//            bottomAxis = null,
//            chartStyle = chartStyle
//        )
//    }

    val modelProducer = remember { CartesianChartModelProducer() }
    LaunchedEffect(Unit) {
        modelProducer.runTransaction {
            columnSeries { series(data) }
        }
    }
    CartesianChartHost(
        rememberCartesianChart(
            rememberColumnCartesianLayer(),
            startAxis = VerticalAxis.rememberStart(),
            bottomAxis = HorizontalAxis.rememberBottom(),
        ),
        modelProducer,
    )
}
