package com.benchmarkandroidssc.app.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.benchmarkandroidssc.app.R
import com.benchmarkandroidssc.app.model.BenchmarkResult
import com.benchmarkandroidssc.app.model.BenchmarkRun
import com.benchmarkandroidssc.app.storage.ResultStorage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

class CompareResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compare_results)

        val textView = findViewById<TextView>(R.id.compareText)
        val runs = ResultStorage.loadAll(this)

        if (runs.isEmpty()) {
            textView.text = "No benchmark results found."
            return
        }

        val tableBuilder = StringBuilder()

        val bestRuns = runs
            .groupBy { "${it.deviceInfo.manufacturer} ${it.deviceInfo.model}" }
            .mapValues { it.value.minByOrNull { run -> run.scores.totalScore } }
            .filterValues { it != null }
            .mapValues { it.value!! }

        tableBuilder.append("=============== ANDROID BENCHMARK TABLE ===============\n\n")

        tableBuilder.append(String.format("%-25s", "Test / Device"))
        bestRuns.keys.forEach { device ->
            tableBuilder.append(String.format("| %-15s ", device.take(15)))
        }
        tableBuilder.append("\n")
        tableBuilder.append("-".repeat(25 + bestRuns.size * 18)).append("\n")

        val extractors: List<Pair<String, (BenchmarkRun) -> List<BenchmarkResult>>> = listOf(
            "CPU" to { it.cpuResults },
            "MEMORY" to { it.memoryResults },
            "GPU" to { it.gpuResults }
        )

        extractors.forEach { (_, extractor) ->
            val testNames = bestRuns.values
                .flatMap { extractor(it) }
                .map { it.name }
                .distinct()

            testNames.forEach { testName ->
                tableBuilder.append(String.format("%-25s", testName))

                bestRuns.values.forEach { run ->
                    val result = extractor(run).find { it.name == testName }
                    val value = result?.average ?: 0.0
                    tableBuilder.append(String.format("| %-15.4f ", value))
                }
                tableBuilder.append("\n")
            }
        }

        tableBuilder.append("\n\n")


        val builder = StringBuilder()
        builder.append("=== BEST RESULT PER DEVICE ===\n\n")

        val bestPerDevice = runs
            .groupBy { "${it.deviceInfo.manufacturer} ${it.deviceInfo.model}" }
            .mapValues { entry ->
                entry.value
                    .filter { it.scores.totalScore > 0.0 }
                    .minByOrNull { it.scores.totalScore }
            }
            .filterValues { it != null }
            .mapValues { it.value!! }

        bestPerDevice.values
            .sortedBy { it.scores.totalScore }
            .forEachIndexed { index, run ->
                builder.append(
                    """
                    ${index + 1} (${formatDate(run.timestamp)})
                      Device: ${run.deviceInfo.manufacturer} ${run.deviceInfo.model}
                      CPU: ${format(run.scores.cpuScore)}
                      MEM: ${format(run.scores.memoryScore)}
                      GPU: ${format(run.scores.gpuScore)}
                      TOTAL: ${format(run.scores.totalScore)}
                    
                    """.trimIndent()
                ).append("\n")
            }

        builder.append("\n=== FULL RUN HISTORY ===\n\n")

        runs
            .sortedByDescending { it.timestamp }
            .forEachIndexed { index, run ->
                builder.append(
                    """
                    Run ${index + 1} (${formatDate(run.timestamp)})
                      Device: ${run.deviceInfo.manufacturer} ${run.deviceInfo.model}
                      CPU: ${format(run.scores.cpuScore)}
                      MEM: ${format(run.scores.memoryScore)}
                      GPU: ${format(run.scores.gpuScore)}
                      TOTAL: ${format(run.scores.totalScore)}
                    
                    """.trimIndent()
                ).append("\n")
            }


        textView.text = tableBuilder.toString() + builder.toString()
    }

    private fun format(v: Double): String =
        "${(v * 10).roundToInt() / 10.0}"

    private fun formatDate(timestamp: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return sdf.format(Date(timestamp))
    }
}
