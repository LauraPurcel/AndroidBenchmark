package com.benchmarkandroidssc.app.ui.charts

import com.benchmarkandroidssc.app.model.BenchmarkRun
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

fun setupChart(
    chart: BarChart,
    runs: List<BenchmarkRun>,
    benchmarkName: String,
    yLabel: String
) {
    val entries = mutableListOf<BarEntry>()
    val labels = mutableListOf<String>()

    runs.forEachIndexed { index, run ->
        val result = run.cpuResults
            .plus(run.memoryResults)
            .firstOrNull { it.name == benchmarkName }

        if (result != null) {
            entries.add(BarEntry(index.toFloat(), result.average.toFloat()))
            labels.add(run.deviceInfo.model)
        }
    }

    val dataSet = BarDataSet(entries, benchmarkName)
    val data = BarData(dataSet)
    data.barWidth = 0.9f

    chart.data = data
    chart.setFitBars(true)

    chart.xAxis.apply {
        position = XAxis.XAxisPosition.BOTTOM
        valueFormatter = IndexAxisValueFormatter(labels)
        granularity = 1f
        setDrawGridLines(false)
    }

    chart.axisLeft.axisMinimum = 0f
    chart.axisRight.isEnabled = false

    chart.description.text = yLabel
    chart.legend.isEnabled = true

    chart.invalidate()
}
