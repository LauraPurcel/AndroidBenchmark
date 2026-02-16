package com.benchmarkandroidssc.app.graphs

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.benchmarkandroidssc.app.R
import com.benchmarkandroidssc.app.storage.ResultStorage
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import kotlin.random.Random
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
class GpuGraphActivity : AppCompatActivity() {

    private lateinit var chart: LineChart
    private val objectCounts = listOf(10f, 25f, 50f, 100f, 200f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gpu_graph)

        chart = findViewById(R.id.gpuChart)
        val deviceList = findViewById<TextView>(R.id.deviceList)

        val runs = ResultStorage.loadAll(this)
        val dataSets = mutableListOf<ILineDataSet>()
        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault())

        chart.legend.isEnabled = false

        val sb = StringBuilder()

        runs.forEach { run ->
            val gpu = run.gpuResults.firstOrNull() ?: return@forEach
            val date = sdf.format(Date(run.timestamp))
            sb.append("• ${run.deviceInfo.manufacturer} ${run.deviceInfo.model} – ")
                .append("  Rulare: $date\n")

            val entries = gpu.values.mapIndexed { i, value ->
                Entry(
                    objectCounts.getOrElse(i) { i.toFloat() },
                    value.toFloat()
                )
            }

            val label = "${run.deviceInfo.manufacturer} ${run.deviceInfo.model}"

            val set = LineDataSet(entries, label).apply {
                color = randomColor()
                setCircleColor(color)
                lineWidth = 2f
                circleRadius = 4f
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }

            dataSets.add(set)
        }

        deviceList.text = sb.toString()

        chart.xAxis.textColor = Color.parseColor("#ADD8E6")
        chart.axisLeft.textColor = Color.WHITE
        chart.axisRight.textColor = Color.WHITE
        chart.description.textColor = Color.WHITE
        chart.axisRight.isEnabled = false

        chart.data = LineData(dataSets)
        chart.description.text = "GPU Render Time (ms)"
        chart.invalidate()
    }

    private fun randomColor(): Int =
        Color.rgb(
            Random.nextInt(50, 255),
            Random.nextInt(50, 255),
            Random.nextInt(50, 255)
        )
}
