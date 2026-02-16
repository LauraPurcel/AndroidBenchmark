package com.benchmarkandroidssc.app.graphs

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.benchmarkandroidssc.app.R
import com.benchmarkandroidssc.app.storage.ResultStorage
import com.benchmarkandroidssc.app.ui.charts.setupChart
import com.github.mikephil.charting.charts.BarChart
import java.text.SimpleDateFormat
import java.util.*

class MemGraphActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mem_graph)

        val runs = ResultStorage.loadAll(this)
        if (runs.isEmpty()) return

        val deviceList = findViewById<TextView>(R.id.deviceList)

        val chartLatency = findViewById<BarChart>(R.id.chartLatency)
        val chartRead = findViewById<BarChart>(R.id.chartRead)
        val chartWrite = findViewById<BarChart>(R.id.chartWrite)

        val charts = listOf(chartLatency, chartRead, chartWrite)

        charts.forEach { chart ->
            chart.xAxis.textColor = Color.parseColor("#ADD8E6")
            chart.axisLeft.textColor = Color.WHITE
            chart.axisRight.isEnabled = false
            chart.legend.textColor = Color.WHITE
            chart.description.textColor = Color.WHITE
        }

        setupChart(chartLatency, runs, "Memory Latency", "Time (ms)")
        setupChart(chartRead, runs, "Memory Read Bandwidth", "MB/s")
        setupChart(chartWrite, runs, "Memory Write Bandwidth", "MB/s")

        val sdf = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
        val sb = StringBuilder()

        runs.forEach { run ->
            sb.append("• ")
                .append(run.deviceInfo.manufacturer)
                .append(" ")
                .append(run.deviceInfo.model)
                .append(" – ")
                .append(sdf.format(Date(run.timestamp)))
                .append("\n")
        }

        deviceList.text = sb.toString()
    }
}
