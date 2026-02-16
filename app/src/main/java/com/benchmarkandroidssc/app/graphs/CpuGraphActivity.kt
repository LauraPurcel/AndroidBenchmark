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

class CpuGraphActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpu_graph)

        val runs = ResultStorage.loadAll(this)
        if (runs.isEmpty()) return

        val deviceList = findViewById<TextView>(R.id.deviceList)

        val chartFibo = findViewById<BarChart>(R.id.chartFibo)
        val chartSort = findViewById<BarChart>(R.id.chartSort)
        val chartIntMatrix = findViewById<BarChart>(R.id.chartIntMatrix)
        val chartFloatMatrix = findViewById<BarChart>(R.id.chartFloatMatrix)

        val charts = listOf(chartFibo, chartSort, chartIntMatrix, chartFloatMatrix)

        charts.forEach { chart ->
            chart.xAxis.textColor = Color.parseColor("#ADD8E6")
            chart.axisLeft.textColor = Color.WHITE
            chart.axisRight.isEnabled = false
            chart.legend.textColor = Color.WHITE
            chart.description.textColor = Color.WHITE
        }

        setupChart(chartFibo, runs, "Fibonacci (native)", "Time (ms)")
        setupChart(chartSort, runs, "QuickSort (native)", "Time (ms)")
        setupChart(chartIntMatrix, runs, "Int Matrix (native)", "Time (ms)")
        setupChart(chartFloatMatrix, runs, "Float Matrix (native)", "Time (ms)")

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
