package visualization
import org.knowm.xchart.SwingWrapper
import org.knowm.xchart.XYChartBuilder
import java.awt.Color
import javax.swing.SwingUtilities
import kotlin.random.Random

object Chart {
    private val chart = XYChartBuilder()
        .width(600)
        .height(600)
        .title("Paths")
        .xAxisTitle("X")
        .yAxisTitle("Y")
        .build()
    private val swingWrapper = SwingWrapper(chart)
    init {
        displayChart()
    }

    fun updateData(name: String, dataArray: Array<MutableList<Double>>) {
        SwingUtilities.invokeLater {
            chart.updateXYSeries(name, dataArray[0], dataArray[1], null)
            swingWrapper.repaintChart()
        }
    }

    fun containsSeries(name: String) : Boolean = chart.seriesMap.containsKey(name)

    fun addSeries(name: String,
                  dataArray: Array<MutableList<Double>>,
                  color: Color? = null,
                  width: Float = 0.0f) {
        val series = chart.addSeries(name, dataArray[0], dataArray[1])
        series.apply {
            lineColor = color
                ?: Color(
                    Random.nextInt(180, 240),
                    Random.nextInt(180,240),
                    Random.nextInt(180,240)
                )
            if (width != 0.0f) {
                lineWidth = width
            }
        }
    }

    private fun displayChart() {
        try {
            swingWrapper.displayChart()
        } catch(e : Exception) { }
    }
}
