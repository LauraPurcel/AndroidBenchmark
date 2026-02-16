package com.benchmarkandroidssc.app.model

data class DeviceInfo(
    val manufacturer: String,
    val model: String,
    val androidVersion: String,
    val cores: Int,
    val cpuFreqMHz: Int?,
    val gpuName: String?
)
