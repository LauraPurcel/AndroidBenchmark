package com.benchmarkandroidssc.app.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.benchmarkandroidssc.app.BenchmarkUtils
import com.benchmarkandroidssc.app.R

class CpuTestActivity : AppCompatActivity() {

    private val benchmark = BenchmarkUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cpu_test)

        val inputFib = findViewById<EditText>(R.id.inputFib)
        val txtFib = findViewById<EditText>(R.id.txtRezFib)

        findViewById<Button>(R.id.btnTestFib).setOnClickListener {
            val n = inputFib.text.toString().toIntOrNull() ?: 30
            txtFib.setText("Se rulează...")

            Thread {
                for (i in 0..5) {
                    benchmark.fibonacciBenchmark(n)
                }

                var totalDuration = 0.0
                for (i in 0..10) {
                    val duration = benchmark.fibonacciBenchmark(n)
                    totalDuration += duration
                }

                totalDuration = totalDuration/10
                val text = "Fibonacci($n) în ${totalDuration} ns = ${totalDuration/1_000_000} ms"
                runOnUiThread { txtFib.setText(text) }
            }.start()
        }

        val inputMatrixInt = findViewById<EditText>(R.id.inputMatrixInt)
        val txtMatrixInt = findViewById<EditText>(R.id.txtRezMatrixInt)

        findViewById<Button>(R.id.btnTestMatrixInt).setOnClickListener {
            val size = inputMatrixInt.text.toString().toIntOrNull() ?: 100
            txtMatrixInt.setText("Se rulează...")

            Thread {
                for (i in 0..5) {
                    benchmark.matrixMultiplicationBenchmark(size)
                }

                var totalDuration = 0.0
                for (i in 0..10) {
                    val duration = benchmark.matrixMultiplicationBenchmark(size)
                    totalDuration += duration
                }

                totalDuration = totalDuration/10

                val text = "Matrix INT ${size}x${size} în ${totalDuration} ns = ${totalDuration/1_000_000} ms"
                runOnUiThread { txtMatrixInt.setText(text) }
            }.start()
        }

        val inputMatrixFloat = findViewById<EditText>(R.id.inputMatrixFloat)
        val txtMatrixFloat = findViewById<EditText>(R.id.txtRezMatrixFloat)

        findViewById<Button>(R.id.btnTestMatrixFloat).setOnClickListener {
            val size = inputMatrixFloat.text.toString().toIntOrNull() ?: 100
            txtMatrixFloat.setText("Se rulează...")

            Thread {
                for (i in 0..5) {
                    benchmark.floatMatrixMultiplicationBenchmark(size)
                }

                var totalDuration = 0.0
                for (i in 0..10) {
                    val duration = benchmark.floatMatrixMultiplicationBenchmark(size)
                    totalDuration += duration
                }

                totalDuration = totalDuration/10
                val text = "Matrix FLOAT ${size}x${size} în ${totalDuration} ns = ${totalDuration/1_000_000} ms"
                runOnUiThread { txtMatrixFloat.setText(text) }
            }.start()
        }

        val inputSort = findViewById<EditText>(R.id.inputSort)
        val txtSort = findViewById<EditText>(R.id.txtRezSort)

        findViewById<Button>(R.id.btnTestSort).setOnClickListener {
            val size = inputSort.text.toString().toIntOrNull() ?: 100_000
            txtSort.setText("Se rulează...")

            Thread {
                for (i in 0..5) {
                    benchmark.quickSortBenchmark(size)
                }

                var totalDuration = 0.0
                for (i in 0..10) {
                    val duration =  benchmark.quickSortBenchmark(size)
                    totalDuration += duration
                }

                totalDuration = totalDuration/10
                val text = "Quicksort cu $size elemente în ${totalDuration} ns = ${totalDuration/1_000_000} ms"
                runOnUiThread { txtSort.setText(text) }
            }.start()
        }
    }
}