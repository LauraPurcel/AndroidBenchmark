package com.benchmarkandroidssc.app.activity

import android.app.ActivityManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.benchmarkandroidssc.app.R

class SystemInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_system_info)

        val infoText = findViewById<TextView>(R.id.txtSystemInfo)

        val deviceModel = Build.MODEL
        val manufacturer = Build.MANUFACTURER
        val androidVersion = Build.VERSION.RELEASE
        val sdk = Build.VERSION.SDK_INT

        val cpuAbi = Build.SUPPORTED_ABIS.joinToString()

        val activityManager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val memInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memInfo)
        val totalRam = memInfo.totalMem / (1024 * 1024)  // MB

        val internalStorage = getStorageInfo(Environment.getDataDirectory().path)

        val cores = Runtime.getRuntime().availableProcessors()

        val info = """
            ● DEVICE INFO
            Model: $deviceModel
            Manufacturer: $manufacturer
            Android Version: $androidVersion (SDK $sdk)

            ● CPU
            ABI: $cpuAbi
            Cores: $cores

            ● MEMORY
            Total RAM: ${totalRam} MB

            ● STORAGE
            Internal Total: ${internalStorage["total"]} GB
            Internal Free: ${internalStorage["free"]} GB
        """.trimIndent()

        infoText.text = info
    }

    private fun getStorageInfo(path: String): Map<String, Long> {
        val stat = StatFs(path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        val availBlocks = stat.availableBlocksLong

        val total = (totalBlocks * blockSize) / (1024L * 1024L * 1024L)
        val free = (availBlocks * blockSize) / (1024L * 1024L * 1024L)

        return mapOf(
            "total" to total,
            "free" to free
        )
    }
}