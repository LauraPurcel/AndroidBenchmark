package com.benchmarkandroidssc.app.core

import com.benchmarkandroidssc.app.model.BenchmarkResult
import com.benchmarkandroidssc.app.model.ScoreResult

object ScoreCalculator {

    fun calculate(
        cpu: List<BenchmarkResult>,
        mem: List<BenchmarkResult>,
        gpu: List<BenchmarkResult>
    ): ScoreResult {

        fun scale(values: List<Double>): List<Double> {
            if (values.isEmpty()) return emptyList()

            val min = values.min()
            val max = values.max()
            val range = max - min

            return values.map { v ->
                if (range == 0.0)
                    100.0
                else (v - min) / range * 100.0
            }
        }

        val scaledCpu = scale(cpu.map { it.average })
        val scaledMem = scale(mem.map { it.average })
        val scaledGpu = scale(gpu.map { it.average })

        val cpuScore = scaledCpu.average()
        val memScore = scaledMem.average()
        val gpuScore = scaledGpu.average()

        val total = cpuScore * 0.4 + memScore * 0.4 + gpuScore * 0.2

        return ScoreResult(
            cpuScore = cpuScore,
            memoryScore = memScore,
            gpuScore = gpuScore,
            totalScore = total
        )
    }
}
