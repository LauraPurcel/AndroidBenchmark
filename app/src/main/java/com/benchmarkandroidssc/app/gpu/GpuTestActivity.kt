package com.benchmarkandroidscc.app.ui.gpu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

import com.benchmarkandroidssc.app.R
class GpuTestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gpu_test)

        val gfxView = findViewById<GraphicView>(R.id.gfxView)
        val btnStart = findViewById<Button>(R.id.btnStart)
        val txtResult = findViewById<TextView>(R.id.txtResult)

        btnStart.setOnClickListener {

            gfxView.startTest(200)
            txtResult.text = "Running..."
            btnStart.postDelayed({
                val frameTimeMs = gfxView.getFrameTimeMs()
                val fps = if (frameTimeMs > 0) 1000.0 / frameTimeMs else 0.0

                txtResult.text = "Frame time: %.2f ms\nFPS: %.1f".format(frameTimeMs, fps)
            }, 3000)
        }

    }
}
