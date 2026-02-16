package com.benchmarkandroidssc.app.device

import android.os.Build
import com.benchmarkandroidssc.app.model.DeviceInfo
import java.io.File

object DeviceInfoProvider {

    fun collect(): DeviceInfo {
        return DeviceInfo(
            manufacturer = Build.MANUFACTURER,
            model = Build.MODEL,
            androidVersion = Build.VERSION.RELEASE,
            cores = Runtime.getRuntime().availableProcessors(),
            cpuFreqMHz = readCpuFreq(),
            gpuName = null // TODO
        )
    }

    private fun readCpuFreq(): Int? {
        return try {
            val file = File("/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq")
            if (file.exists()) file.readText().trim().toInt() / 1000 else null
        } catch (e: Exception) {
            null
        }
    }
}
