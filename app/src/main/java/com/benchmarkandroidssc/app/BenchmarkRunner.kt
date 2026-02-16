package com.benchmarkandroidssc.app.core

import com.benchmarkandroidscc.app.ui.gpu.GraphicView
import com.benchmarkandroidssc.app.BenchmarkUtils
import com.benchmarkandroidssc.app.device.DeviceInfoProvider
import com.benchmarkandroidssc.app.model.*
import kotlinx.coroutines.delay

object BenchmarkRunner {

    private const val WARMUP_RUNS = 5
    private const val MEASURE_RUNS = 10
    private val GPU_OBJECT_COUNTS = listOf(10, 25, 50, 100, 200)
    private val native = BenchmarkUtils()
     suspend fun runAll(view: GraphicView): BenchmarkRun {
        val deviceInfo = DeviceInfoProvider.collect()

        val cpu = runCpuBenchmarks()
        val mem = runMemoryBenchmarks()
        val gpu = listOf(runGpuBenchmark(view))

        val scores = ScoreCalculator.calculate(cpu, mem, gpu)

        return BenchmarkRun(
            timestamp = System.currentTimeMillis(),
            deviceInfo = deviceInfo,
            cpuResults = cpu,
            memoryResults = mem,
            gpuResults = gpu,
            scores = scores
        )
    }
    private suspend fun runGpuBenchmark(view: GraphicView): BenchmarkResult {

        val values = mutableListOf<Double>()

        for (objects in GPU_OBJECT_COUNTS) {
            repeat(WARMUP_RUNS) {
                view.startTest(objects)
                delay(1000)
            }

            val local = mutableListOf<Double>()

            repeat(MEASURE_RUNS) {
                view.startTest(objects)
                delay(1500)
                val t = view.getFrameTimeMs()
                if (t > 0 && t.isFinite()) {
                    local.add(t)
                }
            }

            values.add(local.average())
        }

        return BenchmarkResult(
            name = "GPU Render Time (ms)",
            values = values,
            average = values.average()
        )
    }

    private fun runCpuBenchmarks(): List<BenchmarkResult> {
        return listOf(
            runNativeBenchmark("Fibonacci (native)") {
                native.fibonacciBenchmark(35)
            },
            runNativeBenchmark("QuickSort (native)") {
                native.quickSortBenchmark(50_000)
            },
            runNativeBenchmark("Int Matrix (native)") {
                native.matrixMultiplicationBenchmark(150)
            },
            runNativeBenchmark("Float Matrix (native)") {
                native.floatMatrixMultiplicationBenchmark(150)
            }
        )
    }
    private fun runMemoryBenchmarks(): List<BenchmarkResult> {
        return listOf(
            runNativeBenchmark("Memory Latency") {
                native.memoryLatencyBenchmark(
                    size = 8 * 1024 * 1024,
                    iterations = 5_000_000
                )
            },
            runNativeBenchmark("Memory Read Bandwidth") {
                native.memoryReadBandwidthBenchmark(
                    size = 16 * 1024 * 1024,
                    iterations = 50
                )
            },
            runNativeBenchmark("Memory Write Bandwidth") {
                native.memoryWriteBandwidthBenchmark(
                    size = 16 * 1024 * 1024,
                    iterations = 50
                )
            }
        )
    }

    private fun runNativeBenchmark(
        name: String,
        nativeCall: () -> Long
    ): BenchmarkResult {

        repeat(WARMUP_RUNS) { nativeCall() }

        val values = mutableListOf<Double>()

        repeat(MEASURE_RUNS) {
            val timeNs = nativeCall()
            values.add(timeNs / 1_000_000.0)
        }

        return BenchmarkResult(
            name = name,
            values = values,
            average = values.average()
        )
    }
}
