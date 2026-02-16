package com.benchmarkandroidssc.app.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.benchmarkandroidscc.app.ui.gpu.GpuTestActivity
import com.benchmarkandroidssc.app.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cpuBtn = findViewById<Button>(R.id.btnCpu)
        val memBtn = findViewById<Button>(R.id.btnMemory)
        val gpuBtn = findViewById<Button>(R.id.btnGpu)
        val sysBtn = findViewById<Button>(R.id.btnSystem)
        val integralTestBtn = findViewById<Button>(R.id.btnIntegral)

        cpuBtn.setOnClickListener {
            startActivity(Intent(this, CpuTestActivity::class.java))
        }

        integralTestBtn.setOnClickListener {
            startActivity(Intent(this, IntegralTestActivity::class.java))
        }


        memBtn.setOnClickListener {
            startActivity(Intent(this, MemoryTestActivity::class.java))
        }

        sysBtn.setOnClickListener {
            startActivity(Intent(this, SystemInfoActivity::class.java))
        }

        gpuBtn.setOnClickListener {
            startActivity(Intent(this, GpuTestActivity::class.java))
        }
    }
}