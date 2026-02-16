package com.benchmarkandroidssc.app.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.benchmarkandroidscc.app.ui.gpu.GraphicView
import com.benchmarkandroidssc.app.graphs.CpuGraphActivity
import com.benchmarkandroidssc.app.graphs.MemGraphActivity
import com.benchmarkandroidssc.app.R
import com.benchmarkandroidssc.app.core.BenchmarkRunner
import com.benchmarkandroidssc.app.graphs.GpuGraphActivity
import com.benchmarkandroidssc.app.model.BenchmarkRun
import com.benchmarkandroidssc.app.storage.ResultStorage
import kotlinx.coroutines.launch

class IntegralTestActivity : AppCompatActivity() {

    private lateinit var infoText: TextView
    private lateinit var scoreText: TextView
    private lateinit var gpuView: GraphicView

    private var lastRun: BenchmarkRun? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_integral_test)

        gpuView = findViewById(R.id.gpuView)
        infoText = findViewById(R.id.deviceInfoText)
        scoreText = findViewById(R.id.scoreText)

        val runBtn = findViewById<Button>(R.id.runBench)
        val cpuGraphsBtn = findViewById<Button>(R.id.cpuGraphs)
        val memGraphsBtn = findViewById<Button>(R.id.memGraphs)
        val gpuGraphsBtn = findViewById<Button>(R.id.gpuGraphs)
        val compareBtn = findViewById<Button>(R.id.compareBtn)

        cpuGraphsBtn.isEnabled = false
        memGraphsBtn.isEnabled = false
        gpuGraphsBtn.isEnabled = false
        compareBtn.isEnabled = false

        runBtn.setOnClickListener {
            scoreText.text = "Running benchmarks..."

            lifecycleScope.launch {
                try {
                    val run = BenchmarkRunner.runAll(view = gpuView)
                    lastRun = run
                    ResultStorage.save(this@IntegralTestActivity, run)
                    display(run)
                    cpuGraphsBtn.isEnabled = true
                    memGraphsBtn.isEnabled = true
                    gpuGraphsBtn.isEnabled = true
                    compareBtn.isEnabled = true

                } catch (e: Exception) {
                    scoreText.text = "Benchmark failed: ${e.message}"
                }
            }
        }

        compareBtn.setOnClickListener {

            lastRun?.let {
                ResultStorage.saveIfNotDuplicate(this, it)
            }

            val uri = ResultStorage.getExportUri(this)
            val exportIntent = Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(exportIntent, "Export benchmark results"))
            startActivity(Intent(this, CompareResultsActivity::class.java))
        }

        cpuGraphsBtn.setOnClickListener {
            startActivity(Intent(this, CpuGraphActivity::class.java))
        }

        memGraphsBtn.setOnClickListener {
            startActivity(Intent(this, MemGraphActivity::class.java))
        }

        gpuGraphsBtn.setOnClickListener {
            startActivity(Intent(this, GpuGraphActivity::class.java))
        }
    }

    private fun display(run: BenchmarkRun) {
        val d = run.deviceInfo
        val s = run.scores

        infoText.text = """
            Device: ${d.manufacturer} ${d.model}
            Android: ${d.androidVersion}
            Cores: ${d.cores}
            CPU freq: ${d.cpuFreqMHz ?: "N/A"} MHz
        """.trimIndent()

        scoreText.text = """
            CPU score: ${"%.2f".format(s.cpuScore)}
            Memory score: ${"%.2f".format(s.memoryScore)}
            GPU score: ${"%.2f".format(s.gpuScore)}
            TOTAL score: ${"%.2f".format(s.totalScore)}
        """.trimIndent()
    }
}