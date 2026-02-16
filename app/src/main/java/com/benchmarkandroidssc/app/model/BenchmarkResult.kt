package com.benchmarkandroidssc.app.model

data class BenchmarkResult(
    val name: String,
    val values: List<Double>,
    val average: Double
)