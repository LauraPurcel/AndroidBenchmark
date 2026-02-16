package com.benchmarkandroidssc.app.model

data class DeviceResult(
    val deviceModel: String,
    val cpu: List<BenchmarkResult>,
    val memory: List<BenchmarkResult>,
    val gpu: List<BenchmarkResult>,
    val scores: ScoreResult
)
