package com.benchmarkandroidssc.app.activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.benchmarkandroidssc.app.BenchmarkUtils
import com.benchmarkandroidssc.app.R

class MemoryTestActivity : AppCompatActivity() {

    private val benchmark = BenchmarkUtils()

    private companion object {
        const val RUNS = 5
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_memory_test)

        val inputLatencySize = findViewById<EditText>(R.id.inputLatencySize)
        val inputLatencyIter = findViewById<EditText>(R.id.inputLatencyIter)
        val txtLatencyRez = findViewById<TextView>(R.id.txtRezLatency)

        findViewById<Button>(R.id.btnTestLatency).setOnClickListener {
            val size = inputLatencySize.text.toString().toIntOrNull()
                ?: (8 * 1024 * 1024)
            val iter = inputLatencyIter.text.toString().toIntOrNull()
                ?: 5_000_000

            txtLatencyRez.text = "Se rulează..."

            Thread {
                var totalDuration = 0L

                repeat(RUNS) {
                    val d = benchmark.memoryLatencyBenchmark(size, iter)
                    if (d <= 0) {
                        runOnUiThread {
                            txtLatencyRez.text = "Eroare: memorie insuficientă"
                        }
                        return@Thread
                    }
                    totalDuration += d
                }

                val durationNs = totalDuration / RUNS
                val latencyPerAccess = durationNs.toDouble() / iter

                val text = """
                    Latență memorie
                    Total: $durationNs ns
                    Per acces: ${"%.2f".format(latencyPerAccess)} ns
                """.trimIndent()

                runOnUiThread { txtLatencyRez.text = text }
            }.start()
        }

        val inputReadSize = findViewById<EditText>(R.id.inputReadSize)
        val inputReadIter = findViewById<EditText>(R.id.inputReadIter)
        val txtReadRez = findViewById<TextView>(R.id.txtRezRead)

        findViewById<Button>(R.id.btnTestRead).setOnClickListener {
            val size = inputReadSize.text.toString().toIntOrNull()
                ?: (16 * 1024 * 1024)
            val iter = inputReadIter.text.toString().toIntOrNull() ?: 10

            txtReadRez.text = "Se rulează..."

            Thread {
                var totalTime = 0L

                repeat(RUNS) {
                    val t = benchmark.memoryReadBandwidthBenchmark(size, iter)
                    if (t <= 0) {
                        runOnUiThread {
                            txtReadRez.text = "Eroare: memorie insuficientă"
                        }
                        return@Thread
                    }
                    totalTime += t
                }

                val timeNs = totalTime / RUNS
                val totalBytes = size.toLong() * iter
                val seconds = timeNs / 1e9
                val mbps = (totalBytes / (1024.0 * 1024.0)) / seconds

                val text = """
                    Bandwidth citire
                    Timp: ${timeNs / 1_000_000} ms
                    Viteză: ${"%.2f".format(mbps)} MB/s
                """.trimIndent()

                runOnUiThread { txtReadRez.text = text }
            }.start()
        }

        val inputWriteSize = findViewById<EditText>(R.id.inputWriteSize)
        val inputWriteIter = findViewById<EditText>(R.id.inputWriteIter)
        val txtWriteRez = findViewById<TextView>(R.id.txtRezWrite)

        findViewById<Button>(R.id.btnTestWrite).setOnClickListener {
            val size = inputWriteSize.text.toString().toIntOrNull()
                ?: (16 * 1024 * 1024)
            val iter = inputWriteIter.text.toString().toIntOrNull() ?: 10

            txtWriteRez.text = "Se rulează..."

            Thread {
                var totalTime = 0L

                repeat(RUNS) {
                    val t = benchmark.memoryWriteBandwidthBenchmark(size, iter)
                    if (t <= 0) {
                        runOnUiThread {
                            txtWriteRez.text = "Eroare: memorie insuficientă"
                        }
                        return@Thread
                    }
                    totalTime += t
                }

                val timeNs = totalTime / RUNS
                val totalBytes = size.toLong() * iter
                val seconds = timeNs / 1e9
                val mbps = (totalBytes / (1024.0 * 1024.0)) / seconds

                val text = """
                    Bandwidth scriere
                    Timp: ${timeNs / 1_000_000} ms
                    Viteză: ${"%.2f".format(mbps)} MB/s
                """.trimIndent()

                runOnUiThread { txtWriteRez.text = text }
            }.start()
        }
    }
}