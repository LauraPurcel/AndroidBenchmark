package com.benchmarkandroidssc.app.graphs

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.benchmarkandroidssc.app.R

class GraphSelectActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph_select)

        findViewById<Button>(R.id.cpuGraphs).setOnClickListener {
            startActivity(Intent(this, CpuGraphActivity::class.java))
        }

        findViewById<Button>(R.id.memGraphs).setOnClickListener {
            startActivity(Intent(this, MemGraphActivity::class.java))
        }
    }
}