package com.benchmarkandroidssc.app.model

data class BenchmarkRun(
    val timestamp: Long,
    val deviceInfo: DeviceInfo,
    val cpuResults: List<BenchmarkResult>,
    val memoryResults: List<BenchmarkResult>,
    val gpuResults: List<BenchmarkResult>,
    val scores: ScoreResult
)
