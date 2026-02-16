package com.benchmarkandroidssc.app.storage

import android.content.Context
import com.benchmarkandroidssc.app.model.BenchmarkRun

object LastRunProvider {
    fun get(context: Context): BenchmarkRun? {
        return ResultStorage.loadLast(context)
    }
}
