package com.benchmarkandroidssc.app

class BenchmarkUtils {

    external fun nativeGetTime(): Long
    external fun quickSortBenchmark(arraySize: Int): Long
    external fun matrixMultiplicationBenchmark(size: Int): Long
    external fun floatMatrixMultiplicationBenchmark(size: Int): Long
    external fun fibonacciBenchmark(n: Int): Long

    external fun memoryLatencyBenchmark(size: Int, iterations: Int): Long
    external fun memoryReadBandwidthBenchmark(size: Int, iterations: Int): Long
    external fun memoryWriteBandwidthBenchmark(size: Int, iterations: Int): Long


    companion object {
        init {
            System.loadLibrary("c_benchmark")

        }
    }

}
