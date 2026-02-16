    package com.benchmarkandroidssc.app.storage

    import android.content.Context
    import android.net.Uri
    import androidx.core.content.FileProvider
    import com.benchmarkandroidssc.app.model.BenchmarkRun
    import com.google.gson.Gson
    import com.google.gson.reflect.TypeToken
    import java.io.File

    object ResultStorage {

        private const val FILE_NAME = "benchmark_runs.json"
        private val gson = Gson()

        private fun getFile(context: Context): File {
            val dir = context.getExternalFilesDir(null)
            return File(dir, FILE_NAME)
        }

        fun loadAll(context: Context): MutableList<BenchmarkRun> {
            val file = getFile(context)
            if (!file.exists()) return mutableListOf()

            val type = object : TypeToken<MutableList<BenchmarkRun>>() {}.type
            return gson.fromJson(file.readText(), type)
        }

        fun save(context: Context, run: BenchmarkRun) {
            val all = loadAll(context)
            all.add(run)
            getFile(context).writeText(gson.toJson(all))
        }

        fun saveIfNotDuplicate(context: Context, run: BenchmarkRun) {
            val all = loadAll(context)

            val alreadySaved = all.any {
                it.deviceInfo == run.deviceInfo &&
                        it.scores.totalScore == run.scores.totalScore
            }

            if (!alreadySaved) {
                all.add(run)
                getFile(context).writeText(gson.toJson(all))
            }
        }
        fun loadLast(context: Context): BenchmarkRun? =
            loadAll(context).lastOrNull()

        fun getExportUri(context: Context): Uri {
            val file = getFile(context)
            return FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        }

        fun importFromUri(context: Context, uri: Uri) {
            val input = context.contentResolver.openInputStream(uri) ?: return
            val dest = getFile(context)

            input.use { inputStream ->
                dest.outputStream().use { output ->
                    inputStream.copyTo(output)
                }
            }
        }
    }
