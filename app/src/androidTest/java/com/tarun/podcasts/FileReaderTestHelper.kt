package com.tarun.podcasts

import android.content.Context
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader

/**
 * A utility class to provide some helper functions for reading data from local files.
 */
object FileReaderTestHelper {
    @Throws(Exception::class)
    fun getStringFromFile(
        context: Context,
        filePath: String?
    ): String {
        val stream = context.resources.assets.open(filePath!!)
        val ret = convertStreamToString(stream)
        //Make sure you close all streams.
        stream.close()
        return ret
    }

    @Throws(Exception::class)
    private fun convertStreamToString(`is`: InputStream?): String {
        val reader = BufferedReader(InputStreamReader(`is`))
        val sb = StringBuilder()
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            sb.append(line).append("\n")
        }
        reader.close()
        return sb.toString()
    }
}